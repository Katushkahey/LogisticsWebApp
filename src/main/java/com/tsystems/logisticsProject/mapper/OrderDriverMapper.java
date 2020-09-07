package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dao.*;
import com.tsystems.logisticsProject.dto.OrderDriverDto;
import com.tsystems.logisticsProject.dto.WaypointDto;
import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Waypoint;
import com.tsystems.logisticsProject.entity.enums.Action;
import com.tsystems.logisticsProject.entity.enums.OrderStatus;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class OrderDriverMapper {

    private ModelMapper modelMapper;
    private WaypointMapper waypointMapper;
    private OrderDao orderDao;
    private WaypointDao waypointDao;
    private TruckDao truckDao;

    @Autowired
    public void setDependencies(ModelMapper modelMapper, WaypointMapper waypointMapper, OrderDao orderDao,
                               WaypointDao waypointDao, TruckDao truckDao) {
        this.modelMapper = modelMapper;
        this.waypointMapper = waypointMapper;
        this. orderDao = orderDao;
        this.waypointDao = waypointDao;
        this.truckDao = truckDao;
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
        destination.setOrderTruck(Objects.isNull(source) || Objects.isNull(source.getTruckNumber()) ? null :
                truckDao.findByNumber(source.getTruckNumber()));
        destination.setStatus(Objects.isNull(source) || Objects.isNull(source.getStatus()) ? null :
                OrderStatus.valueOf(source.getStatus()));
        destination.setCompletionDate(Objects.isNull(source) || Objects.isNull(source.getNumber())? null :
                orderDao.findByNumber(source.getNumber()).getCompletionDate());
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
    }

    private List<WaypointDto> getListOfWaypointsDto(Long orderId) {
        List<WaypointDto> waypointsDto = new ArrayList<>();
        List<Waypoint> waypoints = waypointDao.getListOfWaypointsByOrderId(orderId);
        for (Waypoint waypoint: waypoints) {
            waypointsDto.add(waypointMapper.toDto(waypoint));
        }
        return  waypointsDto;
    }

}

