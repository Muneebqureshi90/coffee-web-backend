package com.example.coffee.serviceImpl;

import com.example.coffee.config.AppConstants;
import com.example.coffee.dto.UserDto;
import com.example.coffee.entity.EmailOtp;
import com.example.coffee.entity.Role;
import com.example.coffee.entity.User;
import com.example.coffee.entity.UserTwilioOtp;
import com.example.coffee.expections.ResourceNotFoundException;
import com.example.coffee.expections.UserException;
import com.example.coffee.expections.UserNotFoundException;
import com.example.coffee.repository.EmailOtpRepository;
import com.example.coffee.repository.RoleRepository;
import com.example.coffee.repository.UserRepository;
import com.example.coffee.repository.UserTwilioOp;
import com.example.coffee.services.UserService;
import com.example.coffee.twillo.TwilioService;
import com.example.coffee.util.EmailUtilz;
import com.example.coffee.util.OTP;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailOtpRepository emailOtpRepository;
    @Autowired
    private UserTwilioOp userTwilioOtpRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TwilioService twilioService;

    @Autowired
    private EmailUtilz emailUtilz;

    @Autowired
    private OTP otpGenerator;

//    @Override
//    public UserDto registerUser(UserDto userDto) {
//        try {
//            // Step 1: Generate OTP
//            String emailOtp = otpGenerator.generateOtp();
//            String phoneOtp = otpGenerator.generateOtp();
//
//            // Step 2: Map UserDto to User entity
//            User user = modelMapper.map(userDto, User.class);
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
//            user.setCreatedAt(LocalDateTime.now());
//
//            // Step 3: Save User entity without assigning a role for now
//            // User entity is saved temporarily for OTP verification
//            User savedUser = userRepository.save(user);
//
//            // Step 4: Send OTP via Email
//            try {
//                emailUtilz.sendOTPByEmail(savedUser.getEmail(), emailOtp);
//                // Log success or any relevant information
//                logger.info("OTP sent via email successfully to {}", savedUser.getEmail());
//            } catch (MessagingException e) {
//                // Log the exception or handle it as appropriate for your application
//                logger.error("Failed to send OTP via email to {}", savedUser.getEmail(), e);
//                // Rollback the transaction by deleting the temporarily saved user
//                userRepository.delete(savedUser);
//                throw new RuntimeException("Failed to send OTP via email", e);
//            }
//
//            // Step 5: Send OTP via Twilio for Phone
//            try {
//                twilioService.sendOtp(savedUser.getPhoneNumber(), phoneOtp);
//                // Log success or any relevant information
//                logger.info("OTP sent via Twilio successfully to {}", savedUser.getPhoneNumber());
//            } catch (Exception e) {
//                // Log the exception or handle it as appropriate for your application
//                logger.error("Failed to send OTP via Twilio to {}", savedUser.getPhoneNumber(), e);
//                // Rollback the transaction by deleting the temporarily saved user
//                userRepository.delete(savedUser);
//                throw new RuntimeException("Failed to send OTP via Twilio", e);
//            }
//
//            // Perform OTP verification here
//            // If verification fails, rollback the transaction by deleting the temporarily saved user
//
//            // If OTP verification is successful, assign a role and save the user entity
//            Role role = roleRepository.findById(AppConstants.NORMAL_USER)
//                    .orElseThrow(() -> new RuntimeException("Role not found"));
//            savedUser.setRole(role);
//            savedUser = userRepository.save(savedUser);
//
//            // Map the saved user to UserDto and return
//            UserDto registeredUserDto = modelMapper.map(savedUser, UserDto.class);
//
//            // Optionally, you can include additional information in the response, such as the generated OTPs
//            registeredUserDto.setEmailOtp(emailOtp);
//            registeredUserDto.setTwilioOtp(phoneOtp);
//
//            return registeredUserDto;
//        } catch (Exception e) {
//            // Log the exception or handle it as appropriate for your application
//            logger.error("Error during user registration", e);
//            throw new RuntimeException("Error during user registration", e);
//        }
//    }


    @Override
    public UserDto registerUser(UserDto userDto) {
        try {
            // Step 1: Generate OTPs
            String emailOtp = otpGenerator.generateOtp();
//            String phoneOtp = otpGenerator.generateOtp();

            logger.info("Generated Email OTP: {}", emailOtp);
//            logger.info("Generated Phone OTP: {}", phoneOtp);

            // Step 2: Map UserDto to User entity
            User user = modelMapper.map(userDto, User.class);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreatedAt(LocalDateTime.now());

            // Set the generated OTPs to the User entity
            user.setEmailOtp(emailOtp);
//            user.setTwilioOtp(phoneOtp);

            // Save only the necessary fields in the User entity without assigning a role for now
            // User entity is saved temporarily for OTP verification
            User savedUser = userRepository.save(user);

            // Step 4: Send OTPs via Email and Twilio for Phone
            try {
                logger.info("Sending Email OTP to {}", savedUser.getEmail());
                emailUtilz.sendOTPByEmail(savedUser.getEmail(), emailOtp);
                logger.info("Email OTP sent successfully");

//                logger.info("Sending Phone OTP to {}", savedUser.getPhoneNumber());
//                twilioService.sendOtp(savedUser.getPhoneNumber(), phoneOtp);
//                logger.info("Phone OTP sent successfully");

                // Log success or any relevant information
                logger.info("OTPs sent successfully to {} (Email) and {} (Phone)", savedUser.getEmail(), savedUser.getPhoneNumber());
            } catch (Exception e) {
                // Log the exception or handle it as appropriate for your application
                logger.error("Failed to send OTPs to {} (Email) and {} (Phone)", savedUser.getEmail(), savedUser.getPhoneNumber(), e);

                // Rollback the transaction by deleting the temporarily saved user
                userRepository.delete(savedUser);

                throw new RuntimeException("Failed to send OTPs", e);
            }

            // Step 5: Perform OTP verification here
            logger.info("Verifying Email OTP for {}", savedUser.getEmail());
            boolean isEmailOtpValid = isOtpValid(savedUser.getEmailOtp(), emailOtp);
            logger.info("Email OTP verification result: {}", isEmailOtpValid);

//            logger.info("Verifying Phone OTP for {}", savedUser.getPhoneNumber());
//            boolean isPhoneOtpValid = isOtpValid(savedUser.getTwilioOtp(), phoneOtp);
//            logger.info("Phone OTP verification result: {}", isPhoneOtpValid);

            if (isEmailOtpValid
//                    || isPhoneOtpValid
            ) {
                // If OTP verification is successful, assign a role and save the user entity
                Role role = roleRepository.findById(AppConstants.NORMAL_USER)
                        .orElseThrow(() -> new RuntimeException("Role not found"));

                savedUser.setRole(role);
                savedUser = userRepository.save(savedUser);

                // Map the saved user to UserDto and return
                UserDto registeredUserDto = modelMapper.map(savedUser, UserDto.class);

                // Optionally, you can include additional information in the response
                registeredUserDto.setEmailOtp(emailOtp);
//                registeredUserDto.setTwilioOtp(phoneOtp);

                return registeredUserDto;
            } else {
                // Rollback the transaction by deleting the temporarily saved user
                userRepository.delete(savedUser);

                throw new RuntimeException("OTP verification failed");
            }

        } catch (Exception e) {
            // Log the exception or handle it as appropriate for your application
            logger.error("Error during user registration", e);
            throw new RuntimeException("Error during user registration", e);
        }
    }


    @Override
    public UserDto createUser(UserDto userDto) throws UserException {
        User user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saveUser = userRepository.save(user);
        return modelMapper.map(saveUser, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());

        return userDtos;
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        this.userRepository.deleteById(userId);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        // Update the user's information with the values from the userDto
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setPhoneNumber(userDto.getPhoneNumber());


        User save = this.userRepository.save(user);


        return modelMapper.map(save, UserDto.class);
    }

    @Override
    public UserDto getUserById(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return modelMapper.map(user, UserDto.class);
        } else {
            // Handle the case where the user is not found, e.g., throw an exception or return a default value
            throw new ResourceNotFoundException("User", "id", userId.toString());
        }
    }


    @Override
    public boolean verifyEmailOtp(String email, String otp) {
        // Retrieve saved OTP from a more secure and persistent storage (e.g., database)
        // For demonstration purposes, we'll use a hardcoded value
        String savedOtp = retrieveSavedOtpByEmail(email); // Replace with the actual retrieval logic

        // Perform OTP verification logic (compare with the provided OTP)
        boolean isOtpValid = savedOtp != null && savedOtp.equals(otp);

        // If user verification is successful, delete the OTP from the database
        if (isOtpValid) {
            deleteOtpByEmail(email);
        }

        return isOtpValid;
    }

//    @Override
//    public boolean verifyTwilioOtp(String phoneNumber, String otp) {
//        // Retrieve saved OTP from a more secure and persistent storage (e.g., database)
//        // For demonstration purposes, we'll use a hardcoded value
//        String savedOtp = retrieveSavedOtpByPhoneNumber(phoneNumber); // Replace with the actual retrieval logic
//
//        // Perform OTP verification logic (compare with the provided OTP)
//        boolean isOtpValid = savedOtp != null && savedOtp.equals(otp);
//
//        // If user verification is successful, delete the OTP from the database
//        if (isOtpValid) {
//            deleteOtpByPhoneNumber(phoneNumber);
//        }
//
//        return isOtpValid;
//    }

    // Method to retrieve saved OTP by email from the database
    private String retrieveSavedOtpByEmail(String email) {
        // Replace with your actual database retrieval logic
        // For demonstration purposes, we'll use a hardcoded value

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Assuming you want the first OTP in the list
            if (!user.getOtps().isEmpty()) {
                return user.getOtps().get(0).getOtp();
            }
        }

        return null; // User not found for the given email or no OTP available
    }

    // Method to retrieve saved OTP by phone number from the database
//    private String retrieveSavedOtpByPhoneNumber(String phoneNumber) {
//        // Replace with your actual database retrieval logic
//        // For demonstration purposes, we'll use a hardcoded value
//
//        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//
//            // Assuming you want the first Twilio OTP in the list
//            if (!user.getTwilioOtps().isEmpty()) {
//                return user.getTwilioOtps().get(0).getOtp();
//            }
//        }
//
//        return null; // User not found for the given phone number or no Twilio OTP available
//    }


    // Method to delete OTP by email from the database
    private void deleteOtpByEmail(String email) {
        // Replace with your actual database deletion logic
        // For demonstration purposes, we'll use repository methods

        Optional<User> userOptional = userRepository.findByEmail(email);
        userOptional.ifPresent(user -> {
            // Assuming you have a method in your UserRepository to delete by email
            userRepository.deleteByEmail(email);
            System.out.println("Deleted OTP for email: " + email);
        });
    }

    // Method to delete OTP by phone number from the database
//    private void deleteOtpByPhoneNumber(String phoneNumber) {
//        // Replace with your actual database deletion logic
//        // For demonstration purposes, we'll use repository methods
//
//        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);
//        userOptional.ifPresent(user -> {
//            // Assuming you have a method in your UserRepository to delete by phone number
//            userRepository.deleteByPhoneNumber(phoneNumber);
//            System.out.println("Deleted OTP for phone number: " + phoneNumber);
//        });
//    }


    private final Map<String, String> resetTokens = new ConcurrentHashMap<>();

    // ... (existing code)

    @Override
    public void sendForgetPasswordEmail(String email) {
        String resetToken = generateResetToken();
        saveResetToken(email, resetToken);

        String resetLink = "https://yourapp.com/reset-password?token=" + resetToken;
        String emailContent = "Dear User, <br><br>"
                + "To reset your password, please click on the following link: <br>"
                + "<a href='" + resetLink + "'>" + resetLink + "</a>";

        try {
            emailUtilz.sendEmail(email, "Password Reset", emailContent);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send password reset email to " + email, e);
        }
    }

    @Override
    public boolean verifyForgetPasswordOtp(String email, String otp) {
        String savedOtp = retrieveResetToken(email);

        boolean otpVerified = savedOtp != null && savedOtp.equals(otp);

        if (otpVerified) {
            deleteResetToken(email);
        }

        return otpVerified;
    }

    private String generateResetToken() {
        // Use a UUID for simplicity, but consider using a more secure token generation method in production
        return UUID.randomUUID().toString();
    }

    private void saveResetToken(String email, String resetToken) {
        // Save the reset token in the in-memory map
        resetTokens.put(email, resetToken);
    }

    private String retrieveResetToken(String email) {
        // Retrieve the reset token from the in-memory map
        return resetTokens.get(email);
    }

    private void deleteResetToken(String email) {
        // Remove the reset token from the in-memory map
        resetTokens.remove(email);
        System.out.println("Deleted reset token for email: " + email);
    }


    @Override
    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        userRepository.save(user);

        System.out.println("Password reset for user with email: " + email);
    }


    @Override
    public String regenerateEmailOtp(String email) {
        // Generate a new OTP
        String newOtp = otpGenerator.generateOtp();

        // Save the new OTP (you might want to store this in a more secure and persistent way, like a database)
        saveEmailOtp(email, newOtp);

        // Return the new OTP
        return newOtp;
    }

//    @Override
//    public String regenerateTwilioOtp(String phoneNumber) {
//        // Generate a new OTP
//        String newOtp = otpGenerator.generateOtp();
//
//        // Save the new OTP (you might want to store this in a more secure and persistent way, like a database)
//        saveTwilioOtp(phoneNumber, newOtp);
//
//        // Return the new OTP
//        return newOtp;
//    }

    // Method to save Email OTP (replace with your actual storage logic)
    private void saveEmailOtp(String email, String otp) {
        // For demonstration purposes, we'll use a map. In a real application, consider using a database.
        // Also, ensure proper security measures for storing sensitive information.

        // Assuming you have a method in your UserRepository to update or save the OTP
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setEmailOtp(otp);
            userRepository.save(user);
            System.out.println("Saved Email OTP for email: " + email);
        });
    }

    // Method to save Twilio OTP (replace with your actual storage logic)
//    private void saveTwilioOtp(String phoneNumber, String otp) {
//        // For demonstration purposes, we'll use a map. In a real application, consider using a database.
//        // Also, ensure proper security measures for storing sensitive information.
//
//        // Assuming you have a method in your UserRepository to update or save the OTP
//        userRepository.findByPhoneNumber(phoneNumber).ifPresent(user -> {
//            user.setTwilioOtp(otp);
//            userRepository.save(user);
//            System.out.println("Saved Twilio OTP for phone number: " + phoneNumber);
//        });
//    }

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    public void sendLoginOtp(String email) {
        // Generate OTP
        String otp = otpGenerator.generateOtp();

        // Save OTP for verification
        saveLoginOtp(email, otp);

        // Send OTP via your preferred method (e.g., email, Twilio, etc.)
        sendOtpViaEmail(email, otp);
    }

    private void saveLoginOtp(String email, String otp) {
        try {
            // Retrieve the user by email
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

            // Delete existing OTP entries for the user
//            emailOtpRepository.deleteByUserId(user.getId()); // Corrected to use user.getId()

            userRepository.findByEmailWithOtps(otp);

            // Create a new EmailOtp entity
//            EmailOtp emailOtpEntity = new EmailOtp();
//            emailOtpEntity.setOtp(otp);
//            emailOtpEntity.setCreationTime(LocalDateTime.now());
//            emailOtpEntity.setUser(user);

            user.setEmailOtp(otp);

            // Save the EmailOtp entity
//            emailOtpRepository.save(emailOtpEntity);
            userRepository.save(user);

            logger.info("Saved Login OTP for email {}: {}", email, otp);
        } catch (UserNotFoundException e) {
            // Log the exception or handle it as appropriate for your application
            logger.error("Failed to save Login OTP. User not found with email: {}", email, e);
            throw new RuntimeException("Failed to save Login OTP", e);
        }
    }


    private void sendOtpViaEmail(String email, String otp) {
        // Send the OTP via email using your email utility class
        try {
            emailUtilz.sendOTPByEmail(email, otp);
            logger.info("Sent OTP via email to {}", email);
        } catch (MessagingException e) {
            // Log the exception or handle it as appropriate for your application
            logger.error("Failed to send OTP via email to {}", email, e);
            throw new RuntimeException("Failed to send OTP via email", e);
        }
    }

    @Override
    public boolean verifyLoginOtp(String email, String otp) {
        // Retrieve saved OTP from a more secure and persistent storage (e.g., database)
        String savedOtp = retrieveSavedOtpByEmail(email);

        // Perform OTP verification logic (e.g., compare with the provided OTP)
        boolean isOtpValid = savedOtp != null && savedOtp.equals(otp);

        // If user verification is successful, delete the OTP from the database
        if (isOtpValid) {
            deleteOtpByEmail(email);
        }

        return isOtpValid;
    }

    // New method to check if OTP is valid within a time limit
    private boolean isOtpValid(String savedOtp, String enteredOtp) {
        if (savedOtp != null && savedOtp.equals(enteredOtp)) {
            // Check if the OTP is still valid within a time limit (e.g., 60 seconds)
            LocalDateTime otpTimestamp = retrieveOtpTimestamp(); // Implement your logic to retrieve the OTP timestamp
            LocalDateTime currentTimestamp = LocalDateTime.now();
            Duration duration = Duration.between(otpTimestamp, currentTimestamp);
            long secondsRemaining = 60 - duration.getSeconds();

            // Check if the OTP is valid within the time limit
            return secondsRemaining > 0;
        }
        return false;
    }


    // Implement this method to retrieve the timestamp when the OTP was generated
    private LocalDateTime retrieveOtpTimestamp() {
        // Replace with your actual implementation
        // This could involve retrieving the timestamp from a database or another storage mechanism
        // For demonstration purposes, return the current timestamp
        return LocalDateTime.now();
    }
    private User convertToUserEntity(UserDto userDto) {
        User userEntity = modelMapper.map(userDto, User.class);
        return userEntity;
    }

    private UserDto convertToUserDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }
}