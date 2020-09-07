package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dao.*;
import com.tsystems.logisticsProject.dto.*;
import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Waypoint;
import com.tsystems.logisticsProject.entity.enums.Action;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class OrderAdminMapper {

    private ModelMapper modelMapper;
    private WaypointMapper waypointMapper;
    private TruckDao truckDao;
    private OrderDao orderDao;
    private WaypointDao waypointDao;
    private DriverDao driverDao;

    @Autowired
    public OrderAdminMapper(ModelMapper modelMapper, WaypointMapper waypointMapper, TruckDao truckDao, OrderDao orderDao,
                            WaypointDao waypointDao, DriverDao driverDao) {
        this.modelMapper = modelMapper;
        this.waypointMapper = waypointMapper;
        this.truckDao = truckDao;
        this.orderDao = orderDao;
        this.waypointDao = waypointDao;
        this.driverDao = driverDao;
    }

    public Order toEntity(OrderAdminDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Order.class);
    }

    public OrderAdminDto toDto(Order entity) {
        return Objects.isNull(entity) ? null : modelMapper.map(entity, OrderAdminDto.class);
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Order.class, OrderAdminDto.class)
                .addMappings(m -> m.skip(OrderAdminDto::setDrivers)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderAdminDto::setTruckNumber)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderAdminDto::setMaxWeight)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderAdminDto::setCargoes)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderAdminDto::setCityFrom)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderAdminDto::setCityTo)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(OrderAdminDto.class, Order.class)
                .addMappings(m -> m.skip(Order::setOrderTruck)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Order::setCargoes)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Order::setDrivers)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Order::setStatus)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Order::setCompletionDate)).setPostConverter(toEntityConverter());
    }

    public Converter<OrderAdminDto, Order> toEntityConverter() {
        return context -> {
            OrderAdminDto source = context.getSource();
            Order destination = context.getDestination();
            mapSpecificFieldsForEntity(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForEntity(OrderAdminDto source, Order destination) {
        destination.setOrderTruck(Objects.isNull(source) || Objects.isNull(source.getTruckNumber()) ? null :
                truckDao.findByNumber(source.getNumber()));
        destination.setStatus(Objects.isNull(source) || Objects.isNull(source.getId()) ? null :
                orderDao.findById(source.getId()).getStatus());
        destination.setCompletionDate(Objects.isNull(source) || Objects.isNull(source.getId()) ? null :
                orderDao.findById(source.getId()).getCompletionDate());
    }

    public Converter<Order, OrderAdminDto> toDtoConverter() {
        return context -> {
            Order source = context.getSource();
            OrderAdminDto destination = context.getDestination();
            mapSpecificFieldsForDto(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForDto(Order source, OrderAdminDto destination) {
        destination.setDrivers(Objects.isNull(source) || Objects.isNull(source.getId()) ? null :
                getDriversForOrderAdminDto(source));
        destination.setTruckNumber(Objects.isNull(source) || Objects.isNull(source.getOrderTruck())
                || Objects.isNull(source.getOrderTruck().getNumber()) ? null : source.getOrderTruck().getNumber());
        destination.setMaxWeight(Objects.isNull(source) || Objects.isNull(source.getCargoes())
                || Objects.isNull(source.getId()) ? null : getMaxWeightDuringTheRouteByOrderId(source.getId()));
        destination.setCargoes(Objects.isNull(source) || Objects.isNull(source.getCargoes()) ? null :
                getCargoesForOrderAdminDto(source.getCargoes()));
        destination.setCityFrom(Objects.isNull(source) || Objects.isNull(source.getCargoes()) ||
                Objects.isNull(source.getId()) ? null : getCityFrom(source.getId()));
        destination.setCityFrom(Objects.isNull(source) || Objects.isNull(source.getCargoes()) ||
                Objects.isNull(source.getId()) ? null : getCityTo(source.getId()));
        destination.setWaypoints(Objects.isNull(source) || Objects.isNull(source.getCargoes()) ||
                Objects.isNull(source.getId()) ? null : getWaypointsForOrderAdminDto(source.getId()));
    }

    private List<String> getDriversForOrderAdminDto(Order order) {
        List<String> driversDto = new ArrayList<>();
        List<Driver> drivers = driverDao.findAllDriversForCurrentOrder(order);
        for (Driver driver: drivers) {
            String driverDto = driver.getName() + " " + driver.getSurname();
            driversDto.add(driverDto);
        }
        return driversDto;
    }

    private double getMaxWeightDuringTheRouteByOrderId(Long id) {
        double maxWeight = 0;
        double totalWeight = 0;
        List<Waypoint> waypoints = waypointDao.getListOfWaypointsByOrderId(id);
        for (Waypoint waypoint : waypoints) {
            if (waypoint.getAction().equals(Action.LOADING)) {
                totalWeight += waypoint.getCargo().getWeight();
                if (maxWeight < totalWeight) {
                    maxWeight = totalWeight;
                }
            } else {
                totalWeight -= waypoint.getCargo().getWeight();
            }
        }
        return maxWeight;
    }

    private List<String> getCargoesForOrderAdminDto(List<Cargo> cargoes) {
        List<String> cargoesForOrderAdminDto = new ArrayList<>();
        for (Cargo cargo: cargoes) {
            String cargoDto = cargo.getName() + " " + cargo.getWeight().toString();
            cargoesForOrderAdminDto.add(cargoDto);
        }
        return cargoesForOrderAdminDto;
    }

    private String getCityFrom(Long orderId) {
        List<Waypoint> waypoints = waypointDao.getListOfWaypointsByOrderId(orderId);
        return waypoints.get(0).getCity().toString();
    }

    private String getCityTo(Long orderId) {
        List<Waypoint> waypoints = waypointDao.getListOfWaypointsByOrderId(orderId);
        return waypoints.get(waypoints.size() - 1).getCity().toString();
    }

    private List<WaypointDto> getWaypointsForOrderAdminDto(Long orderId) {
        List<WaypointDto> waypointsDto = new ArrayList<>();
        List<Waypoint> waypoints = waypointDao.getListOfWaypointsByOrderId(orderId);
        for (Waypoint waypoint: waypoints) {
            waypointsDto.add(waypointMapper.toDto(waypoint));
        }
        return waypointsDto;
    }

}
