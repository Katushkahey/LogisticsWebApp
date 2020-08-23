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
public class OrderMapper {

    private ModelMapper modelMapper;
    private DriverMapper driverMapper;
    private WaypointMapper waypointMapper;
    private TruckDao truckDao;
    private CargoDao cargoDao;
    private WaypointDao waypointDao;

    @Autowired
    public OrderMapper(ModelMapper modelMapper, DriverMapper driverMapper, TruckDao truckDao, CargoDao cargoDao,
                       WaypointDao waypointDao, WaypointMapper waypointMapper) {
        this.modelMapper = modelMapper;
        this.driverMapper = driverMapper;
        this.waypointMapper = waypointMapper;
        this.truckDao = truckDao;
        this.cargoDao = cargoDao;
        this.waypointDao = waypointDao;
    }

    public Order toEntity(OrderDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Order.class);
    }

    public OrderDto toDto(Order entity) {
        return Objects.isNull(entity) ? null : modelMapper.map(entity, OrderDto.class);
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Order.class, OrderDto.class)
                .addMappings(m -> m.skip(OrderDto::setWaypointsDto)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderDto::setDrivers)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderDto::setTruckNumber)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(OrderDto.class, Order.class)
                .addMappings(m -> m.skip(Order::setCargoes)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Order::setDrivers)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Order::setOrderTruck)).setPostConverter(toEntityConverter());
    }

    public Converter<OrderDto, Order> toEntityConverter() {
        return context -> {
            OrderDto source = context.getSource();
            Order destination = context.getDestination();
            mapSpecificFieldsForEntity(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForEntity(OrderDto source, Order destination) {
        if (source != null) {
            if (source.getTruckNumber() == null) {
                destination.setOrderTruck(null);
            } else {
                destination.setOrderTruck(truckDao.findByNumber(source.getTruckNumber()));
            }

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

            List<DriverDto> driversDto = source.getDrivers();
            List<Driver> drivers = new ArrayList<>();
            if (driversDto == null) {
                destination.setDrivers(null);
            } else {
                for (DriverDto driverDto : driversDto) {
                    drivers.add(driverMapper.toEntity(driverDto));
                }
                destination.setDrivers(drivers);
            }
        }
    }


    public Converter<Order, OrderDto> toDtoConverter() {
        return context -> {
            Order source = context.getSource();
            OrderDto destination = context.getDestination();
            mapSpecificFieldsForDto(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForDto(Order source, OrderDto destination) {
        if (source != null) {
            destination.setTruckNumber(Objects.isNull(source.getOrderTruck()) ? null :
                    source.getOrderTruck().getNumber());

            List<Driver> drivers = source.getDrivers();
            if (drivers == null) {
                destination.setDrivers(null);
            } else {
                List<DriverDto> driversDto = new ArrayList<>();
                for (Driver driver : drivers) {
                    driversDto.add(driverMapper.toDto(driver));
                }
                destination.setDrivers(driversDto);
            }

            List<Waypoint> listOWaypoint = waypointDao.getListOfWaypointsByOrderId(source.getId());
            if (source.getCargoes() == null || listOWaypoint == null) {
                destination.setWaypointsDto(null);
            } else {
                List<WaypointDto> listOfWaypointsDto = new ArrayList<>();
                for (Waypoint waypoint : listOWaypoint) {
                    listOfWaypointsDto.add(waypointMapper.toDto(waypoint));
                }
                destination.setWaypointsDto(listOfWaypointsDto);
            }
        }
    }
}
