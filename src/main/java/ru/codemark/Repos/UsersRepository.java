package ru.codemark.Repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.codemark.Entities.UserEntity;

import java.util.List;

@Repository
public interface UsersRepository extends CrudRepository<UserEntity, Integer> {
    List<UserEntity> findAll();

    UserEntity findAllByLogin(String login);

    void deleteByLogin(String login);
}
