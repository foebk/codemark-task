package ru.codemark.Services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.codemark.Entities.RolesListEntity;
import ru.codemark.Entities.UserEntity;
import ru.codemark.ErrorMessages;
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

    @Autowired
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
        UserEntity user = usersRepository.findByLogin(login);
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
        List<String> allUsers = usersRepository.findLogins();
        if (allUsers.stream().anyMatch(u -> u.equals(user.getLogin()))) {
            return new DataErrorModel(false, Collections.singletonList(ErrorMessages.USER_EXIST));
        }

        List<String> errors = dataValidation(user);
        if (errors.size() != 0) {
            return new DataErrorModel(false, errors);
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setName(user.getName());
        userEntity.setLogin(user.getLogin());
        userEntity.setPassword(user.getPassword());

        Set<RolesListEntity> rolesListEntities = roleNumsToRolesListEntity(user.getRoles());

        userEntity.setRolesListEntities(rolesListEntities);

        usersRepository.save(userEntity);

        return new DataErrorModel(true, null);
    }

    @Override
    @Transactional
    public DataErrorModel deleteUser(String login) {
        if (usersRepository.findLogins().stream().anyMatch(u -> u.equals(login))) {
            usersRepository.deleteByLogin(login);
            return new DataErrorModel(true, null);
        }

        return new DataErrorModel(false, Collections.singletonList(ErrorMessages.USER_DOESNT_EXIST));
    }

    @Override
    public DataErrorModel editUser(UserAddModel userAddModel) {
        List<String> errors = new ArrayList<>();
        UserEntity user = usersRepository.findByLogin(userAddModel.getLogin());

        if (user == null) {
            return new DataErrorModel(false, Collections.singletonList(ErrorMessages.USER_DOESNT_EXIST));
        }

        if (userAddModel.getName() != null) {
            if (userAddModel.getName().length() == 0) {
                errors.add(ErrorMessages.EMPTY_NAME);
            } else {
                user.setName(userAddModel.getName());
            }
        }

        if (userAddModel.getPassword() != null) {
            if (userAddModel.getPassword().length() == 0) {
                errors.addAll(Arrays.asList(ErrorMessages.EMPTY_PASSWORD, ErrorMessages.PASSWORD_NUM,
                        ErrorMessages.PASSWORD_UPPERCASE_LETTER));
            } else {
                List<String> passwordValidationErrors = passwordValidation(userAddModel.getPassword());

                if (passwordValidationErrors.size() == 0) {
                    user.setPassword(userAddModel.getPassword());
                } else {
                    errors.addAll(passwordValidationErrors);
                }
            }
        }

        if (errors.size() != 0) {
            return new DataErrorModel(false, errors);
        }

        Set<RolesListEntity> rolesListEntities = roleNumsToRolesListEntity(userAddModel.getRoles());
        if (rolesListEntities != null) {
            user.setRolesListEntities(rolesListEntities);
        }

        usersRepository.save(user);
        return new DataErrorModel(true, null);
    }

    private List<String> dataValidation(UserAddModel user) {
        List<String> errors = new ArrayList<>();

        if (user.getName() == null || user.getName().length() == 0) {
            errors.add(ErrorMessages.EMPTY_NAME);
        }

        if (user.getLogin() == null || user.getLogin().length() == 0) {
            errors.add(ErrorMessages.EMPTY_LOGIN);
        }

        if (user.getPassword() == null || user.getPassword().length() == 0) {
            errors.addAll(Arrays.asList(ErrorMessages.EMPTY_PASSWORD, ErrorMessages.PASSWORD_NUM,
                    ErrorMessages.PASSWORD_UPPERCASE_LETTER));
        } else {
            errors.addAll(passwordValidation(user.getPassword()));
        }

        return errors;
    }

    private List<String> passwordValidation(String password) {
        List<String> errors = new ArrayList<>();

        Pattern[] patterns = new Pattern[2];
        patterns[0] = Pattern.compile("[A-Z]+");
        patterns[1] = Pattern.compile("[0-9]+");
        for (int i = 0; i < 2; i++) {
            Matcher matcher = patterns[i].matcher(password);
            if (!matcher.find()) {
                errors.add(i == 0 ? ErrorMessages.PASSWORD_UPPERCASE_LETTER : ErrorMessages.PASSWORD_NUM);
            }
        }
        return errors;
    }

    private Set<RolesListEntity> roleNumsToRolesListEntity(int[] roles) {
        if (roles == null) {
            return Collections.emptySet();
        }

        Map<Integer, String> collect = rolesListRepository.findAll().stream()
                .collect(Collectors.toMap(RolesListEntity::getId, RolesListEntity::getName));

        Integer max = collect.keySet().stream().max(Comparator.naturalOrder()).orElse(0);
        Integer min = collect.keySet().stream().min(Comparator.naturalOrder()).orElse(0);

        return Arrays.stream(roles).filter(role -> role >= min).filter(role -> role <= max).mapToObj(role -> {
            RolesListEntity rolesListEntity = new RolesListEntity();
            rolesListEntity.setId(role);
            rolesListEntity.setName(collect.get(role));

            return rolesListEntity;
        }).collect(Collectors.toSet());
    }
}
