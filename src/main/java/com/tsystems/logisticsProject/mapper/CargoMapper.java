package com.tsystems.logisticsProject.mapper;

import com.tsystems.logisticsProject.dto.CargoDto;
import com.tsystems.logisticsProject.entity.Cargo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CargoMapper {

    private ModelMapper modelMapper;

    @Autowired
    public void setDependecies(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Cargo toEntity(CargoDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Cargo.class);
    }
}
