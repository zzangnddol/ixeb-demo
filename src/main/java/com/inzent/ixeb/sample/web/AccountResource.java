package com.inzent.ixeb.sample.web;

import com.inzent.ixeb.sample.dao.UserDao;
import com.inzent.ixeb.sample.model.User;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/account")
public class AccountResource {
    private UserDao userDao;

    public AccountResource(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public Map<String, String> currentAccount(Principal principal) {
        String username = principal.getName();
        User user = userDao.get(username);
        if (user == null) return null;
        Map<String, String> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("name", user.getName());
        return map;
    }

    @PostMapping(path = "/find")
    public User findAccount(User user) {
        System.out.println("######> " + user.getName() + ", " + user.getCellPhone() + ", " + user.getEmail());
        return userDao.find(user);
    }
}
