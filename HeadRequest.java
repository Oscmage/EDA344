/**
 * The class that handles head requests.
 */
public class HeadRequest {

    public static String handle(String uri) {
        if (!RequestHelper.existingFile(uri)) {
            return RequestHelper.getHeader(STATUS_CODE.NOT_FOUND);
        }
        return RequestHelper.getStandardHeaders(uri, true);
    }
}
