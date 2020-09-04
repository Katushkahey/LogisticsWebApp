package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dao.WaypointDao;
import com.tsystems.logisticsProject.dto.OrderClientDto;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Waypoint;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

@Component
public class OrderClientMapper {

    private ModelMapper mapper;
    private WaypointDao waypointDao;

    @Autowired
    public OrderClientMapper(ModelMapper mapper, WaypointDao waypointDao) {
        this.mapper = mapper;
        this.waypointDao = waypointDao;
    }

    public Order toEntity(OrderClientDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Order.class);
    }

    public OrderClientDto toDto(Order entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, OrderClientDto.class);
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Order.class, OrderClientDto.class)
                .addMappings(m -> m.skip(OrderClientDto::setStartedCity)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderClientDto::setFinishedCity)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderClientDto::setStatus)).setPostConverter(toDtoConverter());
    }


    public Converter<Order, OrderClientDto> toDtoConverter() {
        return context -> {
            Order source = context.getSource();
            OrderClientDto destination = context.getDestination();
            mapSpecificFieldsForDto(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForDto(Order source, OrderClientDto destination) {
        if (source != null && source.getId() != null) {
            List<Waypoint> listOWaypoint = waypointDao.getListOfWaypointsByOrderId(source.getId());
            if (source.getCargoes() == null || listOWaypoint == null) {
                destination.setStartedCity(null);
                destination.setFinishedCity(null);
            } else {
                destination.setStartedCity(listOWaypoint.get(0).getCity().getName());
                destination.setFinishedCity(listOWaypoint.get(listOWaypoint.size() - 1).getCity().getName());
            }
            if (source.getStatus() != null) {
                destination.setStatus(source.getStatus().toString());
            }
        }
    }

}
