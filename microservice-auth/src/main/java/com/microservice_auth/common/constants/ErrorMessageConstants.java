package com.microservice_auth.common.constants;

public final class ErrorMessageConstants {


    public static final String NOT_RESULTS_FOUND_FOR_FIELD = "Not results found for %s with %s: %s";
    public static final String TOKEN_EXPIRED = "Token expired";

    public static final String INVALID_TOKEN = "Invalid token";

    public static final String ACCOUNT_ALREADY_ACTIVE = "Account already active";

    public static final String EMAIL_ALREADY_REGISTERED = "Email already registered, try another one";
    public static final String EMAIL_REGISTERED_BUT_INACTIVE = "The email is already registered but the account is not active.";

    public static final String ACCOUNT_PENDING_VERIFICATION = "Your account is pending verification. Please check your email.";
    public static final String ACCOUNT_SUSPENDED = "Your account is suspended. Please contact support for assistance.";

    public static final String ACCOUNT_BANNED = "Your account has been banned due to violations of our policies.";
    public static final String INVALID_ACCOUNT_STATUS = "Invalid account status.";

    public static final String FILE_SIZE_EXCEEDS_LIMIT_MESSAGE = "File size exceeds the maximum limit";

}
