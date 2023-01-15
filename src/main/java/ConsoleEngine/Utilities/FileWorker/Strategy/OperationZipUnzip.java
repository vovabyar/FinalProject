package ConsoleEngine.Utilities.FileWorker.Strategy;

import ConsoleEngine.Utilities.ZipUtilities;

import java.io.File;
import java.io.IOException;

public class OperationZipUnzip implements Strategy{
    private File file;

    @Override
    public File doOperation(File inputFile, boolean unzip) {
        try {
            if (unzip) {
                String newFileName = ZipUtilities.getInstance().unzip(inputFile);
                file = new File(newFileName);
            } else {
                ZipUtilities.getInstance().compressFile(inputFile.getAbsolutePath());
                file = new File(inputFile.getAbsolutePath()  + ".zip");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }
}
