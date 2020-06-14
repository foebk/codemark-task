package ru.codemark.Repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.codemark.Entities.RolesListEntity;

import java.util.List;

@Repository
public interface RolesListRepository extends CrudRepository<RolesListEntity, Integer> {
    List<RolesListEntity> findAll();
}
