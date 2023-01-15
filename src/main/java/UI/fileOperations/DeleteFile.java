package UI.fileOperations;

import javafx.beans.property.BooleanProperty;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;

/**
 * @author vovabyar
 */
public class DeleteFile extends FileOperation {
    public DeleteFile(List < Path > paths, BooleanProperty isCanceledProperty) {
        super(paths, isCanceledProperty);
    }

    @Override
    public void execute() throws IOException {
        for (Path path: paths) {
            if (isCanceledProperty.get()) {
                break;
            }
            Files.walkFileTree(path, this);
        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (!isCanceledProperty.get()) {
            if (Files.isWritable(file)) {
                progress.set(progress.getValue() + Files.size(file));
                Files.delete(file);
            } else {
                throw new IOException("Can't delete file");
            }
            return CONTINUE;
        } else {
            return TERMINATE;
        }
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
        if (!isCanceledProperty.get()) {
            if (e == null) {
                Files.delete(dir);
            }
            return CONTINUE;
        } else {
            return TERMINATE;
        }
    }
}