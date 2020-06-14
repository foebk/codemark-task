package ru.codemark.Entities;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String login;

    private String password;

    @ManyToMany
    @JoinTable(
            name = "roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RolesListEntity> rolesListEntities;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RolesListEntity> getRolesListEntities() {
        return rolesListEntities;
    }

    public void setRolesListEntities(Set<RolesListEntity> rolesListEntities) {
        this.rolesListEntities = rolesListEntities;
    }
}
