package project.service;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import project.persistence.entities.User;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * By extending {@link UserDetailsService} we can use the method loadByUsername
 * which is used in authentication
 */

public interface CustomUserDetailsService extends UserDetailsService {

    /**
     * Save a {@link User}
     * @param user {@link User} to be saved
     * @return {@link User} that was saved
     * @throws AuthenticationException if a user exists with the given username
     */
    User save(User user) throws AuthenticationException;

    /**
     * Delete {@link User}
     * @param user {@link User} to be deleted
     */
    void delete(User user);

    /**
     * Get all {@link User}s
     * @return A list of {@link User}s
     */
    List<User> findAll();


    /**
     * Find a {@link User} based on {@link Long id}
     * @param id {@link Long}
     * @return A {@link User} with {@link Long id}
     */
    User findOne(Long id);

    /**
     * Find all {@link User}s with {@link String username}
     * @param username {@link String}
     * @return A {@link User} with the {@link String username} passed
     */
    //maybe delete this, because UserDetailsService has method loadByUsername
    User findByUsername(String username) throws UsernameNotFoundException;


}
