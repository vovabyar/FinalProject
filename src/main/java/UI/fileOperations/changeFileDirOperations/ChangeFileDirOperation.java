package UI.fileOperations.changeFileDirOperations;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import UI.fileOperations.FileOperation;
import UI.utils.BundleUtil;
import UI.utils.DialogUtil;
import UI.utils.ReplaceOptionsUtil;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;
import static UI.utils.ReplaceOptionsUtil.KEEP_ALL;
import static UI.utils.ReplaceOptionsUtil.NO_ALL;
import static UI.utils.ReplaceOptionsUtil.YES_ALL;

/**
 * @author vovabyar
 */
public abstract class ChangeFileDirOperation extends FileOperation {
    Path targetPath;
    Path currentSourcePath;
    Path currentTargetPath;
    ReplaceOptionsUtil replaceAll;

    public ChangeFileDirOperation(List < Path > paths, BooleanProperty isCanceledProperty, Path targetPath) {
        super(paths, isCanceledProperty);
        this.targetPath = targetPath;
    }

    @Override
    public void execute() throws IOException {
        if (Files.isWritable(targetPath)) {
            for (Path path: paths) {
                if (isCanceledProperty.get()) {
                    break;
                }
                currentSourcePath = path;
                currentTargetPath = Paths.get(targetPath.toString(), currentSourcePath.getFileName().toString());
                Files.walkFileTree(path, this);
            }
        } else {
            Platform.runLater(DialogUtil::noWriteAccessDialog);
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (!isCanceledProperty.get()) {
            Path path = currentTargetPath.resolve(currentSourcePath.relativize(dir));
            if (!Files.exists(path)) {
                Files.copy(dir, path);
            }
            return CONTINUE;
        } else {
            return TERMINATE;
        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (!isCanceledProperty.get()) {
            Path path = currentTargetPath.resolve(currentSourcePath.relativize(file).toString());
            String fileName = file.getFileName().toString();
            if (!Files.exists(path)) {
                changeFileDirOperation(file, path, false);
            } else {
                if (replaceAll == null) {
                    try {
                        switch (showReplaceDialog(file, path, fileName)) {
                            case YES:
                                changeFileDirOperation(file, path, true);
                                break;
                            case KEEP:
                                keepBoth(file, path, fileName);
                                break;
                            case NO:
                                break;
                            case YES_ALL:
                                changeFileDirOperation(file, path, true);
                                replaceAll = YES_ALL;
                                break;
                            case KEEP_ALL:
                                keepBoth(file, path, fileName);
                                replaceAll = KEEP_ALL;
                                break;
                            case NO_ALL:
                                replaceAll = NO_ALL;
                                break;
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    switch (replaceAll) {
                        case YES_ALL:
                            changeFileDirOperation(file, path, true);
                            break;
                        case KEEP_ALL:
                            keepBoth(file, path, fileName);
                            break;
                        case NO_ALL:
                            break;
                    }
                }
            }
            progress.set(progress.getValue() + Files.size(path));
            return CONTINUE;
        } else {
            return TERMINATE;
        }
    }

    private ReplaceOptionsUtil showReplaceDialog(Path file, Path old, String fileName) throws IOException, ExecutionException, InterruptedException {
        String[] sizes = new String[] {
                Long.toString(Files.size(old)), Long.toString(Files.size(file))
        };
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT,
                BundleUtil.getInstance().getCurrentLocale());
        String[] dates = new String[] {
                df.format(Files.getLastModifiedTime(file).toMillis()),
                df.format(Files.getLastModifiedTime(old).toMillis())
        };

        FutureTask < ReplaceOptionsUtil > dialog =
                new FutureTask < > (() -> DialogUtil.replaceDialog(fileName, sizes, dates));
        Platform.runLater(dialog);

        return dialog.get();
    }

    abstract void changeFileDirOperation(Path file, Path destination, boolean replace) throws IOException;

    abstract void keepBoth(Path file, Path destination, String fileName) throws IOException;
}