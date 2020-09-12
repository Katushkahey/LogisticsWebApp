package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dao.DriverDao;
import com.tsystems.logisticsProject.dao.OrderDao;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class DriverMapper {

    private ModelMapper modelMapper;
    private DriverDao driverDao;
    private OrderDao orderDao;

    @Autowired
    public void setDependencies(ModelMapper modelMapper, OrderDao orderDao, DriverDao driverDao) {
        this.modelMapper = modelMapper;
        this.orderDao = orderDao;
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
                .addMappings(m -> m.skip(DriverDto::setOrderNumber)).setPostConverter(toDtoConverter());
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
        destination.setCurrentCity(Objects.isNull(source) || Objects.isNull(source.getId()) ? null :
                driverDao.findById(source.getId()).getCurrentCity());
        destination.setCurrentOrder(Objects.isNull(source) || Objects.isNull(source.getOrderNumber()) ? null :
                orderDao.findByNumber(source.getOrderNumber()));
        destination.setUser(Objects.isNull(source) || Objects.isNull(source.getId()) ? null :
                driverDao.findById(source.getId()).getUser());
        if (source != null && source.getId() != null && source.getDriverState() != null) {
            editDriverState(source, destination);
        }
    }

    private void editDriverState(DriverDto driverDto, Driver destination) {
        DriverDto lastInstance = toDto(driverDao.findById(driverDto.getId()));
        String lastState = lastInstance.getDriverState();
        String currentState = driverDto.getDriverState();
        if (lastState.equals(currentState)) {
            copyFieldsFromLastInstance(lastInstance, destination);
        } else {
            changeDriverState(lastState, currentState, lastInstance, destination);
        }
    }

    private void changeDriverState(String lastState, String currentState, DriverDto lastInstance, Driver destination) {
        if ((currentState.equals(DriverState.REST.toString())) || currentState.equals(DriverState.SECOND_DRIVER.toString())) {
            if (lastState.equals(DriverState.DRIVING.toString()) || lastState.equals(DriverState.LOADING_UNLOADING.toString())) {
                Date endWorkingTime = new Date();
                Date startWorkingTime = new Date(lastInstance.getStartWorkingTime());
                Long totalWorkingTimePerInterval = endWorkingTime.getTime() - startWorkingTime.getTime();
                int totalWorkingHoursPerInterval = (int) Math.ceil(totalWorkingTimePerInterval / 1000 / 60 / 60);
                destination.setHoursThisMonth(lastInstance.getHoursThisMonth() + totalWorkingHoursPerInterval);
                destination.setDriverState(DriverState.valueOf(currentState));
            }
            destination.setDriverState(DriverState.valueOf(currentState));
        } else {
            if (lastState.equals(DriverState.REST.toString()) || lastState.equals(DriverState.SECOND_DRIVER.toString())) {
                Date startWorkingTime = new Date();
                destination.setStartWorkingTime(startWorkingTime.getTime());
                destination.setDriverState(DriverState.valueOf(currentState));
            }
            destination.setDriverState(DriverState.valueOf(currentState));
        }
    }

    private void copyFieldsFromLastInstance(DriverDto lastInstance, Driver destination) {
        destination.setDriverState(DriverState.valueOf(lastInstance.getDriverState()));
        destination.setHoursThisMonth(lastInstance.getHoursThisMonth());
        destination.setStartWorkingTime(lastInstance.getStartWorkingTime());
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
        destination.setOrderNumber(Objects.isNull(source) || Objects.isNull(source.getCurrentOrder()) ? null :
                source.getCurrentOrder().getNumber());
    }

    private List<String> getPartners(Order order, Long id) {
        List<String> partners = new ArrayList<>();
        List<Driver> drivers = driverDao.findAllDriversForCurrentOrder(order);
        if (drivers != null) {
            for (Driver driver: drivers) {
                if(driver.getId() != id) {
                    String driverDto = driver.getName() + " " + driver.getSurname() + ", " + driver.getTelephoneNumber();
                    partners.add(driverDto);
                }
            }
            return partners;
        } else {
            return null;
        }
    }

}
