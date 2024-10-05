package com.secure.docs.service;

import com.secure.docs.model.AppRole;
import com.secure.docs.model.Role;
import com.secure.docs.model.User;
import com.secure.docs.repository.RoleRepository;
import com.secure.docs.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId).
                orElseThrow(()-> new RuntimeException("user not found"));

        AppRole appRole= AppRole.valueOf(roleName);
        Role role = roleRepository.findByRoleName(appRole).
                orElseThrow(()-> new RuntimeException("Role not found"));

        user.setRole(role);
        userRepository.save(user);
    }

    public User getaUserById(Long id){
       var user= userRepository.findById(id)
               .orElseThrow(()->new RuntimeException("User not found"));
       return user;
    }


    public User findByUsername(String username) {
        var user = userRepository.findByUserName(username)
                .orElseThrow(()-> new RuntimeException("User not found"));
        return user;
    }

//    public UserDTO getUserById(Long id) {
//        User user = userRepository.findById(id).
//                orElseThrow(()-> new RuntimeException("User Not found"));
//        return convertToDto(user);
//    }
//
//    private UserDTO convertToDto(User user){
//        return new UserDTO(
//                user.getUserId(),
//                user.getUserName (),
//                user.getEmail(),
//                user.isAccountNonLocked(),
//                user.isAccountNonExpired(),
//                user.isCredentialsNonExpired(),
//                user.isEnabled(),
//                user.getCredentialsExpiryDate(),
//                user.getAccountExpiryDate(),
//                user.getTwoFactorSecret(),
//                user.isTwoFactorEnabled(),
//                user.getSignUpMethod(),
//                user.getRole(),
//                user.getCreatedDate(),
//                user.getUpdatedDate()
//        );
//    }


    public void updatePassword(Long userId, String password) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update password");
        }
    }



    public void updateAccountLockStatus(Long userId, boolean lock) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setAccountNonLocked(!lock);
        userRepository.save(user);
    }


    public void updateAccountExpiryStatus(Long userId, boolean expire) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setAccountNonExpired(!expire);
        userRepository.save(user);
    }


    public void updateAccountEnabledStatus(Long userId, boolean enabled) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setEnabled(enabled);
        userRepository.save(user);
    }


    public void updateCredentialsExpiryStatus(Long userId, boolean expire) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setCredentialsNonExpired(!expire);
        userRepository.save(user);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
