package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.CreateUserPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    // TODO: wire in the user repository (~ 1 line)
    UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/user")
    public ResponseEntity<Integer> createUser(@RequestBody CreateUserPayload payload) {
        // TODO: Create an user entity with information given in the payload, store it in the database
        //       and return the id of the user in 200 OK response
        // check if the user already exists
        if(userRepository.findUserByName(payload.getName()) != null) {
            return ResponseEntity.status(400).body(-1);
        }
        User user = new User();
        user.setName(payload.getName());
        user.setEmail(payload.getEmail());
        userRepository.save(user);
        return ResponseEntity.status(200).body(user.getId());
    }


    @DeleteMapping("/user")
    @Transactional
    public ResponseEntity<String> deleteUser(@RequestParam int userId) {
        // TODO: Return 200 OK if a user with the given ID exists, and the deletion is successful
        //       Return 400 Bad Request if a user with the ID does not exist
        //       The response body could be anything you consider appropriate
        if(userRepository.findUserById(userId) == null) {
            return ResponseEntity.status(400).body("This user doesn't exist!");
        }
        userRepository.deleteById(userId);
        return ResponseEntity.status(200).body("Delete user successfully!");
    }
}
