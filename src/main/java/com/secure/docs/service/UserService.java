package com.secure.docs.service;

import com.secure.docs.dto.UserDTO;
import com.secure.docs.model.AppRole;
import com.secure.docs.model.Role;
import com.secure.docs.model.User;
import com.secure.docs.repository.RoleRepository;
import com.secure.docs.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

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

//    public User getaUserById(Long id){
//       return userRepository.findById(id).orElseThrow();
//    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).
                orElseThrow(()-> new RuntimeException("User Not found"));
        return convertToDto(user);
    }

    private UserDTO convertToDto(User user){
        return new UserDTO(
                user.getUserId(),
                user.getUserName (),
                user.getEmail(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getCredentialsExpiryDate(),
                user.getAccountExpiryDate(),
                user.getTwoFactorSecret(),
                user.isTwoFactorEnabled(),
                user.getSignUpMethod(),
                user.getRole(),
                user.getCreatedDate(),
                user.getUpdatedDate()
        );
    }
}
