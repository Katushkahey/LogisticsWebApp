package com.tsystems.logisticsProject.service.impl;

import com.tsystems.logisticsProject.dao.OrderDao;
import com.tsystems.logisticsProject.dto.CargoDto;
import com.tsystems.logisticsProject.dto.NewOrderDto;
import com.tsystems.logisticsProject.dto.NewOrderWaypointDto;
import com.tsystems.logisticsProject.entity.enums.Action;
import com.tsystems.logisticsProject.event.UpdateEvent;
import com.tsystems.logisticsProject.exception.checked.TooLargeOrderTotalWeightException;
import com.tsystems.logisticsProject.mapper.NewOrderMapper;
import com.tsystems.logisticsProject.service.TruckService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.logging.Logger;

@Data
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RawOrderSessionService {

    private ApplicationEventPublisher applicationEventPublisher;
    private NewOrderMapper newOrderMapper;
    private TruckService truckService;
    private OrderDao orderDao;

    private List<CargoDto> listOfCargoesToUnload;
    private List<CargoDto> listOfAllCargoes;
    private List<NewOrderWaypointDto> listOfWaypoints;
    private Double totalWeight = 0.0;
    NewOrderDto orderDto;

    private static final Logger LOG = Logger.getLogger(OrderAssignmentService.class.getName());

    @Autowired
    public RawOrderSessionService(TruckService truckService, OrderDao orderDao, NewOrderMapper newOrderMapper,
                                  ApplicationEventPublisher applicationEventPublisher) {
        this.truckService = truckService;
        this.orderDao = orderDao;
        this.newOrderMapper = newOrderMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostConstruct
    public void init() {
        orderDto = new NewOrderDto();
        listOfCargoesToUnload = new ArrayList<>();
        listOfAllCargoes = new ArrayList<>();
        listOfWaypoints = new ArrayList<>();
        orderDto.setCargoes(listOfAllCargoes);
        orderDto.setWaypoints(listOfWaypoints);
        orderDto.setCargoesToUnload(listOfCargoesToUnload);
    }

    public void addLoadingWaypoint(NewOrderWaypointDto waypointDto) throws TooLargeOrderTotalWeightException {
        if (totalWeight + waypointDto.getCargoWeight() > truckService.getMaxCapacity()) {
            LOG.info("Total weight of new order is too large");
            throw new TooLargeOrderTotalWeightException(totalWeight + waypointDto.getCargoWeight(), truckService.getMaxCapacity());
        } else {
            waypointDto.setId(listOfWaypoints.isEmpty() ? 1L : listOfWaypoints.get(listOfWaypoints.size() - 1).getId() + 1);
            createNewCargo(waypointDto);
            totalWeight += waypointDto.getCargoWeight();
            listOfWaypoints.add(waypointDto);
        }
    }

    private void createNewCargo(NewOrderWaypointDto waypointDto) {
        CargoDto cargoDto = new CargoDto();
        cargoDto.setName(waypointDto.getCargoName());
        String cargoNumber = UUID.randomUUID().toString();
        cargoDto.setNumber(cargoNumber);
        waypointDto.setCargoNumber(cargoNumber);
        cargoDto.setWeight(waypointDto.getCargoWeight());
        cargoDto.setId(listOfAllCargoes.isEmpty() ? 1L : listOfAllCargoes.get(listOfAllCargoes.size() - 1).getId() + 1);
        listOfAllCargoes.add(cargoDto);
        listOfCargoesToUnload.add(cargoDto);
    }

    public void addUnloadingWaypoint(NewOrderWaypointDto waypointDto) {
        waypointDto.setId(listOfWaypoints.get(listOfWaypoints.size() - 1).getId() + 1);

        for (CargoDto cargoDto : listOfCargoesToUnload) {
            if (cargoDto.getNumber().equals(waypointDto.getCargoNumber())) {
                waypointDto.setCargoName(cargoDto.getName());
                waypointDto.setCargoWeight(cargoDto.getWeight());
                listOfCargoesToUnload.remove(cargoDto);
                break;
            }
        }
        listOfWaypoints.add(waypointDto);
        totalWeight -= waypointDto.getCargoWeight();
    }

    public void editWaypoint(NewOrderWaypointDto waypointDto) throws TooLargeOrderTotalWeightException {
        for (NewOrderWaypointDto waypointDto1 : listOfWaypoints) {
            if (waypointDto1.getId() == waypointDto.getId()) {
                if (totalWeight - waypointDto1.getCargoWeight() + waypointDto.getCargoWeight() > truckService.getMaxCapacity()) {
                    LOG.info("Total weight of new order is too large");
                    throw new TooLargeOrderTotalWeightException(totalWeight - waypointDto1.getCargoWeight()
                            + waypointDto.getCargoWeight(), truckService.getMaxCapacity());
                }
                waypointDto1.setCargoWeight(waypointDto.getCargoWeight());
                waypointDto1.setCargoName(waypointDto.getCargoName());
                waypointDto1.setCityName(waypointDto.getCityName());
                editCargoInList(listOfAllCargoes, waypointDto);
                editCargoInList(listOfCargoesToUnload, waypointDto);
                break;
            }
        }
    }

    private void editCargoInList(List<CargoDto> list, NewOrderWaypointDto waypointDto) {
        for (CargoDto cargoDto : list) {
            if (cargoDto.getNumber().equals(waypointDto.getCargoNumber())) {
                cargoDto.setName(waypointDto.getCargoName());
                cargoDto.setWeight(waypointDto.getCargoWeight());
                break;
            }
        }
    }

    public void deleteWaypointById(Long id) throws TooLargeOrderTotalWeightException {
        for (NewOrderWaypointDto waypointDto : listOfWaypoints) {
            if (waypointDto.getId() == id) {
                if (waypointDto.getAction().equals(Action.LOADING.toString())) {
                    deleteLoadingWaypoint(waypointDto);
                    break;
                } else {
                    deleteUnLoadingWaypoint(waypointDto);
                    break;
                }
            }
        }
    }

    private void deleteLoadingWaypoint(NewOrderWaypointDto waypointDto) {
        for (NewOrderWaypointDto waypointDto1 : listOfWaypoints) {
            if (waypointDto1.getCargoNumber().equals(waypointDto.getCargoNumber())
                    && waypointDto1.getAction().equals(Action.UNLOADING.toString())) {
                listOfWaypoints.remove(waypointDto1);
                totalWeight += waypointDto1.getCargoWeight();
                break;
            }
        }
        listOfWaypoints.remove(waypointDto);
        totalWeight -= waypointDto.getCargoWeight();
        deleteCargoFromList(listOfCargoesToUnload, waypointDto.getCargoNumber());
        deleteCargoFromList(listOfAllCargoes, waypointDto.getCargoNumber());
    }

    private void deleteCargoFromList(List<CargoDto> list, String cargoNumber) {
        for (CargoDto cargoDto : list) {
            if (cargoDto.getNumber().equals(cargoNumber)) {
                list.remove(cargoDto);
                break;
            }
        }
    }

    private void deleteUnLoadingWaypoint(NewOrderWaypointDto waypointDto) throws TooLargeOrderTotalWeightException {
        if (totalWeight + waypointDto.getCargoWeight() > truckService.getMaxCapacity()) {
            LOG.info("Total weight of new order is too large");
            throw new TooLargeOrderTotalWeightException(waypointDto.getCargoWeight(), truckService.getMaxCapacity());
        } else {
            totalWeight += waypointDto.getCargoWeight();
            listOfWaypoints.remove(waypointDto);
            addCargoToListForUnloadingAfterDeletingUnloadingPoint(waypointDto.getCargoNumber());
        }
    }

    private void addCargoToListForUnloadingAfterDeletingUnloadingPoint(String cargoNumber) {
        for (CargoDto cargoDto : listOfAllCargoes) {
            if (cargoDto.getNumber().equals(cargoNumber)) {
                listOfCargoesToUnload.add(cargoDto);
            }
        }
    }

    @Transactional
    public void saveOrder() {
        orderDao.add(newOrderMapper.toEntity(orderDto));
        clearAll();
        applicationEventPublisher.publishEvent(new UpdateEvent() {
        });
    }

    public void clearAll() {
        listOfCargoesToUnload.clear();
        listOfWaypoints.clear();
        listOfAllCargoes.clear();
        totalWeight = 0.0;
    }
}
