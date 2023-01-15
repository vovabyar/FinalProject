package UI.fileOperations.changeFileDirOperations;

import javafx.beans.property.BooleanProperty;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * @author vovabyar
 */
public class MoveFile extends ChangeFileDirOperation {
    public MoveFile(List < Path > paths, BooleanProperty isCanceledProperty, Path targetPath) {
        super(paths, isCanceledProperty, targetPath);
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if (!isCanceledProperty.get()) {
            if (exc == null) {
                Files.delete(dir);
            }
            return CONTINUE;
        } else {
            return TERMINATE;
        }
    }

    @Override
    void changeFileDirOperation(Path file, Path destination, boolean replace) throws IOException {
        if (replace) {
            Files.move(file, destination, REPLACE_EXISTING);
        } else {
            Files.move(file, destination);
        }
    }

    @Override
    void keepBoth(Path file, Path destination, String fileName) throws IOException {
        String baseFileName = FilenameUtils.getBaseName(fileName);
        String fileExtension = FilenameUtils.getExtension(fileName);
        Path fileCopy = file.resolveSibling(baseFileName + "_copy" + "." + fileExtension);
        Files.move(file, currentTargetPath.resolve(currentSourcePath.relativize(fileCopy).toString()), REPLACE_EXISTING);
    }
}