package ua.yware.slace.web.rest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserForm {

    private String login;

    private String password;

    private String email;

    private String phone;

    private String firstName;

    private String lastName;

    private String city;

    private String about;

}
