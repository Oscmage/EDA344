/**
 * The class that handles get requests
 */
public class GetRequest {

    public static String handle(String uri) {
        StringBuilder b = new StringBuilder();

        if (!RequestHelper.existingFile(uri)) {
            return b.append(RequestHelper.getHeader(STATUS_CODE.NOT_FOUND)).toString() + Constants.CRLF;
        }

        b.append(RequestHelper.getStandardHeaders(uri, false));
        b.append(RequestHelper.getFileContent(uri));

        return b.toString();
    }
}
