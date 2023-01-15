package ConsoleEngine;

import ConsoleEngine.Exceptions.FileEncryptionException;
import ConsoleEngine.Utilities.FileEncryption;
import ConsoleEngine.Utilities.FileWorker.ReaderAndWriter;
import ConsoleEngine.Utilities.ZipUtilities;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws FileEncryptionException, IOException {
        ReaderAndWriter readWrite = new ReaderAndWriter();
        //readWrite.process("test.txt.encrypted.zip.encrypted.encrypted.encrypted");
        readWrite.process("input.xml.zip.encrypted");

       // readWrite.write("out.txt", text);
        //new FileEncryption("input.xml.zip", "123", FileEncryption.ENCRYPT_MODE).start();
        //ZipUtilities.getInstance().compressFile("input.xml", "input.xml.zip");
        //ZipUtilities.getInstance().unzip("test.txt.encrypted.zip");
    }
}