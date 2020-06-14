package ru.codemark.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.codemark.Models.DataErrorModel;
import ru.codemark.Models.UserAddModel;
import ru.codemark.Models.UserModel;
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

    @GetMapping("/get")
    public UserModel getUserByLogin(@RequestParam(name = "login") String login) {
        UserModel userModel = usersService.getUser(login);

        return userModel;
    }

    @PostMapping("/add")
    public DataErrorModel addNewUser(@RequestBody UserAddModel newUser) {
        DataErrorModel dataErrorModel = usersService.addUser(newUser);

        return dataErrorModel;
    }

    @DeleteMapping("/delete")
    public String deleteUserByLogin(@RequestParam String login) {
        String msg = usersService.deleteUser(login);

        return msg;
    }

    @PutMapping("/edit")
    public DataErrorModel editUser(@RequestBody UserAddModel user) {
        DataErrorModel dataErrorModel = usersService.editUser(user);

        return dataErrorModel;
    }
}
