package com.user_service.common.exception;

import static com.user_service.common.constants.ErrorMessageConstants.NOT_RESULTS_FOUND_FOR_FIELD;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format(NOT_RESULTS_FOUND_FOR_FIELD, resourceName, fieldName, fieldValue));
    }
}

