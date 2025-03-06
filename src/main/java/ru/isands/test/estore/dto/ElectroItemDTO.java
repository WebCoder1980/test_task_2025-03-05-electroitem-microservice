package ru.isands.test.estore.dto;

import lombok.Getter;
import lombok.Setter;
import ru.isands.test.estore.dao.entity.ElectroItemType;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ElectroItemDTO {
    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Long type;

    @NotNull
    private Double price;

    @NotNull
    private Integer quantity;

    @NotNull
    private Boolean archive;

    @NotNull
    private String description;
}
