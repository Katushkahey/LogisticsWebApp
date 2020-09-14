package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dao.CityDao;
import com.tsystems.logisticsProject.dto.NewOrderWaypointDto;
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
public class NewOrderWaypointMapper {

    private ModelMapper modelMapper;
    private CityDao cityDao;

    @Autowired
    public NewOrderWaypointMapper(ModelMapper modelMapper, CityDao cityDao) {
        this.modelMapper = modelMapper;
        this.cityDao = cityDao;
    }

    public Waypoint toEntity(NewOrderWaypointDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Waypoint.class);
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(NewOrderWaypointDto.class, Waypoint.class)
                .addMappings(m -> m.skip(Waypoint::setId)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Waypoint::setCity)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Waypoint::setAction)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Waypoint::setStatus)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Waypoint::setSequence)).setPostConverter(toEntityConverter());
    }

    public Converter<NewOrderWaypointDto, Waypoint> toEntityConverter() {
        return context -> {
            NewOrderWaypointDto source = context.getSource();
            Waypoint destination = context.getDestination();
            mapSpecificFieldsForEntity(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForEntity(NewOrderWaypointDto source, Waypoint destination) {
        destination.setId(null);
        destination.setCity(Objects.isNull(source) || Objects.isNull(source.getCityName())
                ? null : cityDao.findByName(source.getCityName()));
        destination.setAction(Objects.isNull(source) || Objects.isNull(source.getAction())
                ? null : Action.valueOf(source.getAction()));
        destination.setStatus(Objects.isNull(source) ? null : WaypointStatus.TODO);
        destination.setSequence(source.getId());
    }

}
