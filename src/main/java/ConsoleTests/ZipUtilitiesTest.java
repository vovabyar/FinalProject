package ConsoleTests;

import ConsoleEngine.Utilities.FileWorker.ReaderAndWriter;
import ConsoleEngine.Utilities.ZipUtilities;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ZipUtilitiesTest {

    @Test
    void compressFile() {
        ZipUtilities.getInstance().compressFile("D:\\demo1\\src\\main\\java\\ConsoleTests\\resources\\test.txt");
        File fileExp = new File("D:\\demo1\\src\\main\\java\\ConsoleTests\\resources\\test.txt.zip");
        assertNotNull(fileExp);
    }

    @Test
    void unzip() throws IOException {
        ZipUtilities.getInstance().unzip(new File("D:\\demo1\\src\\main\\java\\ConsoleTests\\resources\\test.txt.encrypted.zip"));
        File fileExp = new File("D:\\demo1\\src\\main\\java\\ConsoleTests\\resources\\test.txt.encrypted");
        assertNotNull(fileExp);
    }
}