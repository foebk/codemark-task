package ru.codemark.Repos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.codemark.Entities.UserEntity;

import java.util.List;

@Repository
public interface UsersRepository extends CrudRepository<UserEntity, Integer> {
    List<UserEntity> findAll();

    UserEntity findByLogin(String login);

    @Query(value = "SELECT login FROM users", nativeQuery = true)
    List<String> findLogins();

    void deleteByLogin(String login);
}
