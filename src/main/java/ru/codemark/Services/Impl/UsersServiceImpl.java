package ru.codemark.Services.Impl;

import org.springframework.stereotype.Service;
import ru.codemark.Models.UserWithoutRolesModel;
import ru.codemark.Entities.UserEntity;
import ru.codemark.Repos.UsersRepository;
import ru.codemark.Services.UsersService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<UserWithoutRolesModel> getUsersList() {
        List<UserEntity> users = usersRepository.findAll();
        List<UserWithoutRolesModel> usersWithoutRoles = new ArrayList<>();

        users.forEach(u -> {
            UserWithoutRolesModel userWithoutRoles = new UserWithoutRolesModel();

            userWithoutRoles.setId(u.getId());
            userWithoutRoles.setName(u.getName());
            userWithoutRoles.setLogin(u.getLogin());
            userWithoutRoles.setPassword(u.getPassword());

            usersWithoutRoles.add(userWithoutRoles);
        });

        return usersWithoutRoles;
    }
}
