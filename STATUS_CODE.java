/**
 * The enum to represent the different status codes.
 */
public enum STATUS_CODE {
    OK, BAD_REQUEST, NOT_FOUND, INTERNAL_SERVER_ERROR, NOT_IMPLEMENTED;

    public static String toString(STATUS_CODE code) {
        switch (code) {
            case OK:
                return "200 OK";
            case BAD_REQUEST:
                return "400 Bad Request";
            case NOT_FOUND:
                return "404 Not Found";
            case INTERNAL_SERVER_ERROR:
                return "500 Internal Server Error";
            default:
                return "501 Not Implemented";
        }
    }
}
