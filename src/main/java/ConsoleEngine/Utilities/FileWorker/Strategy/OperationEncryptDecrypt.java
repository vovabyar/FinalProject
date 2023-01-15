package ConsoleEngine.Utilities.FileWorker.Strategy;

import ConsoleEngine.Utilities.FileEncryption;
import ConsoleEngine.Exceptions.FileEncryptionException;
import ConsoleEngine.Utilities.FileEncryption;

import java.io.File;

public class OperationEncryptDecrypt implements Strategy{

    private File file;

    private FileEncryption fileEncryptionObj;

    @Override
    public File doOperation(File inputFile, boolean isDecrypt) {
        if (isDecrypt) {
            try {
                fileEncryptionObj = new FileEncryption(inputFile.getAbsolutePath(), "123", FileEncryption.DECRYPT_MODE);
                fileEncryptionObj.start();
                file = fileEncryptionObj.getFile();
                return file;

            } catch (FileEncryptionException e) {
                System.out.println("FileEncryption!");
            }
        } else {
            try {
                fileEncryptionObj = new FileEncryption(inputFile.getAbsolutePath(), "123", FileEncryption.ENCRYPT_MODE);
                fileEncryptionObj.start();
                file = fileEncryptionObj.getFile();
                return file;

            } catch (FileEncryptionException e) {
                System.out.println("FileEncryption!");
            }
        }

        return null;
    }
}
