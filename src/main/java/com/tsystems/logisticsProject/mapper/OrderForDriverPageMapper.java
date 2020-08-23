package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dao.CargoDao;
import com.tsystems.logisticsProject.dao.WaypointDao;
import com.tsystems.logisticsProject.dto.OrderDtoForDriverPage;
import com.tsystems.logisticsProject.dto.WaypointDto;
import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Waypoint;
import com.tsystems.logisticsProject.entity.enums.Action;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class OrderForDriverPageMapper {

    private ModelMapper modelMapper;
    private WaypointMapper waypointMapper;
    private CargoDao cargoDao;
    private WaypointDao waypointDao;

    @Autowired
    public void setDependencies(ModelMapper modelMapper, CargoDao cargoDao, WaypointDao waypointDao,
                                WaypointMapper waypointMapper) {
        this.modelMapper = modelMapper;
        this.waypointMapper = waypointMapper;
        this.cargoDao = cargoDao;
        this.waypointDao = waypointDao;
    }

    public Order toEntity(OrderDtoForDriverPage dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Order.class);
    }

    public OrderDtoForDriverPage toDto(Order entity) {
        return Objects.isNull(entity) ? null : modelMapper.map(entity, OrderDtoForDriverPage.class);
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Order.class, OrderDtoForDriverPage.class)
                .addMappings(m -> m.skip(OrderDtoForDriverPage::setWaypointsDto)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderDtoForDriverPage::setTruckNumber)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(OrderDtoForDriverPage.class, Order.class)
                .addMappings(m -> m.skip(Order::setCargoes)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Order::setDrivers))
                .addMappings(m -> m.skip(Order::setOrderTruck))
                .addMappings(m -> m.skip(Order::setCompletionDate));
    }

    public Converter<OrderDtoForDriverPage, Order> toEntityConverter() {
        return context -> {
            OrderDtoForDriverPage source = context.getSource();
            Order destination = context.getDestination();
            mapSpecificFieldsForEntity(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForEntity(OrderDtoForDriverPage source, Order destination) {
        if (source != null) {
            if (source.getWaypointsDto() == null) {
                destination.setCargoes(null);
            }
            List<WaypointDto> waypointsDto = source.getWaypointsDto();
            List<Cargo> listOfCargoes = new ArrayList<>();
            for (WaypointDto waypoint : waypointsDto) {
                if (waypoint.getAction() == Action.LOADING) {
                    listOfCargoes.add(cargoDao.findByNumber(waypoint.getCargoNumber()));
                }
            }
            destination.setCargoes(listOfCargoes);

            for (Cargo cargo : listOfCargoes) {
                List<Waypoint> listOfWaypoints = new ArrayList<>();

                for (WaypointDto waypointDto: waypointsDto) {
                    Waypoint waypoint = waypointMapper.toEntity(waypointDto);
                    if (waypoint.getCargo().equals(cargo)) {
                        listOfWaypoints.add(waypoint);
                    }
                }
                cargo.setWaypoints(listOfWaypoints);
            }

        }
    }


    public Converter<Order, OrderDtoForDriverPage> toDtoConverter() {
        return context -> {
            Order source = context.getSource();
            OrderDtoForDriverPage destination = context.getDestination();
            mapSpecificFieldsForDto(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForDto(Order source, OrderDtoForDriverPage destination) {
        if (source != null) {
            destination.setTruckNumber(Objects.isNull(source.getOrderTruck()) ? null :
                    source.getOrderTruck().getNumber());

            List<Waypoint> listOfWaypoint = waypointDao.getListOfWaypointsByOrderId(source.getId());
            if (listOfWaypoint == null) {
                destination.setWaypointsDto(null);
            } else {
                List<WaypointDto> listOfWaypointsDto = new ArrayList<>();
                for (Waypoint waypoint : listOfWaypoint) {
                    listOfWaypointsDto.add(waypointMapper.toDto(waypoint));
                }
                destination.setWaypointsDto(listOfWaypointsDto);
            }
        }
    }
}

