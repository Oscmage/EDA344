import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

public class FileHandler {

    public static String readFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get("." + fileName)));
    }

    public static boolean fileExists(String fileName) {
        return new File("." +fileName).exists();
    }

    public static String lastModified(String fileName) {
        File f = new File("." + fileName);
        return new SimpleDateFormat("d MMM Y H:m:s z").format(f.lastModified());
    }
}
