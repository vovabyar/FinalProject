package UI.tasks;

import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import UI.fileOperations.FileOperation;
import UI.utils.FileOperationsUtil;
import UI.views.ProgressDialogView;

import java.io.IOException;

/**
 * @author vovabyar
 */
public class FileOperationTask extends Task < Void > {
    private FileOperation fileOperation;
    private BooleanProperty isCanceledProperty;
    private ProgressDialogView progressDialog;

    public FileOperationTask(FileOperation fileOperation, BooleanProperty isCanceledProperty) throws IOException {
        this.fileOperation = fileOperation;
        this.isCanceledProperty = isCanceledProperty;
        this.progressDialog = new ProgressDialogView();

        progressDialog.setTask(this);
        this.setOnCancelled(event -> {
            this.isCanceledProperty.set(true);
            progressDialog.close();
        });
        this.setOnSucceeded(event -> progressDialog.close());
        progressDialog.show();
    }

    @Override
    protected Void call() throws Exception {
        long fileListSize = FileOperationsUtil.getPathListSize(fileOperation.getPaths());

        fileOperation.progressProperty().addListener((observable, oldValue, newValue) -> {
            updateProgress(fileOperation.getProgress(), fileListSize);
        });

        fileOperation.execute();
        return null;
    }
}