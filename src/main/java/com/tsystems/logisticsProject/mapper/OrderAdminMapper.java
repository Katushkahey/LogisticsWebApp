package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dao.*;
import com.tsystems.logisticsProject.dto.*;
import com.tsystems.logisticsProject.entity.*;
import com.tsystems.logisticsProject.entity.enums.OrderStatus;
import com.tsystems.logisticsProject.service.OrderService;
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
    private DriverShortMapper driverShortMapper;
    private OrderService orderService;
    private TruckDao truckDao;
    private OrderDao orderDao;
    private WaypointDao waypointDao;
    private DriverDao driverDao;

    @Autowired
    public OrderAdminMapper(ModelMapper modelMapper, WaypointMapper waypointMapper, TruckDao truckDao, OrderDao orderDao,
                            WaypointDao waypointDao, DriverDao driverDao, OrderService orderService,
                            DriverShortMapper driverShortMapper) {
        this.modelMapper = modelMapper;
        this.waypointMapper = waypointMapper;
        this.driverShortMapper = driverShortMapper;
        this.truckDao = truckDao;
        this.orderDao = orderDao;
        this.waypointDao = waypointDao;
        this.driverDao = driverDao;
        this.orderService = orderService;
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
                .addMappings(m -> m.skip(OrderAdminDto::setStatus)).setPostConverter(toDtoConverter())
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
        destination.setDrivers(Objects.isNull(source) || Objects.isNull(source.getNumber()) ? null
                : setDriversForOrder(source.getDrivers(), source.getNumber()));
        destination.setOrderTruck(Objects.isNull(source) || Objects.isNull(source.getId())
                ? null : setTruckForOrder(source.getTruckNumber(), source.getId()));
        destination.setStatus(Objects.isNull(source) || Objects.isNull(source.getId()) ? null :
                OrderStatus.valueOf(source.getStatus()));
        destination.setCompletionDate(Objects.isNull(source) || Objects.isNull(source.getId()) ? null :
                orderDao.findById(source.getId()).getCompletionDate());
    }

    private List<Driver> setDriversForOrder(List<DriverShortDto> listOfDrivers, String orderNumber) {
        if (listOfDrivers == null) {
            return cancelAssignmentForDrivers(orderNumber);
        } else {
            return assignDriversForOrder(listOfDrivers, orderNumber);
        }
    }

    private List<Driver> cancelAssignmentForDrivers(String orderNumber) {
        Order order = orderDao.findByNumber(orderNumber);
        List<Driver> drivers = driverDao.findAllDriversForCurrentOrder(order);
        if (drivers != null) {
            for (Driver driver : drivers) {
                driver.setCurrentOrder(null);
                driverDao.update(driver);
            }
        }
        return null;
    }

    private List<Driver> assignDriversForOrder(List<DriverShortDto> drivers, String orderNumber) {
        List<Driver> listOfDrivers = new ArrayList<>();
        for (DriverShortDto driverDto : drivers) {
            driverDto.setOrderNumber(orderNumber);
            Driver driver = driverShortMapper.toEntity(driverDto);
            driverDao.update(driver);
            listOfDrivers.add(driver);
        }
        return listOfDrivers;
    }

    private Truck setTruckForOrder(String truckNumber, Long id) {
        if (truckNumber == null) {
            return cancelAssignmentForTruck(id);
        } else {
            return assignTruckForOrder(truckNumber, id);
        }
    }

    private Truck cancelAssignmentForTruck(Long id) {
        Truck truck = orderDao.findTruckOfOrder(id);
        truck.setOrder(null);
        truckDao.update(truck);
        return null;
    }

    private Truck assignTruckForOrder(String truckNumber, Long id) {
        Truck truck = truckDao.findByNumber(truckNumber);
        truck.setOrder(orderDao.findById(id));
        truckDao.update(truck);
        return truck;
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
        destination.setDrivers(Objects.isNull(source) || Objects.isNull(source.getId()) ||
                Objects.isNull(driverDao.findAllDriversForCurrentOrder(source)) ? null : getDriversForOrderAdminDto(source));
        destination.setTruckNumber(Objects.isNull(source) || Objects.isNull(source.getOrderTruck())
                || Objects.isNull(source.getOrderTruck().getNumber()) ? null : source.getOrderTruck().getNumber());
        destination.setMaxWeight(Objects.isNull(source) || Objects.isNull(source.getCargoes())
                || Objects.isNull(source.getId()) ? null : getMaxWeightDuringTheRouteByOrderId(source.getId()));
        destination.setCargoes(Objects.isNull(source) || Objects.isNull(source.getCargoes()) ? null :
                getCargoesForOrderAdminDto(source.getCargoes()));
        destination.setCityFrom(Objects.isNull(source) || Objects.isNull(source.getCargoes()) ||
                Objects.isNull(source.getId()) ? null : getCityFrom(source.getId()));
        destination.setCityTo(Objects.isNull(source) || Objects.isNull(source.getCargoes()) ||
                Objects.isNull(source.getId()) ? null : getCityTo(source.getId()));
        destination.setWaypoints(Objects.isNull(source) || Objects.isNull(source.getCargoes()) ||
                Objects.isNull(source.getId()) ? null : getWaypointsForOrderAdminDto(source.getId()));
        destination.setStatus(Objects.isNull(source) || Objects.isNull(source.getStatus()) ? null
                : source.getStatus().toString());
    }

    private List<DriverShortDto> getDriversForOrderAdminDto(Order order) {
        List<DriverShortDto> driversDto = new ArrayList<>();
        List<Driver> drivers = driverDao.findAllDriversForCurrentOrder(order);
        for (Driver driver : drivers) {
            driversDto.add(driverShortMapper.toDto(driver));
        }
        return driversDto;
    }

    private double getMaxWeightDuringTheRouteByOrderId(Long id) {
        List<Waypoint> waypoints = waypointDao.getListOfWaypointsByOrderId(id);
        return orderService.getMaxWeightForOrderById(waypoints);
    }

    private List<String> getCargoesForOrderAdminDto(List<Cargo> cargoes) {
        List<String> cargoesForOrderAdminDto = new ArrayList<>();
        for (Cargo cargo : cargoes) {
            String cargoDto = cargo.getName() + " " + cargo.getWeight().toString();
            cargoesForOrderAdminDto.add(cargoDto);
        }
        return cargoesForOrderAdminDto;
    }

    private String getCityFrom(Long orderId) {
        List<Waypoint> waypoints = waypointDao.getListOfWaypointsByOrderId(orderId);
        return waypoints.get(0).getCity().getName();
    }

    private String getCityTo(Long orderId) {
        List<Waypoint> waypoints = waypointDao.getListOfWaypointsByOrderId(orderId);
        return waypoints.get(waypoints.size() - 1).getCity().getName();
    }

    private List<WaypointDto> getWaypointsForOrderAdminDto(Long orderId) {
        List<WaypointDto> waypointsDto = new ArrayList<>();
        List<Waypoint> waypoints = waypointDao.getListOfWaypointsByOrderId(orderId);
        for (Waypoint waypoint : waypoints) {
            waypointsDto.add(waypointMapper.toDto(waypoint));
        }
        return waypointsDto;
    }

}
