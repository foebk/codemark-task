package ru.codemark.Services.Impl;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import ru.codemark.Entities.UserEntity;
import ru.codemark.Models.DataErrorModel;
import ru.codemark.Models.UserAddModel;
import ru.codemark.Repos.RolesListRepository;
import ru.codemark.Repos.UsersRepository;
import ru.codemark.Services.UsersService;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

public class UsersServiceImplTest {

    @Mock
    private UsersRepository usersRepository;
    @Mock
    private RolesListRepository rolesListRepository;

    @Autowired
    private UsersService usersService;

    public UsersServiceImplTest() {
        MockitoAnnotations.initMocks(this);
        usersService = new UsersServiceImpl(usersRepository, rolesListRepository);
    }

    @Test
    public void addUserSuccess() {
        given(usersRepository.findAll()).willReturn(Collections.emptyList());

        UserAddModel userAddModel = new UserAddModel();

        userAddModel.setName("Sasha");
        userAddModel.setPassword("Test1");
        userAddModel.setLogin("Check");

        UserEntity userEntity = new UserEntity();

        given(usersRepository.save(userEntity)).willReturn(null);

        DataErrorModel expected = new DataErrorModel(true, null);
        DataErrorModel actual = usersService.addUser(userAddModel);

        assertEquals(expected, actual);
    }

    @Test
    public void addUserWithEmptyName() {
        given(usersRepository.findAll()).willReturn(Collections.emptyList());

        UserAddModel userAddModel = new UserAddModel();

        userAddModel.setName("");
        userAddModel.setPassword("Test1");
        userAddModel.setLogin("Check");

        DataErrorModel expected = new DataErrorModel(false, Collections.singletonList("Name is empty"));
        DataErrorModel actual = usersService.addUser(userAddModel);

        assertEquals(expected, actual);
    }

    @Test
    public void addUserWithEmptyLogin() {
        given(usersRepository.findAll()).willReturn(Collections.emptyList());

        UserAddModel userAddModel = new UserAddModel();

        userAddModel.setName("Sasha");
        userAddModel.setPassword("Test1");
        userAddModel.setLogin("");

        DataErrorModel expected = new DataErrorModel(false, Collections.singletonList("Login is empty"));
        DataErrorModel actual = usersService.addUser(userAddModel);

        assertEquals(expected, actual);
    }

    @Test
    public void addUserWithEmptyPassword() {
        given(usersRepository.findAll()).willReturn(Collections.emptyList());

        UserAddModel userAddModel = new UserAddModel();

        userAddModel.setName("Sasha");
        userAddModel.setPassword("");
        userAddModel.setLogin("Check");

        DataErrorModel expected = new DataErrorModel(false, Arrays.asList("Password is empty",
                "Password must contain at least one number",
                "Password must contain at least one uppercase letter"));
        expected.getErrors().sort(String::compareTo);

        DataErrorModel actual = usersService.addUser(userAddModel);
        actual.getErrors().sort(String::compareTo);

        assertEquals(expected, actual);
    }

    @Test
    public void addUserWithAllPasswordErrors() {
        given(usersRepository.findAll()).willReturn(Collections.emptyList());

        UserAddModel userAddModel = new UserAddModel();

        userAddModel.setName("Sasha");
        userAddModel.setPassword("test");
        userAddModel.setLogin("Check");

        DataErrorModel expected = new DataErrorModel(false, Arrays.asList("Password must contain at least one number",
                "Password must contain at least one uppercase letter"));
        expected.getErrors().sort(String::compareTo);

        DataErrorModel actual = usersService.addUser(userAddModel);
        actual.getErrors().sort(String::compareTo);

        assertEquals(expected, actual);
    }

    @Test
    public void addUserWithAllErrors() {
        given(usersRepository.findAll()).willReturn(Collections.emptyList());

        UserAddModel userAddModel = new UserAddModel();

        DataErrorModel expected = new DataErrorModel(false, Arrays.asList("Password must contain at least one number",
                "Password must contain at least one uppercase letter",
                "Name is empty",
                "Login is empty",
                "Password is empty"));
        expected.getErrors().sort(String::compareTo);

        DataErrorModel actual = usersService.addUser(userAddModel);
        actual.getErrors().sort(String::compareTo);

        assertEquals(expected, actual);
    }

    @Test
    public void addUserWithExistLogin() {
        UserEntity userEntity = new UserEntity();

        given(usersRepository.findLogins()).willReturn(Collections.singletonList("Check"));

        UserAddModel userAddModel = new UserAddModel();

        userAddModel.setName("Sasha");
        userAddModel.setPassword("Test1");
        userAddModel.setLogin("Check");

        DataErrorModel expected = new DataErrorModel(false, Collections.singletonList("User with this login already exists"));
        DataErrorModel actual = usersService.addUser(userAddModel);

        assertEquals(expected, actual);
    }

    @Test
    public void addUserWithPasswordWithoutUpperCaseLetter() {

    }

    @Test
    public void deleteUser() {
    }

    @Test
    public void editUser() {
    }
}