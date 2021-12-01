import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class FileUtils {

    public static List<String> readInputFile(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        try(InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
            Scanner scanner = new Scanner(is)) {
            while(scanner.hasNext()) {
                lines.add(scanner.nextLine());
            }
        }
        return lines;
    }
}
