package ru.isands.test.estore.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ElectroItemTypeDTO {

    @NotNull
    private Long id;

    @NotNull
    private String name;
}