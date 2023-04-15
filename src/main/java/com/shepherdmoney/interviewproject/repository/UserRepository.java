package com.shepherdmoney.interviewproject.repository;

import com.shepherdmoney.interviewproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Crud Repository to store User classes
 */
@Repository("UserRepo")
public interface UserRepository extends JpaRepository<User, Integer> {

    // check if user exists given username
    @Query(value = "SELECT * FROM MY_USER WHERE name = :name", nativeQuery = true)
    User findUserByName(@Param("name") String name);

    // check if user exists given id
    @Query(value = "SELECT * FROM MY_USER WHERE id = :userId", nativeQuery = true)
    User findUserById(int userId);

}
