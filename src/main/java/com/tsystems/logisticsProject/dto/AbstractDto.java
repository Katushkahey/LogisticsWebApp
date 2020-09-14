package com.tsystems.logisticsProject.dto;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
@Data
public abstract class AbstractDto implements Serializable {

    public Long id;
}
