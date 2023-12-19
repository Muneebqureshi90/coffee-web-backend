package com.example.coffee.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtilz {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOTPByEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify Your OTP");
        String verificationLink = "https://yourwebsite.com/verify-account?otp=" + otp; // Replace with your actual verification link

        mimeMessageHelper.setText("""
        <div>
          <a href="http://localhost:8080/verify-account?email=%s&otp=%s" target="_blank">click link to verify</a>
        </div>
        """.formatted(email, otp), true);

        javaMailSender.send(mimeMessage);
    }

    public void sendNewPasswordByEmail(String email, String newPassword) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("New Password");

        String emailContent = String.format(
                "Hello,\n\n"
                        + "Your new password is: %s\n\n"
                        + "Please use this password to log in.\n\n"
                        + "If you didn't request a new password, please contact us immediately.\n\n"
                        + "Best regards,\nYour Company",
                newPassword
        );

        mimeMessageHelper.setText(emailContent, true);

        javaMailSender.send(mimeMessage);
    }

//     for forget password

    public void sendEmail(String email, String subject, String emailContent) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(emailContent, true);

        javaMailSender.send(mimeMessage);
    }

}
