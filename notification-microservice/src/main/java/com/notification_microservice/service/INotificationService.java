package com.notification_microservice.service;


public interface INotificationService {

    void sendRegistrationVerificationEmail(String email, String token);


}
