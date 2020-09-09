package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dao.CityDao;
import com.tsystems.logisticsProject.dao.OrderDao;
import com.tsystems.logisticsProject.dao.TruckDao;
import com.tsystems.logisticsProject.dto.TruckDto;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.entity.enums.TruckState;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class TruckMapper {

    private ModelMapper modelMapper;
    private CityDao cityDao;
    private OrderDao orderDao;
    private TruckDao truckDao;

    @Autowired
    public TruckMapper(ModelMapper modelMapper, CityDao cityDao, OrderDao orderDao, TruckDao truckDao) {
        this.modelMapper = modelMapper;
        this.cityDao = cityDao;
        this.orderDao = orderDao;
        this.truckDao = truckDao;
    }

    public Truck toEntity(TruckDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Truck.class);
    }

    public TruckDto toDto(Truck entity) {
        return Objects.isNull(entity) ? null : modelMapper.map(entity, TruckDto.class);
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Truck.class, TruckDto.class)
                .addMappings(m -> m.skip(TruckDto::setState)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(TruckDto::setAvailable)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(TruckDto::setCityName)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(TruckDto.class, Truck.class)
                .addMappings(m -> m.skip(Truck::setTruckState)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Truck::setCurrentCity)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Truck::setOrder)).setPostConverter(toEntityConverter());
    }

    public Converter<TruckDto, Truck> toEntityConverter() {
        return context -> {
            TruckDto source = context.getSource();
            Truck destination = context.getDestination();
            mapSpecificFieldsForEntity(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForEntity(TruckDto source, Truck destination) {
        destination.setTruckState(Objects.isNull(source) || Objects.isNull(source.getState()) ? null :
                TruckState.valueOf(source.getState()));
        destination.setCurrentCity(Objects.isNull(source) || Objects.isNull(source.getCityName()) ? null :
                cityDao.findByName(source.getCityName()));
        if (source.isAvailable() && source.getId() != null) {
            destination.setOrder(null);
        } else {
            Truck truck = truckDao.findById(source.getId());
            Order order = orderDao.findByTruck(truck);
            if (truck == null || order == null) {
                destination.setOrder(null);
            }
            destination.setOrder(order);
        }
    }

    public Converter<Truck, TruckDto> toDtoConverter() {
        return context -> {
            Truck source = context.getSource();
            TruckDto destination = context.getDestination();
            mapSpecificFieldsForDto(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForDto(Truck source, TruckDto destination) {
        destination.setState(Objects.isNull(source) || Objects.isNull(source.getTruckState()) ? null :
                source.getTruckState().toString());
        destination.setAvailable(Objects.isNull(source) ? null : (Objects.isNull(source.getOrder())));
        destination.setCityName(Objects.isNull(source) || Objects.isNull(source.getCurrentCity()) ? null :
                source.getCurrentCity().getName());
    }
}
