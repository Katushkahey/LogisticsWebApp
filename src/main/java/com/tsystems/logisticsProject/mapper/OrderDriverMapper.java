package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dao.*;
import com.tsystems.logisticsProject.dto.DriverShortDto;
import com.tsystems.logisticsProject.dto.OrderDriverDto;
import com.tsystems.logisticsProject.dto.WaypointDto;
import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.entity.Waypoint;
import com.tsystems.logisticsProject.entity.enums.DriverState;
import com.tsystems.logisticsProject.entity.enums.OrderStatus;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class OrderDriverMapper {

    private ModelMapper modelMapper;
    private DriverShortMapper driverShortMapper;
    private WaypointMapper waypointMapper;
    private OrderDao orderDao;
    private DriverDao driverDao;
    private WaypointDao waypointDao;
    private TruckDao truckDao;

    @Autowired
    public void setDependencies(ModelMapper modelMapper, WaypointMapper waypointMapper, OrderDao orderDao,
                               WaypointDao waypointDao, TruckDao truckDao, DriverShortMapper driverShortMapper,
                                DriverDao driverDao) {
        this.modelMapper = modelMapper;
        this.waypointMapper = waypointMapper;
        this.driverShortMapper = driverShortMapper;
        this.orderDao = orderDao;
        this.waypointDao = waypointDao;
        this.truckDao = truckDao;
        this.driverDao = driverDao;
    }

    public Order toEntity(OrderDriverDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Order.class);
    }

    public OrderDriverDto toDto(Order entity) {
        return Objects.isNull(entity) ? null : modelMapper.map(entity, OrderDriverDto.class);
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Order.class, OrderDriverDto.class)
                .addMappings(m -> m.skip(OrderDriverDto::setTruckNumber)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderDriverDto::setStatus)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderDriverDto::setWaypoints)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(OrderDriverDto.class, Order.class)
                .addMappings(m -> m.skip(Order::setOrderTruck)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Order::setCargoes)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Order::setDrivers)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Order::setStatus)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Order::setCompletionDate)).setPostConverter(toEntityConverter());
    }

    public Converter<OrderDriverDto, Order> toEntityConverter() {
        return context -> {
            OrderDriverDto source = context.getSource();
            Order destination = context.getDestination();
            mapSpecificFieldsForEntity(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForEntity(OrderDriverDto source, Order destination) {
        destination.setCargoes(Objects.isNull(source) || Objects.isNull(source.getId()) ? null :
                orderDao.getListOfCargoesForOrder(source.getId()));
        if (source != null && source.getStatus() != null) {
            if (source.getStatus().equals(OrderStatus.IN_PROGRESS.toString())) {
                destination.setOrderTruck(truckDao.findByNumber(source.getTruckNumber()));
                destination.setStatus(OrderStatus.valueOf(source.getStatus()));
                destination.setCompletionDate(null);
            } else {
                finishOrder(source, destination);
            }
        }
    }

    private void finishOrder(OrderDriverDto source, Order destination) {
        List<Driver> listOfDriver = driverDao.findAllDriversForCurrentOrder(orderDao.findByNumber(source.getNumber()));
        for (Driver driver: listOfDriver) {
            driver.setCurrentOrder(null);
            driverDao.update(driver);
        }
        Truck truck = truckDao.findByNumber(source.getTruckNumber());
        truck.setOrder(null);
        truckDao.update(truck);
        destination.setOrderTruck(null);
        destination.setStatus(OrderStatus.valueOf(source.getStatus()));
        destination.setCompletionDate(new Date().getTime());
    }

    public Converter<Order, OrderDriverDto> toDtoConverter() {
        return context -> {
            Order source = context.getSource();
            OrderDriverDto destination = context.getDestination();
            mapSpecificFieldsForDto(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForDto(Order source, OrderDriverDto destination) {
        destination.setTruckNumber(Objects.isNull(source) || Objects.isNull(source.getOrderTruck())
                || Objects.isNull(source.getOrderTruck().getNumber()) ? null : source.getOrderTruck().getNumber());
        destination.setStatus(Objects.isNull(source) || Objects.isNull(source.getStatus()) ? null :
                source.getStatus().toString());
        destination.setWaypoints(Objects.isNull(source) || Objects.isNull(source.getId()) ||
                Objects.isNull(source.getCargoes()) ? null : getListOfWaypointsDto(source.getId()));
        destination.setDrivers(Objects.isNull(source) || Objects.isNull(source.getId()) ? null :
                getListOfDriverShortDtoForOrder(source));
    }

    private List<WaypointDto> getListOfWaypointsDto(Long orderId) {
        List<WaypointDto> waypointsDto = new ArrayList<>();
        List<Waypoint> waypoints = waypointDao.getListOfWaypointsByOrderId(orderId);
        for (Waypoint waypoint: waypoints) {
            waypointsDto.add(waypointMapper.toDto(waypoint));
        }
        return  waypointsDto;
    }

    private List<DriverShortDto> getListOfDriverShortDtoForOrder(Order order) {
        List<DriverShortDto> drivers = new ArrayList<>();
        List<Driver> listOfDrivers = driverDao.findAllDriversForCurrentOrder(order);
        for (Driver driver: listOfDrivers) {
            drivers.add(driverShortMapper.toDto(driver));
        }
        return drivers;
    }

}

