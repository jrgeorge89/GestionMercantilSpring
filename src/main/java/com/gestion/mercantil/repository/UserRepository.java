
package com.gestion.mercantil.repository;

import com.gestion.mercantil.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    @Procedure(procedureName = "test.authentication_pkg.register_user")
    void registerUser(@Param("p_username") String username, 
                      @Param("p_firstname") String firstname,
                      @Param("p_lastname") String lastname,
                      @Param("p_country") String country,
                      @Param("p_password") String password,
                      @Param("p_role") String role);

    @Query(value = "SELECT test.authentication_pkg.login_user(:username, :password) FROM dual", nativeQuery = true)
    @Transactional(readOnly = true)
    Optional<String> loginUser(@Param("username") String username, 
                               @Param("password") String password);

    Optional<User> findByUsername(String username);
}
