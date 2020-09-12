package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dao.CityDao;
import com.tsystems.logisticsProject.dao.OrderDao;
import com.tsystems.logisticsProject.dao.TruckDao;
import com.tsystems.logisticsProject.dto.TruckDto;
import com.tsystems.logisticsProject.entity.Order;
import com.tsystems.logisticsProject.entity.Truck;
import com.tsystems.logisticsProject.entity.enums.TruckState;
import com.tsystems.logisticsProject.exception.NotUniqueTruckNumberException;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class TruckMapper {

    private ModelMapper modelMapper;
    private CityDao cityDao;
    private OrderDao orderDao;
    private TruckDao truckDao;

    @Autowired
    public TruckMapper(ModelMapper modelMapper, CityDao cityDao, OrderDao orderDao, TruckDao truckDao) {
        this.modelMapper = modelMapper;
        this.cityDao = cityDao;
        this.orderDao = orderDao;
        this.truckDao = truckDao;
    }

    public Truck toEntity(TruckDto dto) throws NotUniqueTruckNumberException {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Truck.class);
    }

    public TruckDto toDto(Truck entity) {
        return Objects.isNull(entity) ? null : modelMapper.map(entity, TruckDto.class);
    }

    @PostConstruct
    public void setupMapper() throws NotUniqueTruckNumberException{
        modelMapper.createTypeMap(Truck.class, TruckDto.class)
                .addMappings(m -> m.skip(TruckDto::setState)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(TruckDto::setAvailable)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(TruckDto::setCityName)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(TruckDto.class, Truck.class)
                .addMappings(m-> m.skip(Truck::setNumber)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Truck::setTruckState)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Truck::setCurrentCity)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Truck::setOrder)).setPostConverter(toEntityConverter());
    }

//    public Converter<TruckDto, Truck> toEntityConverter() {
//        return context -> {
//            TruckDto source = context.getSource();
//            Truck destination = context.getDestination();
//            mapSpecificFieldsForEntity(source, destination);
//            return context.getDestination();
//        };
//    }

    public Converter<TruckDto, Truck> toEntityConverter()  {
        return new Converter<TruckDto, Truck>() {
            @Override
            public Truck convert(MappingContext<TruckDto, Truck> mappingContext) throws NotUniqueTruckNumberException {
                mapSpecificFieldsForEntity(mappingContext.getSource(), mappingContext.getDestination());
                return mappingContext.getDestination();
            }
        }
    }

    public void mapSpecificFieldsForEntity(TruckDto source, Truck destination) throws NotUniqueTruckNumberException {
        if (source != null) {
            destination.setNumber(Objects.isNull(source.getId()) ? checkEditedNumber(source.getNumber()) :
                    checkEditedNumber(source.getNumber(), source.getId()));
            destination.setTruckState(Objects.isNull(source.getState()) ? null : TruckState.valueOf(source.getState()));
            destination.setCurrentCity(Objects.isNull(source.getCityName()) ? null : cityDao.findByName(source.getCityName()));

            if (source.isAvailable() || source.getId() != null) {
                destination.setOrder(null);
            } else {
                Truck truck = truckDao.findById(source.getId());
                Order order = orderDao.findByTruck(truck);
                if (truck == null || order == null) {
                    destination.setOrder(null);
                }
                destination.setOrder(order);
            }
        }
    }

    private String checkEditedNumber(String number, Long id) throws NotUniqueTruckNumberException {
        Truck truck = truckDao.findByNumber(number);
        if (truck.getId() != id) {
            throw new NotUniqueTruckNumberException(number);
        }
        return number;
    }

    private String checkEditedNumber(String number) throws NotUniqueTruckNumberException  {
        Truck truck = truckDao.findByNumber(number);
        if (truck == null) {
            return null;
        } else {
            throw new NotUniqueTruckNumberException(number);
        }
    }

    public Converter<Truck, TruckDto> toDtoConverter() {
        return context -> {
            Truck source = context.getSource();
            TruckDto destination = context.getDestination();
            mapSpecificFieldsForDto(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFieldsForDto(Truck source, TruckDto destination) {
        destination.setState(Objects.isNull(source) || Objects.isNull(source.getTruckState()) ? null :
                source.getTruckState().toString());
        destination.setAvailable(Objects.isNull(source) ? null : (Objects.isNull(source.getOrder())));
        destination.setCityName(Objects.isNull(source) || Objects.isNull(source.getCurrentCity()) ? null :
                source.getCurrentCity().getName());
    }


}
