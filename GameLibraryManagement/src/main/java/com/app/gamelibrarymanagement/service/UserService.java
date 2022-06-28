package com.app.gamelibrarymanagement.service;

import com.app.gamelibrarymanagement.model.Game;
import com.app.gamelibrarymanagement.model.Role;
import com.app.gamelibrarymanagement.model.User;
import com.app.gamelibrarymanagement.repository.GameRepository;
import com.app.gamelibrarymanagement.repository.RoleRepository;
import com.app.gamelibrarymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    /**
     * This method is fetching all the customers from the database.
     */
    public List<User> getAllCustomers() {
        Optional<Role> role = roleRepository.findByName("ROLE_CUSTOMER");
        if (role.isPresent()) {
            return userRepository.getAllCustomers(role.get().getId());
        } else {
            throw new RuntimeException("There is no customer in the database");
        }
    }

    /**
     * This method is saving the user in the database.
     */
    public User saveUser(User user) {
        Optional<Role> role = roleRepository.findByName("ROLE_CUSTOMER");
        if (role.isPresent()) {
            user.setRole(role.get());
        } else {
            Role savedRole = new Role("ROLE_CUSTOMER");
            user.setRole(roleRepository.save(savedRole));
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * This method is updating the user in the database.
     *
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    /**
     * This method is finding the user by its username.
     */
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * This method is finding the user by its email.

     */
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    /**
     * This method is deleting the user by its ID.

     */
    public void deleteCustomer(Integer customerId) {
        Optional<User> user = userRepository.findById(customerId);
        if (user.isPresent()) {
            if (user.get().getBorrowedGamesList().size() > 0) {
                for (Game borrowedGame : user.get().getBorrowedGamesList()
                ) {
                    Game game = gameRepository.getById(borrowedGame.getId());
                    game.setQuantity(game.getQuantity() + 1);
                    gameRepository.save(game);

                    user.get().getBorrowedGamesList().remove(game);
                    updateUser(user.get());
                    if (user.get().getBorrowedGamesList().size() <= 0) {
                        break;
                    }
                }
            }

            if (user.get().getPurchasedGamesList().size() > 0) {
                for (Game purchasedGame : user.get().getPurchasedGamesList()
                ) {
                    Game game = gameRepository.getById(purchasedGame.getId());
                    game.setQuantity(game.getQuantity() + 1);
                    gameRepository.save(game);

                    user.get().getPurchasedGamesList().remove(game);
                    updateUser(user.get());
                    if (user.get().getPurchasedGamesList().size() <= 0) {
                        break;
                    }
                }
            }

            userRepository.delete(user.get());
        } else {
            throw new RuntimeException("There is no customer against this ID: " + customerId);
        }
    }


    public void createNewAdminOnRuntime() {
        Optional<User> admin = userRepository.findByUsername("admin");
        if (!admin.isPresent()) {
            User newAdmin = new User();
            newAdmin.setFirstName("admin");
            newAdmin.setLastName("admin");
            newAdmin.setAge(32);
            newAdmin.setAddress("Minnesota");
            newAdmin.setEmail("admin@gmail.com");
            newAdmin.setUsername("admin");
            newAdmin.setPassword(bCryptPasswordEncoder.encode("admin"));
            Optional<Role> role = roleRepository.findByName("ROLE_ADMIN");
            if (role.isPresent()) {
                newAdmin.setRole(role.get());
            } else {
                Role savedRole = new Role("ROLE_ADMIN");
                newAdmin.setRole(roleRepository.save(savedRole));
            }
            userRepository.save(newAdmin);
        }
    }

    public void createNewCustomersOnRuntime() {
        Role customerRole;
        Optional<Role> role = roleRepository.findByName("ROLE_CUSTOMER");
        if (!role.isPresent()) {
            customerRole = roleRepository.save(new Role("ROLE_CUSTOMER"));
        } else {
            customerRole = role.get();
        }

        Optional<User> customer1 = userRepository.findByUsername("customer1");
        if (!customer1.isPresent()) {
            User newCustomer1 = new User();
            newCustomer1.setFirstName("Fowzan");
            newCustomer1.setLastName("Issa");
            newCustomer1.setAge(23);
            newCustomer1.setAddress("Somalia");
            newCustomer1.setEmail("customer1@gmail.com");
            newCustomer1.setUsername("customer1");
            newCustomer1.setPassword(bCryptPasswordEncoder.encode("customer1"));
            newCustomer1.setRole(customerRole);
            userRepository.save(newCustomer1);
        }

        Optional<User> customer2 = userRepository.findByUsername("customer2");
        if (!customer2.isPresent()) {
            User newCustomer2 = new User();
            newCustomer2.setFirstName("Hayat");
            newCustomer2.setLastName("Mustafa");
            newCustomer2.setAge(24);
            newCustomer2.setAddress("Uganda");
            newCustomer2.setEmail("customer2@gmail.com");
            newCustomer2.setUsername("customer2");
            newCustomer2.setPassword(bCryptPasswordEncoder.encode("customer2"));
            newCustomer2.setRole(customerRole);
            userRepository.save(newCustomer2);
        }


        Optional<User> customer3 = userRepository.findByUsername("customer3");
        if (!customer3.isPresent()) {
            User newCustomer3 = new User();
            newCustomer3.setFirstName("Unaysa");
            newCustomer3.setLastName("Issa");
            newCustomer3.setAge(23);
            newCustomer3.setAddress("Canada");
            newCustomer3.setEmail("customer3@gmail.com");
            newCustomer3.setUsername("customer3");
            newCustomer3.setPassword(bCryptPasswordEncoder.encode("customer3"));
            newCustomer3.setRole(customerRole);
            userRepository.save(newCustomer3);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    user.get().getUsername(),
                    user.get().getPassword(),
                    getAuthority(user.get())
            );
        } else {
            throw new RuntimeException("User doesn't exists against username: " + username);
        }
    }

    /**
     * This method is providing the role of the user that we have in the database.
     */
    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        return authorities;
    }
}
