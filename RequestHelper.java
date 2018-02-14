import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class RequestHelper {
    final static String WEB_SERVER = "WebServer/1.0";

    final static String SUPPORTED_METHODS = "GET, HEAD";

    public static String getStandardHeaders(String uri, boolean isHead) {
        StringBuilder b = new StringBuilder();

        b.append(getHeader(STATUS_CODE.OK));
        b.append(getDate());
        b.append(getLocation(uri));
        b.append(getServerInfo());
        b.append(getSupportedEntries());
        b.append(getContentLength(uri,isHead));
        b.append(getContentType(uri));
        b.append(getLastModified(uri));

        return b.toString();
    }

    public static String getFileContent(String uri) {
        try {
            return FileHandler.readFile(uri) + Constants.CRLF;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getLastModified(String uri) {
        return "Last-Modified: on, " + FileHandler.lastModified(uri) + Constants.CRLF;
    }

    public static String getContentLength(String uri,boolean isHead) {
        if (isHead || uri.equals("/empty.txt")) {
            return "Content-Length: 0" + Constants.CRLF;
        }
        return "Content-Length: 402" + Constants.CRLF;
    }

    public static String getContentType(String fileName) {
        return "Content-Type: " + contentType(fileName) + Constants.CRLF;
    }

    public static String getSupportedEntries() {
        return "Allow: " + SUPPORTED_METHODS+ Constants.CRLF;
    }

    public static String getServerInfo() {
        return "Server: " + WEB_SERVER + Constants.CRLF;
    }

    public static String getLocation(String uri) {
        return "Location: " + uri + Constants.CRLF;
    }

    public static boolean existingFile(String file) {
        return FileHandler.fileExists(file);
    }

    public static String getHeader(STATUS_CODE status_code) {
        return (Constants.HTTPVERSION + " ") + STATUS_CODE.toString(status_code) + Constants.CRLF;
    }


    public static String notImplemented() {
        return (Constants.HTTPVERSION + " ") + STATUS_CODE.toString(STATUS_CODE.NOT_IMPLEMENTED) + Constants.CRLF +
                getDate();
    }

    public static String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EE MMM d H:m:s z y");
        ZonedDateTime d = ZonedDateTime.now();
        String s = d.format(formatter);
        return "Date: " + s + Constants.CRLF;
    }

    public static String badRequest() {
        StringBuilder b = new StringBuilder();
        b.append(Constants.HTTPVERSION + " ").append(STATUS_CODE.toString(STATUS_CODE.BAD_REQUEST)).append(Constants.CRLF);
        b.append(getDate());
        return b.toString();
    }

    private static String contentType(String fileName) {
        if (fileName.toLowerCase().endsWith(".htm") ||
                fileName.toLowerCase().endsWith(".html")) {
            return "text/html";
        } else if (fileName.toLowerCase().endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.toLowerCase().endsWith(".jpg")) {
            return "image/jpeg";
        } else {
            return "application/octet-stream";
        }
    }

}
