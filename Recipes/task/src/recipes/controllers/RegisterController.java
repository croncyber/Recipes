package recipes.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import recipes.models.User;
import recipes.services.UserService;

import javax.validation.Valid;
import java.util.regex.Pattern;

@RestController
@RequestMapping(("/api"))
public class RegisterController {

    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public void register(@Valid @RequestBody User user) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        boolean checkEmail = Pattern.compile(regexPattern)
                .matcher(user.getEmail())
                .matches();

        if (
                user.getEmail().length() >= 8 &&
                        checkEmail &&
                        user.getPassword().length() >= 8 &&
                        !userService.existUser(user.getEmail())
        ) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("ROLE_USER");
            userService.saveUser(user);
            throw new ResponseStatusException(HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }
}

