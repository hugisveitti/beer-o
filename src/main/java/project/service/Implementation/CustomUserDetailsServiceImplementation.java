package project.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.persistence.entities.Role;
import project.persistence.repositories.RoleRepository;
import project.persistence.entities.User;
import project.persistence.repositories.UserRepository;

import org.springframework.security.core.AuthenticationException;
import project.service.CustomUserDetailsService;

import java.util.*;

@Service
public class CustomUserDetailsServiceImplementation implements CustomUserDetailsService {

    private RoleRepository roleRepository;
    private UserRepository repository;
    private PasswordEncoder passwordEncoder;

    // Dependency Injection
    @Autowired
    public CustomUserDetailsServiceImplementation(UserRepository repository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = repository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("Username not found");
        }
        return user;
    }


    //Paeling ad setja thetta i ser classa
    //Exception sent if there exsitis a user with this username
    public class UserExsitsException extends AuthenticationException {
        public UserExsitsException(final String msg){
            super(msg);
        }
    }

    public User save(User user) throws UserExsitsException {
        // Save the user that we received from the form
        Role role = roleRepository.findByRole("ROLE_USER");
        Set<Role> newRoles = new HashSet<>();
        newRoles.add(role);
        user.setRoles(newRoles);
        user.setEnabled(true);
        //check if username is already in database
        if(repository.findByUsername(user.getUsername()) != null){
            throw new UserExsitsException("Username taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    public void delete(User user) {
        repository.delete(user);
    }


    public List<User> findAll() {
        return repository.findAll();
    }


    @Override
    public User findOne(Long id) {
        return repository.findOne(id);
    }


    //paeling ad eyda thessu og nota bara loadByUsername, eda lata thetta bara skila id hja usernum eda ehv
    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }

}
