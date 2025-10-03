package com.notification_service.service;


public interface INotificationService {

    void sendRegistrationVerificationEmail(String email, String token);


}
