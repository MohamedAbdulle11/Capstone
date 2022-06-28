package com.app.gamelibrarymanagement;

import com.app.gamelibrarymanagement.model.Role;
import com.app.gamelibrarymanagement.model.User;
import com.app.gamelibrarymanagement.repository.RoleRepository;
import com.app.gamelibrarymanagement.repository.UserRepository;
import com.app.gamelibrarymanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class UserServiceTests {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Test
    public void saveUserTest() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_CUSTOMER");

        User user = new User();
        user.setId(1);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setAge(23);
        user.setAddress("Minnesota");
        user.setEmail("test@gmail.com");
        user.setUsername("test_user");
        user.setPassword("password");


        when(roleRepository.findByName("ROLE_CUSTOMER")).thenReturn(Optional.of(role));
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(user, userService.saveUser(user));
    }


    @Test
    public void getAllCustomersTest() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_CUSTOMER");

        User user = new User();
        user.setId(1);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setAge(23);
        user.setAddress("Minnesota");
        user.setEmail("test@gmail.com");
        user.setUsername("test_user");
        user.setPassword("password");

        when(roleRepository.findByName("ROLE_CUSTOMER")).thenReturn(Optional.of(role));
        when(userRepository.getAllCustomers(1)).thenReturn(Collections.singletonList(user));
        assertEquals(Collections.singletonList(user), userService.getAllCustomers());
    }

    @Test
    public void updateUserTest() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_CUSTOMER");

        User user = new User();
        user.setId(1);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setAge(25);
        user.setAddress("Minnesota");
        user.setEmail("test@gmail.com");
        user.setUsername("test_user");
        user.setPassword("password");
        user.setRole(role);

        when(userRepository.save(user)).thenReturn(user);
        assertEquals(user, userService.updateUser(user));
    }

    @Test
    public void findUserByUsernameTest() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_CUSTOMER");

        User user = new User();
        user.setId(1);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setAge(23);
        user.setAddress("Minnesota");
        user.setEmail("test@gmail.com");
        user.setUsername("test_user");
        user.setPassword("password");
        user.setRole(role);

        when(userRepository.findByUsername("test_user")).thenReturn(Optional.of(user));
        assertEquals(Optional.of(user), userService.findUserByUsername("test_user"));
    }

    @Test
    public void findUserByEmailTest() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_CUSTOMER");

        User user = new User();
        user.setId(1);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setAge(23);
        user.setAddress("Minnesota");
        user.setEmail("test@gmail.com");
        user.setUsername("test_user");
        user.setPassword("password");
        user.setRole(role);

        when(userRepository.findUserByEmail("test@gmail.com")).thenReturn(Optional.of(user));
        assertEquals(Optional.of(user), userService.findUserByEmail("test@gmail.com"));
    }

}
