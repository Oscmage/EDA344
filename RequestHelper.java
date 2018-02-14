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
        b.append(getContentLength(isHead));
        b.append(getContentType(uri));
        b.append(getLastModified(uri));

        return b.toString();
    }

    public static String getFileContent(String uri) {
        try {
            return FileHandler.readFile(uri) + "\n";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getLastModified(String uri) {
        return "Last-Modified: on, " + FileHandler.lastModified(uri) + "\n";
    }

    public static String getContentLength(boolean isHead) {
        if (isHead) {
            return "Content-Length: 0" + "\n";
        }
        return "Content-Length: 402" + "\n";
    }

    public static String getContentType(String fileName) {
        return "Content-Type: " + contentType(fileName) + "\n";
    }

    public static String getSupportedEntries() {
        return "Allow: " + SUPPORTED_METHODS+ "\n";
    }

    public static String getServerInfo() {
        return "Server: " + WEB_SERVER + "\n";
    }

    public static String getLocation(String uri) {
        return "Location: " + uri + "\n";
    }

    public static boolean existingFile(String file) {
        return FileHandler.fileExists(file);
    }

    public static String getHeader(STATUS_CODE status_code) {
        return (Constants.HTTPVERSION + " ") + STATUS_CODE.toString(status_code) + "\n";
    }


    public static String notImplemented() {
        return (Constants.HTTPVERSION + " ") + STATUS_CODE.toString(STATUS_CODE.NOT_IMPLEMENTED) + "\n" +
                getDate();
    }

    public static String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EE MMM d H:m:s z y");
        ZonedDateTime d = ZonedDateTime.now();
        String s = d.format(formatter);
        return "Date: " + s + "\n";
    }

    public static String badRequest() {
        StringBuilder b = new StringBuilder();
        b.append(Constants.HTTPVERSION + " ").append(STATUS_CODE.toString(STATUS_CODE.BAD_REQUEST)).append("\n");
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
