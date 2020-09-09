package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dao.TruckDao;
import com.tsystems.logisticsProject.dto.CombinationForOrderDto;
import com.tsystems.logisticsProject.dto.DriverShortDto;
import com.tsystems.logisticsProject.entity.CombinationForOrder;
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
public class CombinationForOrderMapper {

    private ModelMapper modelMapper;
    private DriverShortMapper driverShortMapper;
    private TruckDao truckDao;

    @Autowired
    public void setDependencies(ModelMapper modelMapper, DriverShortMapper driverShortMapper, TruckDao truckDao) {
        this.modelMapper = modelMapper;
        this.driverShortMapper = driverShortMapper;
        this.truckDao = truckDao;
    }

    public CombinationForOrder toEntity(CombinationForOrderDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, CombinationForOrder.class);
    }

    public CombinationForOrderDto toDto(CombinationForOrder entity) {
        return Objects.isNull(entity) ? null : modelMapper.map(entity, CombinationForOrderDto.class);
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(CombinationForOrder.class, CombinationForOrderDto.class)
                .addMappings(m -> m.skip(CombinationForOrderDto::setDrivers)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(CombinationForOrderDto::setTruckNumber)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(CombinationForOrderDto.class, CombinationForOrder.class)
                .addMappings(m -> m.skip(CombinationForOrder::setListOfDrivers)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(CombinationForOrder::setTruck)).setPostConverter(toEntityConverter());

    }

    public Converter<CombinationForOrderDto, CombinationForOrder> toEntityConverter() {
        return context -> {
            CombinationForOrderDto source = context.getSource();
            CombinationForOrder destination = context.getDestination();
            mapSpecificFieldsForEntity(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForEntity(CombinationForOrderDto source, CombinationForOrder destination) {
        destination.setTruck(Objects.isNull(source) || Objects.isNull(source.getTruckNumber()) ? null :
                truckDao.findByNumber(source.getTruckNumber()));
        destination.setListOfDrivers(Objects.isNull(source) || Objects.isNull(source.getDrivers()) ? null :
                convertListODriversDtoListOfDrivers(source.getDrivers()));
    }


    public Converter<CombinationForOrder, CombinationForOrderDto> toDtoConverter() {
        return context -> {
            CombinationForOrder source = context.getSource();
            CombinationForOrderDto destination = context.getDestination();
            mapSpecificFieldsForDto(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForDto(CombinationForOrder source, CombinationForOrderDto destination) {
        destination.setDrivers(Objects.isNull(source) || Objects.isNull(source.getListOfDrivers()) ? null :
                getDriversForCombinationDto(source.getListOfDrivers()));
        destination.setTruckNumber(Objects.isNull(source) || Objects.isNull(source.getTruck())
                || Objects.isNull(source.getTruck().getNumber()) ? null : source.getTruck().getNumber());
    }

    private List<DriverShortDto> getDriversForCombinationDto(List<Driver> drivers) {
        List<DriverShortDto> driversDto = new ArrayList<>();
        for (Driver driver: drivers) {
            driversDto.add(driverShortMapper.toDto(driver));
        }
        return driversDto;
    }

    private List<Driver> convertListODriversDtoListOfDrivers(List<DriverShortDto> driversDto) {
        List<Driver> listOfDrivers = new ArrayList<>();
        for (DriverShortDto driverDto : driversDto) {
            listOfDrivers.add(driverShortMapper.toEntity(driverDto));
        }
        return listOfDrivers;
    }

}
