package ru.codemark.Services;

import ru.codemark.Models.DataErrorModel;
import ru.codemark.Models.UserAddModel;
import ru.codemark.Models.UserModel;
import ru.codemark.Models.UserWithoutRolesModel;

import java.util.List;

public interface UsersService {
    List<UserWithoutRolesModel> getUsersList();

    UserModel getUser(String login);

    DataErrorModel addUser(UserAddModel user);

    DataErrorModel editUser(UserAddModel user);

    String deleteUser(String login);
}
