package MicroservicePago.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    public static final String NOT_RESULTS_FOUND_MESSAGE = "Not results found for %s with %s: %s";
    public ResourceNotFoundException(String entityName, String fieldName, Object fieldValue) {
        super(String.format(NOT_RESULTS_FOUND_MESSAGE, entityName, fieldName, fieldValue));
    }
}