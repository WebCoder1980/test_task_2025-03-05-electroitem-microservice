package ru.isands.test.estore.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "electro_item_type")
public class ElectroItemType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "electroitemtype_counter")
    @TableGenerator(name = "electroitemtype_counter", pkColumnName = "name", pkColumnValue = "ru.isands.test.estore.dao.entity.ElectroItemType", table = "counter", valueColumnName = "currentid", allocationSize = 1)
    @Column(name = "id_", unique = true, nullable = false)
    @NotNull
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;
}