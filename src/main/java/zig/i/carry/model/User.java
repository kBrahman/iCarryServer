package zig.i.carry.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @NotBlank
    @Column(unique = true)
    private String login;

    @NotBlank
    private String pwd;

    public User() {
    }

    public User(String email) {
        this.login = email;
    }

    public User(String name, String pwd, String login) {
        this.name = name;
        this.pwd = pwd;
        this.login = login;
    }

    public User(String login, String pwd) {
        this.login = login;
        this.pwd = pwd;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPwd() {
        return pwd;
    }
}
