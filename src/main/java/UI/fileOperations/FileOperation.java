package UI.fileOperations;

import javafx.beans.property.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.List;

/**
 * @author vovabyar
 */

public abstract class FileOperation extends SimpleFileVisitor < Path > {
    protected ReadOnlyLongWrapper progress;
    protected List < Path > paths;
    protected BooleanProperty isCanceledProperty;

    public FileOperation(List < Path > paths, BooleanProperty isCanceledProperty) {
        this.paths = paths;
        this.isCanceledProperty = isCanceledProperty;
        this.progress = new ReadOnlyLongWrapper(this, "progress");
    }

    public long getProgress() {
        return progress.get();
    }

    public ReadOnlyLongWrapper progressProperty() {
        return progress;
    }

    public abstract void execute() throws IOException;

    public List < Path > getPaths() {
        return paths;
    }
}