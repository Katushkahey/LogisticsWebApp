package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dao.CargoDao;
import com.tsystems.logisticsProject.dao.CityDao;
import com.tsystems.logisticsProject.dto.WaypointDto;
import com.tsystems.logisticsProject.entity.Waypoint;
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
                .addMappings(m -> m.skip(WaypointDto::setCityName)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(WaypointDto.class, Waypoint.class)
                .addMappings(m -> m.skip(Waypoint::setCargo)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Waypoint::setCity)).setPostConverter(toEntityConverter());
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
        if (source != null) {
            if (source.getCargoName() == null) {
                destination.setCargo(null);
            } else {
                destination.setCargo(cargoDao.findByNumber(source.getCargoNumber()));
            }

            if (source.getCityName() == null) {
                destination.setCity(null);
            } else {
                destination.setCity(cityDao.findByName(source.getCityName()));
            }
        }
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
        destination.setCargoName(Objects.isNull(source) || Objects.isNull(source.getCargo()) ? null :
                source.getCargo().getName());
        destination.setCargoWeight(Objects.isNull(source) || Objects.isNull(source.getCargo()) ? null :
                source.getCargo().getWeight());
        destination.setCargoNumber(Objects.isNull(source) || Objects.isNull(source.getCargo()) ? null :
                source.getCargo().getNumber());
        destination.setCityName(Objects.isNull(source) || Objects.isNull(source.getCity()) ? null :
                source.getCity().getName());
    }
}
