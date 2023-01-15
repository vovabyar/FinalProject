package ConsoleEngine.Utilities.FileWorker;

import java.io.File;

public class FileUtilities {

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        String test = file.getAbsolutePath();
        // если в имени файла есть точка и она не является первым символом в названии файла
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            // ХХХХХ.txt -> txt
            test = fileName.substring(fileName.lastIndexOf(".") + 1);
            return test;
        }
        return "";
    }
}
