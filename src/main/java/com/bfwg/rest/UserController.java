package com.bfwg.rest;

import com.bfwg.model.User;
import com.bfwg.repository.UserRepository;
import com.bfwg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by fan.jin on 2016-10-15.
 */

@RestController
@RequestMapping( value = "/api", produces = MediaType.APPLICATION_JSON_VALUE )
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping( method = GET, value = "/user/{userId}" )
    @PreAuthorize("hasRole('ADMIN')")
    public User loadById( @PathVariable Long userId ) {
        return this.userService.findById( userId );
    }

    @RequestMapping( method = GET, value= "/user/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> loadAll() {
        return this.userService.findAll();
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public User createPost(@Valid @RequestBody Request request) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(request.password);
        System.out.println(request);
        User user = new User();
        user.setPassword(bCryptPasswordEncoder.encode(request.password));
        user.setPassword(request.username);
        user.setPassword(request.firstName);
        user.setPassword(request.lastName);
        user.setPassword(request.phoneNumber);

//        System.out.println(bCryptPasswordEncoder.encode(passwordChanger.password));

        return userRepository.save(user);
    }
    static class Request {
        public String password;
        public String username;
        public String firstName;
        public String lastName;
        public String email;
        public String phoneNumber;
        public int enabled;

    }

    /*
     *  We are not using userService.findByUsername here(we could),
     *  so it is good that we are making sure that the user has role "ROLE_USER"
     *  to access this endpoint.
     */
    @RequestMapping("/whoami")
    @PreAuthorize("hasRole('USER')")
    public User user(Principal user) {
        return this.userService.findByUsername(user.getName());
    }
}
