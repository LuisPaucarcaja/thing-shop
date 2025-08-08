package ProductMicroservice.common.constants;

public class GeneralConstants {

    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final int DEFAULT_PAGE_SIZE = 16; //for paginated response

    public static final int DEFAULT_PRODUCT_LIMIT = 10; //For limited and non-paginated responses
    public static final String ASC = "asc";

    public static final String ID_IN_PATH = "/{id}";

    public static final String FIELD_ID = "id";

    public static final int MIN_SEARCH_TERM_LENGTH = 3;
    public static final String SEARCH_TERM_MIN_LENGTH_MESSAGE = "The search term must be at least 3 characters long.";


}