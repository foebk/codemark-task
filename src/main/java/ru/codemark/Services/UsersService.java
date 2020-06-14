package ru.codemark.Services;

import ru.codemark.Models.UserWithoutRolesModel;

import java.util.List;

public interface UsersService {
    List<UserWithoutRolesModel> getUsersList();
}
