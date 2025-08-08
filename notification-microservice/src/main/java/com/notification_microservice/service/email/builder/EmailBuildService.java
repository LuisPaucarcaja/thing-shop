package com.notification_microservice.service.email.builder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailBuildService {

    @Value("${frontend.urls.activate-account}")
    private String activateAccountUrl;
    @Value("${frontend.urls.report-account}")
    private String reportAccountUrl;

    @Value("${app.logo-url}")
    private String logoUrl;

    public String buildVerificationEmailBody(String email, String tokenJwt) {
        StringBuilder bodyHtml = new StringBuilder();

        bodyHtml.append("<h2> Hello ").append(email).append("</h2>")
                .append("<p>Thank you for registering at 'ThingShop'.<br>")
                .append("Please click the link below to verify your email address and activate your account:</p><br>")
                .append("<a href='").append(activateAccountUrl).append(tokenJwt).append("'")
                .append(" style='padding:10px 20px; background-color:#4CAF50; color:white; text-decoration:none; border-radius:5px;'>")
                .append("Verify Email</a><br><br>")
                .append("<div>If you did not register with this email, <a href='")
                .append(reportAccountUrl).append(tokenJwt).append("'>report the account of the user who tried to register.</a> <br/>")
                .append("Best regards,<br> 'Things Shop' where everything you're looking for is in one place.</div>");

        return bodyHtml.toString();
    }

    public String wrapWithTemplate(String body) {
        return new StringBuilder()
                .append("<!DOCTYPE html>")
                .append("<html xmlns=\"http://www.w3.org/1999/xhtml\">")
                .append("<head>")
                .append("<meta charset=\"UTF-8\">")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
                .append("<title>Email</title>")
                .append("</head>")
                .append("<body>")
                .append("<br/><img src='").append(logoUrl).append("'/><br/>")
                .append(body)
                .append("</body>")
                .append("</html>")
                .toString();
    }

}
