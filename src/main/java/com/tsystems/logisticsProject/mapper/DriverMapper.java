package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dao.CityDao;
import com.tsystems.logisticsProject.dao.OrderDao;
import com.tsystems.logisticsProject.dto.DriverDto;
import com.tsystems.logisticsProject.entity.Driver;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class DriverMapper {

    private ModelMapper modelMapper;
    private CityDao cityDao;
    private OrderDao orderDao;

    @Autowired
    public void setDependencies(ModelMapper modelMapper, CityDao cityDao, OrderDao orderDao) {
        this.modelMapper = modelMapper;
        this.cityDao = cityDao;
        this.orderDao = orderDao;
    }

    public Driver toEntity(DriverDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Driver.class);
    }

    public DriverDto toDto(Driver entity) {
        return Objects.isNull(entity) ? null : modelMapper.map(entity, DriverDto.class);
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Driver.class, DriverDto.class)
                .addMappings(m -> m.skip(DriverDto::setCityName)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(DriverDto::setOrderNumber)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(DriverDto::setUserName));
        modelMapper.createTypeMap(DriverDto.class, Driver.class)
                .addMappings(m -> m.skip(Driver::setCurrentCity)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Driver::setCurrentOrder)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Driver::setUser));
    }

    public Converter<DriverDto, Driver> toEntityConverter() {
        return context -> {
            DriverDto source = context.getSource();
            Driver destination = context.getDestination();
            mapSpecificFieldsForEntity(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForEntity(DriverDto source, Driver destination) {
        if (source != null) {
            if (source.getCityName() == null) {
                destination.setCurrentCity(null);
            }
            destination.setCurrentCity(cityDao.findByName(source.getCityName()));
            if (source.getOrderNumber() == null) {
                destination.setCurrentOrder(null);
            }
            destination.setCurrentOrder(orderDao.findByNumber(source.getOrderNumber()));
        }
    }

    public Converter<Driver, DriverDto> toDtoConverter() {
        return context -> {
            Driver source = context.getSource();
            DriverDto destination = context.getDestination();
            mapSpecificFieldsForDto(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForDto(Driver source, DriverDto destination) {
        destination.setCityName(Objects.isNull(source) || Objects.isNull(source.getId()) ? null :
                source.getCurrentCity().getName());
        destination.setOrderNumber(Objects.isNull(source) || Objects.isNull(source.getCurrentOrder()) ||
                Objects.isNull(source.getCurrentOrder().getNumber()) ? null : source.getCurrentOrder().getNumber());

    }

}

