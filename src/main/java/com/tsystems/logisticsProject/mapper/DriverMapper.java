package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dao.DriverDao;
import com.tsystems.logisticsProject.dto.DriverDto;
import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.enums.DriverState;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class DriverMapper {

    private ModelMapper modelMapper;
    private OrderDriverMapper orderDriverMapper;
    private DriverDao driverDao;

    @Autowired
    public void setDependencies(ModelMapper modelMapper, OrderDriverMapper orderDriverMapper, DriverDao driverDao) {
        this.modelMapper = modelMapper;
        this.orderDriverMapper = orderDriverMapper;
        this.driverDao = driverDao;
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
                .addMappings(m -> m.skip(DriverDto::setPartners)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(DriverDto::setDriverState)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(DriverDto::setOrder)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(DriverDto.class, Driver.class)
                .addMappings(m -> m.skip(Driver::setDriverState)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Driver::setCurrentCity)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Driver::setCurrentOrder)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Driver::setUser)).setPostConverter(toEntityConverter());
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
        destination.setDriverState(Objects.isNull(source) || Objects.isNull(source.getDriverState()) ? null :
                DriverState.valueOf(source.getDriverState()));
        destination.setCurrentCity(Objects.isNull(source) || Objects.isNull(source.getId()) ? null :
                driverDao.findById(source.getId()).getCurrentCity());
        destination.setCurrentOrder(Objects.isNull(source) || Objects.isNull(source.getOrder()) ? null :
                orderDriverMapper.toEntity(source.getOrder()));
        destination.setUser(Objects.isNull(source) || Objects.isNull(source.getId()) ? null :
                driverDao.findById(source.getId()).getUser());
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
        destination.setPartners(Objects.isNull(source) || Objects.isNull(source.getCurrentOrder())
                || Objects.isNull(source.getId()) ? null : getPartners(source.getCurrentOrder(), source.getId()));
        destination.setDriverState(Objects.isNull(source) || Objects.isNull(source.getDriverState()) ? null :
                source.getDriverState().toString());
        destination.setOrder(Objects.isNull(source) || Objects.isNull(source.getCurrentCity()) ? null :
                orderDriverMapper.toDto(source.getCurrentOrder()));
    }

    private List<String> getPartners(Order order, Long id) {
        List<String> partners = new ArrayList<>();
        List<Driver> drivers = driverDao.findAllDriversForCurrentOrder(order);
        for (Driver driver: drivers) {
            if(driver.getId() != id) {
                String driverDto = driver.getName() + " " + driver.getSurname() + ", " + driver.getTelephoneNumber();
                partners.add(driverDto);
            }
        }
        return partners;
    }

}
