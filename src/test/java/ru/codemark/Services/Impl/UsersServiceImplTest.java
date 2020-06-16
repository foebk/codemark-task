package ru.codemark.Services.Impl;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.codemark.Entities.UserEntity;
import ru.codemark.ErrorMessages;
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

    private final UsersService usersService;

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

        DataErrorModel expected = new DataErrorModel(false, Collections.singletonList(ErrorMessages.EMPTY_NAME));
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

        DataErrorModel expected = new DataErrorModel(false, Collections.singletonList(ErrorMessages.EMPTY_LOGIN));
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

        DataErrorModel expected = new DataErrorModel(false, Arrays.asList(ErrorMessages.EMPTY_PASSWORD,
                ErrorMessages.PASSWORD_NUM, ErrorMessages.PASSWORD_UPPERCASE_LETTER));
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

        DataErrorModel expected = new DataErrorModel(false, Arrays.asList(ErrorMessages.PASSWORD_NUM,
                ErrorMessages.PASSWORD_UPPERCASE_LETTER));
        expected.getErrors().sort(String::compareTo);

        DataErrorModel actual = usersService.addUser(userAddModel);
        actual.getErrors().sort(String::compareTo);

        assertEquals(expected, actual);
    }

    @Test
    public void addUserWithAllErrors() {
        given(usersRepository.findAll()).willReturn(Collections.emptyList());

        UserAddModel userAddModel = new UserAddModel();

        DataErrorModel expected = new DataErrorModel(false, Arrays.asList(ErrorMessages.EMPTY_NAME,
                ErrorMessages.EMPTY_LOGIN, ErrorMessages.EMPTY_PASSWORD, ErrorMessages.PASSWORD_NUM,
                ErrorMessages.PASSWORD_UPPERCASE_LETTER));
        expected.getErrors().sort(String::compareTo);

        DataErrorModel actual = usersService.addUser(userAddModel);
        actual.getErrors().sort(String::compareTo);

        assertEquals(expected, actual);
    }

    @Test
    public void addUserWithExistLogin() {
        given(usersRepository.findLogins()).willReturn(Collections.singletonList("Check"));

        UserAddModel userAddModel = new UserAddModel();

        userAddModel.setName("Sasha");
        userAddModel.setPassword("Test1");
        userAddModel.setLogin("Check");

        DataErrorModel expected = new DataErrorModel(false, Collections.singletonList(ErrorMessages.USER_EXIST));
        DataErrorModel actual = usersService.addUser(userAddModel);

        assertEquals(expected, actual);
    }

    @Test
    public void addUserWithoutNumInPassword() {
        given(usersRepository.findLogins()).willReturn(Collections.emptyList());

        UserAddModel userAddModel = new UserAddModel();

        userAddModel.setName("Sasha");
        userAddModel.setPassword("Test");
        userAddModel.setLogin("Check");

        DataErrorModel expected = new DataErrorModel(false, Collections.singletonList(ErrorMessages.PASSWORD_NUM));
        DataErrorModel actual = usersService.addUser(userAddModel);

        assertEquals(expected, actual);
    }

    @Test
    public void addUserWithoutUppercaseLetterInPassword() {
        given(usersRepository.findLogins()).willReturn(Collections.emptyList());

        UserAddModel userAddModel = new UserAddModel();

        userAddModel.setName("Sasha");
        userAddModel.setPassword("test1");
        userAddModel.setLogin("Check");

        DataErrorModel expected = new DataErrorModel(false, Collections.singletonList(ErrorMessages.PASSWORD_UPPERCASE_LETTER));
        DataErrorModel actual = usersService.addUser(userAddModel);

        assertEquals(expected, actual);
    }

    @Test
    public void deleteUserSuccess() {
        given(usersRepository.findLogins()).willReturn(Collections.singletonList("Check"));

        DataErrorModel actual = new DataErrorModel(true, null);
        DataErrorModel expected = usersService.deleteUser("Check");

        assertEquals(expected, actual);
    }

    @Test
    public void deleteUserFailure() {
        given(usersRepository.findLogins()).willReturn(Collections.emptyList());

        DataErrorModel actual = new DataErrorModel(false, Collections.singletonList(ErrorMessages.USER_DOESNT_EXIST));
        DataErrorModel expected = usersService.deleteUser("Check");

        assertEquals(expected, actual);
    }

    @Test
    public void editUserSuccess() {
        UserEntity userEntity = new UserEntity();

        userEntity.setName("Masha");
        userEntity.setLogin("Check");
        userEntity.setPassword("Test2");

        given(usersRepository.findByLogin("Check")).willReturn(userEntity);

        UserAddModel userAddModel = new UserAddModel();

        userAddModel.setName("Sasha");
        userAddModel.setPassword("Test1");
        userAddModel.setLogin("Check");

        DataErrorModel expected = new DataErrorModel(true, null);
        DataErrorModel actual = usersService.editUser(userAddModel);

        assertEquals(expected, actual);
    }

    @Test
    public void editNonexistentUser() {
        given(usersRepository.findByLogin("Check")).willReturn(null);

        UserAddModel userAddModel = new UserAddModel();

        userAddModel.setName("Sasha");
        userAddModel.setPassword("Test1");
        userAddModel.setLogin("Check");

        DataErrorModel expected = new DataErrorModel(false, Collections.singletonList(ErrorMessages.USER_DOESNT_EXIST));
        DataErrorModel actual = usersService.editUser(userAddModel);

        assertEquals(expected, actual);
    }

    @Test
    public void editUserWithIncorrectPassword() {
        UserEntity userEntity = new UserEntity();

        userEntity.setName("Masha");
        userEntity.setLogin("Check");
        userEntity.setPassword("Test2");

        given(usersRepository.findByLogin("Check")).willReturn(userEntity);

        UserAddModel userAddModel = new UserAddModel();

        userAddModel.setPassword("");
        userAddModel.setLogin("Check");

        DataErrorModel expected = new DataErrorModel(false, Arrays.asList(ErrorMessages.EMPTY_PASSWORD,
                ErrorMessages.PASSWORD_UPPERCASE_LETTER, ErrorMessages.PASSWORD_NUM));
        expected.getErrors().sort(String::compareTo);

        DataErrorModel actual = usersService.editUser(userAddModel);
        actual.getErrors().sort(String::compareTo);

        assertEquals(expected, actual);
    }

    @Test
    public void editUserWithoutNumPassword() {
        UserEntity userEntity = new UserEntity();

        userEntity.setName("Masha");
        userEntity.setLogin("Check");
        userEntity.setPassword("Test2");

        given(usersRepository.findByLogin("Check")).willReturn(userEntity);

        UserAddModel userAddModel = new UserAddModel();

        userAddModel.setPassword("Test");
        userAddModel.setLogin("Check");

        DataErrorModel expected = new DataErrorModel(false, Collections.singletonList(ErrorMessages.PASSWORD_NUM));
        DataErrorModel actual = usersService.editUser(userAddModel);

        assertEquals(expected, actual);
    }

    @Test
    public void editUserWithoutUppercaseLetterPassword() {
        UserEntity userEntity = new UserEntity();

        userEntity.setName("Masha");
        userEntity.setLogin("Check");
        userEntity.setPassword("Test2");

        given(usersRepository.findByLogin("Check")).willReturn(userEntity);

        UserAddModel userAddModel = new UserAddModel();

        userAddModel.setPassword("test1");
        userAddModel.setLogin("Check");

        DataErrorModel expected = new DataErrorModel(false, Collections.singletonList(ErrorMessages.PASSWORD_UPPERCASE_LETTER));
        DataErrorModel actual = usersService.editUser(userAddModel);

        assertEquals(expected, actual);
    }

    @Test
    public void editUserWithEmptyName() {
        UserEntity userEntity = new UserEntity();

        userEntity.setName("Masha");
        userEntity.setLogin("Check");
        userEntity.setPassword("Test2");

        given(usersRepository.findByLogin("Check")).willReturn(userEntity);

        UserAddModel userAddModel = new UserAddModel();

        userAddModel.setName("");
        userAddModel.setLogin("Check");

        DataErrorModel expected = new DataErrorModel(false, Collections.singletonList(ErrorMessages.EMPTY_NAME));
        DataErrorModel actual = usersService.editUser(userAddModel);

        assertEquals(expected, actual);
    }
}