package ConsoleEngine.Utilities.FileWorker.Strategy;

import java.io.File;

public interface Strategy {
    public File doOperation(File inputFile, boolean isRead);

}
