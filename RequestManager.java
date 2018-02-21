/**
 * This class purpose is to do the general split of different request and let their respective handler handle the request.
 */
public class RequestManager {
    private final static int HEADER_WITHOUT_USER_AGENT= 3;
    private final static int HEADER_WITH_USER_AGENT = 5;
    private final static String USER_AGENT = "User-Agent:";

    public static String handleRequest(String request) {
        String[] spaces = request.split(" ");

        // Either we have 3 or 5 elements in the array if valid
        if (spaces.length == HEADER_WITHOUT_USER_AGENT || spaces.length == HEADER_WITH_USER_AGENT) {
            String method = spaces[0];
            String argument = spaces[1];
            String version = spaces[2];

            if (spaces.length == HEADER_WITH_USER_AGENT) {
                String userAgent = spaces[3];
                if (!userAgent.equals(USER_AGENT)) {
                    return RequestHelper.badRequest();
                }
            }

            //INCORRECT HTTP VERSION
            if (!version.equals(Constants.HTTPVERSION)) {
                return RequestHelper.badRequest();
            }

            // Argument (file) to look for requires a '/' in the start otherwise it is invalid
            if (argument.length() < 1 || argument.charAt(0) != '/') {
                return RequestHelper.badRequest();
            }

            switch (method) {
                case HTTP_METHOD.GET:
                    return GetRequest.handle(argument);
                case HTTP_METHOD.HEAD:
                    return HeadRequest.handle(argument);
                case HTTP_METHOD.POST:
                    return RequestHelper.notImplemented();
                case HTTP_METHOD.PUT:
                    return RequestHelper.notImplemented();
                case HTTP_METHOD.DELETE:
                    return RequestHelper.notImplemented();
                case HTTP_METHOD.LINK:
                    return RequestHelper.notImplemented();
                case HTTP_METHOD.UNLINK:
                    return RequestHelper.notImplemented();
                default:
                    return RequestHelper.badRequest();
            }
        } else {
            return RequestHelper.badRequest();
        }
    }


}
