package com.secure.docs.service;

import com.secure.docs.model.AppRole;
import com.secure.docs.model.PasswordResetToken;
import com.secure.docs.model.Role;
import com.secure.docs.model.User;
import com.secure.docs.repository.PasswordResetTokenRepository;
import com.secure.docs.repository.RoleRepository;
import com.secure.docs.repository.UserRepository;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Value("${frontend.url}")
    String frontendUrl;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

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


    //methods of perform admin actions on users
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

    //validate user email and generate a token to reset the password
    public void generatePasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User Not Found"));

        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(Duration.ofHours(24));
        PasswordResetToken resetToken = new PasswordResetToken(token,expiryDate,user);
        passwordResetTokenRepository.save(resetToken);

        String resetUrl = frontendUrl +"/reset-password?token=" + token;

        //send the email to the user
        emailService.sendPasswordResetEmail(user.getEmail(), resetUrl);
    }


    //validate the token and reset with the new password
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken= passwordResetTokenRepository.findByToken(token)
                .orElseThrow(()-> new RuntimeException("invalid password reset token"));

        if(resetToken.isUsed()){
            throw new RuntimeException("password reset token has already been used");
        }

        if(resetToken.getExpiryDate().isBefore(Instant.now())){
            throw new RuntimeException("password reset token is expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);

    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User registerUser(User newUser) {
        if(newUser.getPassword() != null)
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        return userRepository.save(newUser);
    }
}
