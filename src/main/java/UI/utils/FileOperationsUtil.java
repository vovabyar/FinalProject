package UI.utils;

import UI.views.SimpleFileEditor;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import UI.models.FileListEntry;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author vovabyar
 */
public abstract class FileOperationsUtil {

    public static String getRootSpace(String root) {
        File rootFile = new File(root);
        return Long.toString(rootFile.getTotalSpace() / 1024);
    }

    public static String getRootFreeSpace(String root) {
        File rootFile = new File(root);
        return Long.toString(rootFile.getFreeSpace() / 1024);
    }

    public static Long getPathListSize(List < Path > paths) throws IOException {
        long result = 0;

        for (Path path: paths) {
            if (Files.isDirectory(path)) {
                Stream < Path > stream = Files.list(path);
                result += getPathListSize(stream.collect(Collectors.toList()));
                stream.close();
            } else {
                result += Files.size(path);
            }
        }

        return result;
    }

    public static ObservableList < FileListEntry > listPathContent(ObservableList < FileListEntry > fileListEntries, File pathContent,
                                                                   StringProperty parentPath) throws IOException {
        if (pathContent.exists()) {
            if (pathContent.isDirectory()) {
                File[] files = pathContent.listFiles(file -> !file.isHidden() && Files.isReadable(file.toPath()));
                fileListEntries.clear();
                if (pathContent.getParent() != null) {
                    parentPath.set(pathContent.getParent());
                }
                for (File file: files) {
                    Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
                    fileListEntries.add(new FileListEntry(file, icon));
                }
            } else {
                //TODO:: CHECK FILE EXTENSIONS
                //                SimpleFileEditor test = new SimpleFileEditor();
                //                test.setTask(pathContent.toPath());
                //                test.show();
                //Desktop.getDesktop().open(pathContent);
            }
        }

        return fileListEntries;
    }
}