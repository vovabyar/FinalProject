package ConsoleEngine.Utilities.FileWorker;

import ConsoleEngine.Utilities.FileWorker.Parsing.ParseService;
import ConsoleEngine.Utilities.FileWorker.Strategy.Context;
import ConsoleEngine.Utilities.FileWorker.Strategy.OperationEncryptDecrypt;
import ConsoleEngine.Utilities.FileWorker.Strategy.OperationZipUnzip;

import java.io.File;



public class ReaderAndWriter {

    private File file;

    public void process(String filename) {
        file = new File(filename);

        String result = "";
        while((!FileUtilities.getFileExtension(file).equals("txt")
        && !FileUtilities.getFileExtension(file).equals("json")
        && !FileUtilities.getFileExtension(file).equals("xml"))) {

            switch (FileUtilities.getFileExtension(file)) {
                case "zip":
                    boolean unzip;
                    Context contextZip = new Context(new OperationZipUnzip());
                    file = contextZip.executeStrategy(file, unzip = true);
                    break;
                case "encrypted":
                    boolean decrypt;
                    Context contextEnc = new Context(new OperationEncryptDecrypt());
                    file = contextEnc.executeStrategy(file, decrypt = true);
                    break;
            }
        }

        ParseService parse = new ParseService();
        try {
            parse.parseAndWrite(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void Zip(String path) {
        file = new File(path);
        Context contextZip = new Context(new OperationZipUnzip());
        file = contextZip.executeStrategy(file, false);

    }
    public void unZip(String path) {
        file = new File(path);
        Context contextZip = new Context(new OperationZipUnzip());
        file = contextZip.executeStrategy(file, true);
    }
    public void Encrypt(String path) {
        file = new File(path);
        Context contextZip = new Context(new OperationEncryptDecrypt());
        file = contextZip.executeStrategy(file, false);

    }
    public void Decrypt(String path) {
        file = new File(path);
        Context contextZip = new Context(new OperationEncryptDecrypt());
        file = contextZip.executeStrategy(file, true);
    }
}