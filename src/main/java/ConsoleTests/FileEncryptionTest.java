package ConsoleTests;

import ConsoleEngine.Exceptions.FileEncryptionException;
import ConsoleEngine.Utilities.FileEncryption;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileEncryptionTest {

    @Test
    void start() throws FileEncryptionException {
        new FileEncryption("D:\\demo1\\src\\main\\java\\ConsoleTests\\resources\\input.xml.zip.encrypted", "123", FileEncryption.DECRYPT_MODE).start();
        File fileExp = new File("D:\\demo1\\src\\main\\java\\ConsoleTests\\resources\\input.xml.zip");
        assertNotNull(fileExp);
    }
}