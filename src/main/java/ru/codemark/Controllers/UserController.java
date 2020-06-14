package ru.codemark.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.codemark.Models.UserWithoutRolesModel;
import ru.codemark.Services.UsersService;

import java.util.List;

@RestController
public class UserController {
    private final UsersService usersService;

    @Autowired
    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/list")
    public List<UserWithoutRolesModel> getUserList() {
        List<UserWithoutRolesModel> userWithoutRolesModel = usersService.getUsersList();

        return userWithoutRolesModel;
    }
}
