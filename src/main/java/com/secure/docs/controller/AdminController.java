package com.secure.docs.controller;

import com.secure.docs.model.User;
import com.secure.docs.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {
    private final UserService userService;


    //get all users
    @PreAuthorize("hasRole('ROLE_ADMIN')") //only the users with ADMIN_ROLE can access this end point
    @GetMapping("/getusers")
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

    //change user role
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update-role")
    public ResponseEntity<String> updateUserRole(@RequestParam Long userId, @RequestParam String roleName){
        userService.updateUserRole(userId,roleName);
        return ResponseEntity.ok("User role updated");
    }


    //    //find user by id
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getaUserById(@PathVariable Long id){
        return new ResponseEntity<>(userService.getaUserById(id),HttpStatus.OK);
    }


}
