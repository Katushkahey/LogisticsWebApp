package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dao.CityDao;
import com.tsystems.logisticsProject.dao.OrderDao;
import com.tsystems.logisticsProject.dao.TruckDao;
import com.tsystems.logisticsProject.dto.TruckDto;
import com.tsystems.logisticsProject.entity.Truck;
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

    @Autowired
    public TruckMapper(ModelMapper modelMapper, CityDao cityDao ,OrderDao orderDao) {
        this.modelMapper = modelMapper;
        this.cityDao = cityDao;
        this.orderDao = orderDao;
    }

    public Truck toEntity(TruckDao dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Truck.class);
    }

    public TruckDao toDto(Truck entity) {
        return Objects.isNull(entity) ? null : modelMapper.map(entity, TruckDao.class);
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Truck.class, TruckDto.class)
                .addMappings(m -> m.skip(TruckDto::setCurrentCity)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(TruckDto::setOrderNumber)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(TruckDto.class, Truck.class)
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
        if (source.getCurrentCity() == null) {
            destination.setCurrentCity(null);
        } else {
            destination.setCurrentCity(cityDao.findByName(source.getCurrentCity()));
        }

        if (source.getOrderNumber() == null) {
            destination.setOrder(null);
        } else {
            destination.setOrder(orderDao.findByNumber(source.getOrderNumber()));
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
        destination.setCurrentCity(Objects.isNull(source) || Objects.isNull(source.getId()) ? null :
                source.getCurrentCity().getName());
        destination.setOrderNumber(Objects.isNull(source) || Objects.isNull(source.getOrder()) ? null : source.getOrder().getNumber());
    }
}
