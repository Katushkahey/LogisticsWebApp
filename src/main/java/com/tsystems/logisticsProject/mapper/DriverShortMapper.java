package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dao.DriverDao;
import com.tsystems.logisticsProject.dao.OrderDao;
import com.tsystems.logisticsProject.dto.DriverShortDto;
import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.enums.DriverState;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class DriverShortMapper {

    private ModelMapper modelMapper;
    private DriverDao driverDao;
    private OrderDao orderDao;

    @Autowired
    public void setDependencies(ModelMapper modelMapper, DriverDao driverDao, OrderDao orderDao) {
        this.modelMapper = modelMapper;
        this.driverDao = driverDao;
        this.orderDao = orderDao;
    }

    public Driver toEntity(DriverShortDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Driver.class);
    }

    public DriverShortDto toDto(Driver entity) {
        return Objects.isNull(entity) ? null : modelMapper.map(entity, DriverShortDto.class);
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Driver.class, DriverShortDto.class)
                .addMappings(m -> m.skip(DriverShortDto::setState)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(DriverShortDto::setOrderNumber)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(DriverShortDto.class, Driver.class)
                .addMappings(m -> m.skip(Driver::setTelephoneNumber)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Driver::setDriverState)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Driver::setCurrentCity)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Driver::setCurrentOrder)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Driver::setUser)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Driver::setStartWorkingTime)).setPostConverter(toEntityConverter());
    }

    public Converter<DriverShortDto, Driver> toEntityConverter() {
        return context -> {
            DriverShortDto source = context.getSource();
            Driver destination = context.getDestination();
            mapSpecificFieldsForEntity(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForEntity(DriverShortDto source, Driver destination) {
       if (source != null && source.getId() != null) {
           Driver driver = driverDao.findById(source.getId());
           destination.setTelephoneNumber(driver.getTelephoneNumber());
           destination.setCurrentCity(driver.getCurrentCity());
           destination.setUser(driver.getUser());
           destination.setStartWorkingTime(driver.getStartWorkingTime());
       }
       destination.setDriverState(Objects.isNull(source) || Objects.isNull(source.getState()) ? null :
               DriverState.valueOf(source.getState()));
       destination.setCurrentOrder(Objects.isNull(source) || Objects.isNull(source.getOrderNumber()) ? null :
                orderDao.findByNumber(source.getOrderNumber()));
    }

    public Converter<Driver, DriverShortDto> toDtoConverter() {
        return context -> {
            Driver source = context.getSource();
            DriverShortDto destination = context.getDestination();
            mapSpecificFieldsForDto(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForDto(Driver source, DriverShortDto destination) {
        destination.setState(Objects.isNull(source) || Objects.isNull(source.getDriverState()) ? null :
                source.getDriverState().toString());
        destination.setOrderNumber(Objects.isNull(source) || Objects.isNull(source.getCurrentOrder()) ||
                Objects.isNull(source.getCurrentOrder().getNumber()) ? null : source.getCurrentOrder().getNumber());
    }

}
