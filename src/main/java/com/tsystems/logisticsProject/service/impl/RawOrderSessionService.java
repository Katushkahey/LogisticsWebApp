package com.tsystems.logisticsProject.service.impl;

import com.tsystems.logisticsProject.dao.OrderDao;
import com.tsystems.logisticsProject.dto.CargoDto;
import com.tsystems.logisticsProject.dto.NewOrderDto;
import com.tsystems.logisticsProject.dto.NewOrderWaypointDto;
import com.tsystems.logisticsProject.dto.WaypointDto;
import com.tsystems.logisticsProject.entity.enums.Action;
import com.tsystems.logisticsProject.mapper.NewOrderMapper;
import com.tsystems.logisticsProject.service.TruckService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.*;

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

    @Autowired
    public RawOrderSessionService (TruckService truckService, OrderDao orderDao, NewOrderMapper newOrderMapper,
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

    public void addLoadingWaypoint(NewOrderWaypointDto waypointDto) {
        if (totalWeight + waypointDto.getCargoWeight() > truckService.getMaxCapacity()) {
            throw new NullPointerException();
        } else {
            waypointDto.setId(Objects.isNull(listOfWaypoints) ? 1L : listOfWaypoints.get(listOfWaypoints.size() - 1).getId() + 1);
            createNewCargo(waypointDto);
            totalWeight += waypointDto.getCargoWeight();
        }
    }

    private void createNewCargo(NewOrderWaypointDto waypointDto) {
        CargoDto cargoDto = new CargoDto();
        cargoDto.setName(waypointDto.getCargoName());
        cargoDto.setNumber("cоздать уникальный номер");
        cargoDto.setWeight(waypointDto.getCargoWeight());
        cargoDto.setId(Objects.isNull(listOfAllCargoes) ? 1L : listOfAllCargoes.get(listOfAllCargoes.size() - 1).getId() + 1);
        listOfAllCargoes.add(cargoDto);
        listOfCargoesToUnload.add(cargoDto);
    }

    public void addUnloadingWaypoint(NewOrderWaypointDto waypointDto) {
        waypointDto.setId(listOfWaypoints.get(listOfAllCargoes.size() - 1).getId() + 1);
        listOfWaypoints.add(waypointDto);
        for (CargoDto cargoDto: listOfCargoesToUnload) {
            if (cargoDto.getNumber().equals(waypointDto.getCargoNumber())) {
                listOfCargoesToUnload.remove(cargoDto);
                break;
            }
        }
        totalWeight -= waypointDto.getCargoWeight();
    }

    public void editWaypoint(NewOrderWaypointDto waypointDto) {
        for (NewOrderWaypointDto waypointDto1: listOfWaypoints) {
            if (waypointDto1.getId() == waypointDto.getId()) {
                if (totalWeight - waypointDto1.getCargoWeight() + waypointDto.getCargoWeight() > truckService.getMaxCapacity()) {
                    throw new NullPointerException();
                }
                waypointDto1.setCargoNumber(waypointDto.getCargoNumber());
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
        for (CargoDto cargoDto: list) {
            if (cargoDto.getNumber().equals(waypointDto.getCargoNumber())) {
                cargoDto.setName(waypointDto.getCargoName());
                cargoDto.setWeight(waypointDto.getCargoWeight());
                break;
            }
        }
    }

    public void deleteWaypointById(Long id) {
        for (NewOrderWaypointDto waypointDto: listOfWaypoints) {
            if (waypointDto.getId() == id) {
                if (waypointDto.getAction().equals(Action.LOADING.toString())) {
                    deleteLoadingWaypoint(waypointDto);
                } else {
                    deleteUnLoadingWaypoint(waypointDto);
                }
            }
        }
    }

    private void deleteLoadingWaypoint(NewOrderWaypointDto waypointDto) {
        for (NewOrderWaypointDto waypointDto1: listOfWaypoints) {
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
        for (CargoDto cargoDto: list) {
            if (cargoDto.getNumber().equals(cargoNumber)) {
                list.remove(cargoDto);
                break;
            }
        }
    }

    private void deleteUnLoadingWaypoint(NewOrderWaypointDto waypointDto) {
        listOfWaypoints.remove(waypointDto);
        if (totalWeight + waypointDto.getCargoWeight() > truckService.getMaxCapacity()) {
            throw new NullPointerException();
        } else {
            totalWeight += waypointDto.getCargoWeight();
            addCargoToListForUnloadingAfterDeletingUnloadingPoint(waypointDto.getCargoNumber());
        }
    }

    private void addCargoToListForUnloadingAfterDeletingUnloadingPoint(String cargoNumber) {
        for (CargoDto cargoDto: listOfAllCargoes) {
            if (cargoDto.getNumber().equals(cargoNumber)) {
                listOfCargoesToUnload.add(cargoDto);
            }
        }
    }

    public void saveOrder() {
        orderDao.add(newOrderMapper.toEntity(orderDto));
    }

    public void clearAll() {
        listOfCargoesToUnload.clear();
        listOfWaypoints.clear();
        listOfAllCargoes.clear();
    }
}
