package ru.codemark.Services.Impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.codemark.Entities.RolesListEntity;
import ru.codemark.Entities.UserEntity;
import ru.codemark.Models.*;
import ru.codemark.Repos.RolesListRepository;
import ru.codemark.Repos.UsersRepository;
import ru.codemark.Services.UsersService;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final RolesListRepository rolesListRepository;

    public UsersServiceImpl(UsersRepository usersRepository, RolesListRepository rolesListRepository) {
        this.usersRepository = usersRepository;
        this.rolesListRepository = rolesListRepository;
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

    @Override
    public UserModel getUser(String login) {
        UserEntity user = usersRepository.findAllByLogin(login);
        if (user == null) {
            return null;
        }
        UserModel newUser = new UserModel();
        
        newUser.setId(user.getId());
        newUser.setName(user.getName());
        newUser.setLogin(user.getLogin());
        newUser.setPassword(user.getPassword());

        List<RoleModel> roles = user.getRolesListEntities()
                .stream()
                .map(u -> new RoleModel(u.getId(), u.getName()))
                .sorted(Comparator.comparingInt(RoleModel::getId))
                .collect(Collectors.toList());

        newUser.setRoles(roles);

        return newUser;
    }

    @Override
    public DataErrorModel addUser(UserAddModel user) {
        List<String> errors = dataValidation(user);
        if (errors.size() != 0) {
            return new DataErrorModel(false, errors);
        }

        List<RolesListEntity> allRoles = rolesListRepository.findAll();

        UserEntity userEntity = new UserEntity();
        userEntity.setName(user.getName());
        userEntity.setLogin(user.getLogin());
        userEntity.setPassword(user.getPassword());

        Set<RolesListEntity> rolesListEntities = Arrays.stream(user.getRoles()).mapToObj(role -> {
            RolesListEntity rolesListEntity = new RolesListEntity();
            rolesListEntity.setId(role);
            rolesListEntity.setName(allRoles.get(role - 1).getName());

            return rolesListEntity;
        }).collect(Collectors.toSet());

        userEntity.setRolesListEntities(rolesListEntities);

        usersRepository.save(userEntity);

        return new DataErrorModel(true, null);
    }

    @Override
    @Transactional
    public String deleteUser(String login) {
        usersRepository.deleteByLogin(login);

        return "Entity was deleted or didn't exist";
    }

    private List<String> dataValidation(UserAddModel user) {
        List<String> errors = passwordValidation(user.getPassword());
        if (user.getName() == null || user.getName().length() == 0) {
            errors.add("Name is empty");
        }
        if (user.getLogin() == null || user.getLogin().length() == 0) {
            errors.add("Login is empty");
        }

        return errors;
    }

    private List<String> passwordValidation(String password) {
        List<String> errors = new ArrayList<>();
        if (password == null || password.length() == 0) {
            errors.addAll(Arrays.asList("Password is empty", "Password must contain at least one number",
                    "Password must contain at least one uppercase letter"));
            return errors;
        }

        Pattern[] patterns = new Pattern[2];
        patterns[0] = Pattern.compile("[A-Z]+");
        patterns[1] = Pattern.compile("[0-9]+");
        for (int i = 0; i < 2; i++) {
            Matcher matcher = patterns[i].matcher(password);
            if (!matcher.find()) {
                errors.add(i == 0 ? "Password must contain at least one number" : "Password must contain at least one uppercase letter");
            }
        }
        return errors;
    }
}
