package com.RestTime.RestTime.service;

public interface EmailService {
    void sendPasswordResetEmail(String toEmail, String resetLink);

}
