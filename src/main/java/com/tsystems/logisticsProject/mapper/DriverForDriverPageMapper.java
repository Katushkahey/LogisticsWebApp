package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dao.CityDao;
import com.tsystems.logisticsProject.dao.DriverDao;
import com.tsystems.logisticsProject.dao.OrderDao;
import com.tsystems.logisticsProject.dto.DriverDtoForDriverPage;
import com.tsystems.logisticsProject.entity.Driver;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class DriverForDriverPageMapper {

    private ModelMapper modelMapper;
    private OrderForDriverPageMapper orderMapper;
    private CityDao cityDao;
    private OrderDao orderDao;
    private DriverDao driverDao;

    @Autowired
    public void setDependencies(ModelMapper modelMapper, OrderForDriverPageMapper orderMapper,
                                DriverDao driverDao, CityDao cityDao, OrderDao orderDao) {
        this.modelMapper = modelMapper;
        this.orderMapper = orderMapper;
        this.driverDao = driverDao;
        this.cityDao = cityDao;
        this.orderDao = orderDao;
    }

    public Driver toEntity(DriverDtoForDriverPage dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Driver.class);
    }

    public DriverDtoForDriverPage toDto(Driver entity) {
        return Objects.isNull(entity) ? null : modelMapper.map(entity, DriverDtoForDriverPage.class);
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Driver.class, DriverDtoForDriverPage.class)
                .addMappings(m -> m.skip(DriverDtoForDriverPage::setCityName)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(DriverDtoForDriverPage::setOrderDtoForDriverPage)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(DriverDtoForDriverPage::setPartners)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(DriverDtoForDriverPage.class, Driver.class)
                .addMappings(m -> m.skip(Driver::setCurrentCity))
                .addMappings(m -> m.skip(Driver::setCurrentOrder))
                .addMappings(m -> m.skip(Driver::setUser));
    }

//    public Converter<DriverDtoForDriverPage, Driver> toEntityConverter() {
//        return context -> {
//            DriverDtoForDriverPage source = context.getSource();
//            Driver destination = context.getDestination();
//            mapSpecificFieldsForEntity(source, destination);
//            return context.getDestination();
//        };
//    }

//    public void mapSpecificFieldsForEntity(DriverDtoForDriverPage source, Driver destination) {
//        if (source != null) {
//            if (source.getCityName() == null) {
//                destination.setCurrentCity(null);
//            }
//            destination.setCurrentCity(cityDao.findByName(source.getCityName()));
//            if (source.getOrderDtoForDriverPage() == null) {
//                destination.setCurrentOrder(null);
//            }
//            destination.setCurrentOrder(orderMapper.toEntity(source.getOrderDtoForDriverPage()));
//        }
//    }

    public Converter<Driver, DriverDtoForDriverPage> toDtoConverter() {
        return context -> {
            Driver source = context.getSource();
            DriverDtoForDriverPage destination = context.getDestination();
            mapSpecificFieldsForDto(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForDto(Driver source, DriverDtoForDriverPage destination) {
        destination.setCityName(Objects.isNull(source) || Objects.isNull(source.getId()) ? null :
                source.getCurrentCity().getName());
        destination.setOrderDtoForDriverPage(Objects.isNull(source) || Objects.isNull(source.getCurrentOrder()) ? null :
                orderMapper.toDto(orderDao.findByNumber(source.getCurrentOrder().getNumber())));
        if (source == null || source.getCurrentOrder() == null) {
            destination.setPartners(null);
        } else {
            List<Driver> drivers = driverDao.findAllDriversForCurrentOrder(source.getCurrentOrder());
            List<String> partners = new ArrayList<>();
            for(Driver driver: drivers) {
                if (!driver.equals(source)) {
                    String partnerInfo = driver.getName() + " " + driver.getSurname() + ", " + driver.getTelephoneNumber();
                    partners.add(partnerInfo);
                }
            }
            destination.setPartners(partners);
        }
    }

}
