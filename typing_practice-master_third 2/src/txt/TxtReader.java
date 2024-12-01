package txt;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class TxtReader {
    public List<String> txtRead(int fileNum) throws IOException {
        return Files.readAllLines(Paths.get("src/txt/" + fileNum + ".txt"));  // fileNum 을 받아 지정된 경로로 찾아감
    }
}