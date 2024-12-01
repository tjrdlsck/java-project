package txt;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;


public class test {
    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/txt/1.txt"));
            String[] file = lines.toArray(new String[0]); // List를 String 배열로 변환
            // 이제 file 배열을 사용할 수 있습니다.
            for (int i = 0; i < file.length; i++) {
                System.out.println(file[i]);
                System.out.println();
                System.out.println(lines.size());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
