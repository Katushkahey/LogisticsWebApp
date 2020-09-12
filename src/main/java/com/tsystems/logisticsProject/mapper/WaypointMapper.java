package com.tsystems.logisticsProject.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.logisticsProject.dao.CargoDao;
import com.tsystems.logisticsProject.dao.CityDao;
import com.tsystems.logisticsProject.dto.WaypointDto;
import com.tsystems.logisticsProject.entity.Cargo;
import com.tsystems.logisticsProject.entity.Waypoint;
import com.tsystems.logisticsProject.entity.enums.Action;
import com.tsystems.logisticsProject.entity.enums.WaypointStatus;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class WaypointMapper {

    private ModelMapper modelMapper;
    private CargoDao cargoDao;
    private CityDao cityDao;

    @Autowired
    public WaypointMapper(ModelMapper modelMapper, CargoDao cargoDao, CityDao cityDao) {
        this.modelMapper = modelMapper;
        this.cargoDao = cargoDao;
        this.cityDao = cityDao;
    }

    public Waypoint toEntity(WaypointDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Waypoint.class);
    }

    public WaypointDto toDto(Waypoint entity) {
        return Objects.isNull(entity) ? null : modelMapper.map(entity, WaypointDto.class);
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Waypoint.class, WaypointDto.class)
                .addMappings(m -> m.skip(WaypointDto::setCargoName)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(WaypointDto::setCargoWeight)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(WaypointDto::setCargoNumber)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(WaypointDto::setCityName)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(WaypointDto::setAction)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(WaypointDto::setStatus)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(WaypointDto.class, Waypoint.class)
                .addMappings(m -> m.skip(Waypoint::setCargo)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Waypoint::setCity)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Waypoint::setAction)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Waypoint::setStatus)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Waypoint::setStatus)).setPostConverter(toEntityConverter());
    }

    public Converter<WaypointDto, Waypoint> toEntityConverter() {
        return context -> {
            WaypointDto source = context.getSource();
            Waypoint destination = context.getDestination();
            mapSpecificFieldsForEntity(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForEntity(WaypointDto source, Waypoint destination) {
        destination.setCargo(Objects.isNull(source) || Objects.isNull(source.getCargoNumber())
                ? null : updateCargo(source.getCargoNumber(), source.getCargoWeight(), source.getCargoName()));
        destination.setCity(Objects.isNull(source) || Objects.isNull(source.getCityName())
                ? null : cityDao.findByName(source.getCityName()));
        destination.setAction(Objects.isNull(source) || Objects.isNull(source.getAction())
                ? null : Action.valueOf(source.getAction()));
        destination.setStatus(Objects.isNull(source) || Objects.isNull(source.getStatus())
                ? null : WaypointStatus.valueOf(source.getStatus()));
    }

    private Cargo updateCargo(String cargoNumber, Double cargoWeight, String cargoName) {
        Cargo cargo = cargoDao.findByNumber(cargoNumber);
        cargo.setWeight(cargoWeight);
        cargo.setName(cargoName);
        cargoDao.update(cargo);
        return cargo;
    }

    public Converter<Waypoint, WaypointDto> toDtoConverter() {
        return context -> {
            Waypoint source = context.getSource();
            WaypointDto destination = context.getDestination();
            mapSpecificFieldsForDto(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForDto(Waypoint source, WaypointDto destination) {
        destination.setCargoName(Objects.isNull(source) || Objects.isNull(source.getCargo())
                || Objects.isNull(source.getCargo().getName()) ? null : source.getCargo().getName());
        destination.setCargoWeight(Objects.isNull(source) || Objects.isNull(source.getCargo())
                || Objects.isNull(source.getCargo().getWeight()) ? null : source.getCargo().getWeight());
        destination.setCargoNumber(Objects.isNull(source) || Objects.isNull(source.getCargo())
                || Objects.isNull(source.getCargo().getNumber()) ? null : source.getCargo().getNumber());
        destination.setCityName(Objects.isNull(source) || Objects.isNull(source.getCity())
                || Objects.isNull(source.getCity().getName()) ? null : source.getCity().getName());
        destination.setAction(Objects.isNull(source) || Objects.isNull(source.getAction()) ? null :
                source.getAction().toString());
        destination.setStatus(Objects.isNull(source) || Objects.isNull(source.getStatus()) ? null :
                source.getStatus().toString());
    }
}
