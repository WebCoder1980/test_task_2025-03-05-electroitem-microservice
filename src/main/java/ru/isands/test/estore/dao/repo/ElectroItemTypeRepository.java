package ru.isands.test.estore.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isands.test.estore.dao.entity.ElectroItemType;

public interface ElectroItemTypeRepository extends JpaRepository<ElectroItemType, Long> {
}