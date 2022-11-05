package com.frkn.invoicelister.model;

import javax.persistence.*;

@Entity
@Table(name = "sys_user")
public class SystemUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sys_user_generator")
    @SequenceGenerator(name = "sys_user_generator", sequenceName = "sys_user_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    public SystemUser(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public SystemUser() {
    }

    public SystemUser(long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
