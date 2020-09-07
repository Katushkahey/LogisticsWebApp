package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dao.CityDao;
import com.tsystems.logisticsProject.dao.DriverDao;
import com.tsystems.logisticsProject.dao.UserDao;
import com.tsystems.logisticsProject.dto.DriverAdminDto;
import com.tsystems.logisticsProject.entity.Driver;
import com.tsystems.logisticsProject.entity.enums.DriverState;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class DriverAdminMapper {

    private ModelMapper modelMapper;
    private DriverDao driverDao;
    private CityDao cityDao;
    private UserDao userDao;

    @Autowired
    public void setDependencies(ModelMapper modelMapper, DriverDao driverDao, CityDao cityDao, UserDao userDao) {
        this.modelMapper = modelMapper;
        this.driverDao = driverDao;
        this.cityDao = cityDao;
        this.userDao = userDao;
    }

    public Driver toEntity(DriverAdminDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Driver.class);
    }

    public DriverAdminDto toDto(Driver entity) {
        return Objects.isNull(entity) ? null : modelMapper.map(entity, DriverAdminDto.class);
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Driver.class, DriverAdminDto.class)
                .addMappings(m -> m.skip(DriverAdminDto::setCityName)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(DriverAdminDto::setAvailable)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(DriverAdminDto::setUserName)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(DriverAdminDto.class, Driver.class)
                .addMappings(m -> m.skip(Driver::setDriverState)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Driver::setCurrentCity)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Driver::setCurrentOrder)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Driver::setUser)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Driver::setStartWorkingTime)).setPostConverter(toEntityConverter());
    }

    public Converter<DriverAdminDto, Driver> toEntityConverter() {
        return context -> {
            DriverAdminDto source = context.getSource();
            Driver destination = context.getDestination();
            mapSpecificFieldsForEntity(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForEntity(DriverAdminDto source, Driver destination) {
        destination.setDriverState(Objects.isNull(source) || Objects.isNull(source.getId()) ? DriverState.REST :
                driverDao.findById(source.getId()).getDriverState());
        destination.setCurrentCity(Objects.isNull(source) || Objects.isNull(source.getCityName()) ? null :
                cityDao.findByName(source.getCityName()));
        destination.setCurrentOrder(Objects.isNull(source) || Objects.isNull(source.getId()) ? null :
                driverDao.findById(source.getId()).getCurrentOrder());
        destination.setUser(Objects.isNull(source) || Objects.isNull(source.getUserName()) ? null :
                userDao.findByUsername(source.getUserName()));
        destination.setStartWorkingTime(Objects.isNull(source) || Objects.isNull(source.getId()) ? null :
                driverDao.findById(source.getId()).getStartWorkingTime());
    }

    public Converter<Driver, DriverAdminDto> toDtoConverter() {
        return context -> {
            Driver source = context.getSource();
            DriverAdminDto destination = context.getDestination();
            mapSpecificFieldsForDto(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForDto(Driver source, DriverAdminDto destination) {
        destination.setCityName(Objects.isNull(source) || Objects.isNull(source.getCurrentCity()) ? null :
                source.getCurrentCity().getName());
        destination.setAvailable(Objects.isNull(source) ? null : (Objects.isNull(source.getCurrentOrder())));
        destination.setUserName(Objects.isNull(source) || Objects.isNull(source.getUser()) ||
                Objects.isNull(source.getUser().getUsername()) ? null : source.getUser().getUsername());
    }

}

