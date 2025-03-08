package ru.isands.test.estore.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "electro_item")
public class ElectroItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "electroitem_counter")
    @TableGenerator(name = "electroitem_counter", pkColumnName = "name", pkColumnValue = "ru.isands.test.estore.dao.entity.ElectroItem", table = "counter", valueColumnName = "currentid", allocationSize = 1)
    @Column(name = "id_", unique = true, nullable = false)
    @NotNull
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @ManyToOne
    @JoinColumn(name = "typeid", nullable = false)
    private ElectroItemType type;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "archive", nullable = false)
    private Boolean archive;

    @Column(name = "description")
    private String description;
}