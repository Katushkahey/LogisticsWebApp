package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dto.CargoDto;
import com.tsystems.logisticsProject.dto.NewOrderDto;
import com.tsystems.logisticsProject.dto.NewOrderWaypointDto;
import com.tsystems.logisticsProject.dto.WaypointDto;
import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Waypoint;
import com.tsystems.logisticsProject.entity.enums.OrderStatus;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class NewOrderMapper {

    private ModelMapper mapper;
    private CargoMapper cargoMapper;
    private NewOrderWaypointMapper newOrderWaypointMapper;

    @Autowired
    public NewOrderMapper(ModelMapper mapper, CargoMapper cargoMapper, NewOrderWaypointMapper newOrderWaypointMapper) {
        this.mapper = mapper;
        this.cargoMapper = cargoMapper;
        this.newOrderWaypointMapper = newOrderWaypointMapper;
    }

    public Order toEntity(NewOrderDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Order.class);
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(NewOrderDto.class, Order.class)
                .addMappings(m -> m.skip(Order::setCargoes)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Order::setDrivers)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Order::setStatus)).setPostConverter(toEntityConverter());
    }


    public Converter<NewOrderDto, Order> toEntityConverter() {
        return context -> {
            NewOrderDto source = context.getSource();
            Order destination = context.getDestination();
            mapSpecificFieldsForDto(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForDto(NewOrderDto source, Order destination) {
        List<Cargo> cargoes = new ArrayList<>();
        List<Waypoint> waypoints = new ArrayList<>();
        for (CargoDto cargoDto : source.getCargoes()) {
            cargoes.add(cargoMapper.toEntity(cargoDto));
        }

        for (NewOrderWaypointDto waypointDto : source.getWaypoints()) {
            waypoints.add(newOrderWaypointMapper.toEntity(waypointDto));
        }

        for (Cargo cargo : cargoes) {
            List<Long> waypointsId = new ArrayList<>();
            for (NewOrderWaypointDto waypointDto : source.getWaypoints()) {
                if (waypointDto.getCargoNumber().equals(cargo.getNumber())) {
                    waypointsId.add(waypointDto.getId());
                }
            }
            List<Waypoint> listOfCargoWaypoints = new ArrayList<>();
            for (Long id : waypointsId) {
                for (Waypoint waypoint : waypoints) {
                    if (waypoint.getSequence() == id) {
                        waypoint.setCargo(cargo);
                        listOfCargoWaypoints.add(waypoint);
                    }
                }
            }
            cargo.setWaypoints(listOfCargoWaypoints);
            cargo.setOrder(destination);
            String number = UUID.randomUUID().toString();
            destination.setNumber(number);
            destination.setStatus(OrderStatus.NOT_ASSIGNED);
            destination.setCargoes(cargoes);
        }

    }
}
