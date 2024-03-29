package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.User;
import com.manageemployee.employeemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(username);
        System.out.println(username);
        System.out.println(username);
        System.out.println(username);
        System.out.println(username);
        System.out.println(username);
        System.out.println(username);
        System.out.println(username);

        if (user.isPresent()) {
            User user1 = user.get();
            System.out.println(user1.getUsername());
            System.out.println(user1.getUsername());
            System.out.println(user1.getUsername());
            System.out.println(user1.getUsername());
            System.out.println(user1.getUsername());
            System.out.println(user1.getUsername());
            System.out.println(user1.getUsername());
            System.out.println(user1.getPassword());
            System.out.println(user1.getPassword());
            System.out.println(user1.getPassword());
            System.out.println(user1.getPassword());
            System.out.println(user1.getPassword());
            System.out.println(user1.getPassword());

            return user1;
        }else throw new UsernameNotFoundException("Пользователь не найден!");
    }
}
