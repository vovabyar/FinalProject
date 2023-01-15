package UI.controllers;

import ConsoleEngine.Utilities.FileWorker.ReaderAndWriter;

import UI.comparators.NameComparator;
import UI.comparators.SizeComparator;
import UI.fileOperations.changeFileDirOperations.CopyFile;
import UI.fileOperations.changeFileDirOperations.MoveFile;
import UI.models.FileListEntry;
import UI.models.NameColumnEntry;
import UI.tasks.FileOperationTask;
import UI.utils.BundleUtil;
import UI.utils.DialogUtil;
import UI.utils.FileOperationsUtil;
import UI.views.SimpleFileEditor;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import UI.fileOperations.DeleteFile;
import UI.fileOperations.FileOperation;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author vovabyar
 */
public class SingleTabController implements Observer {
    private StringProperty currentPath;
    private StringProperty currentDirectory;
    private StringProperty parentPath;
    private boolean canBeDropped;

    @FXML
    private TableView < FileListEntry > fileList;
    @FXML
    private TableColumn < FileListEntry, NameColumnEntry > nameColumn;
    @FXML
    private TableColumn < FileListEntry, String > sizeColumn;
    @FXML
    private TableColumn < FileListEntry, String > dateColumn;
    @FXML
    private ComboBox < String > rootsComboBox;
    @FXML
    private Button upButton;
    @FXML
    private Button rootButton;
    @FXML
    private Label sizeLabel;
    @FXML
    private TextField pathTextField;

    @FXML
    private AnchorPane opacityPane, drawerPane;

    @FXML
    private Label drawerImage;

    @FXML
    private ImageView exit;

    @FXML
    private void initialize() throws IOException {
        currentPath = new SimpleStringProperty("D:\\Новая папка\\Новая папка");
        currentDirectory = new SimpleStringProperty("D:\\Новая папка\\Новая папка");
        parentPath = new SimpleStringProperty("");

        pathTextField.setText("D:\\Новая папка\\Новая папка");
        pathTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    handleChangePath(new File(pathTextField.getText()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        initializeColumns();
        initializeFileLists();
        initializeRootsComboBoxes();
        setupDragAndDrop();

        opacityPane.setVisible(false);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), opacityPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), drawerPane);
        translateTransition.setByX(-600);
        translateTransition.play();

        drawerImage.setOnMouseClicked(event -> {

            opacityPane.setVisible(true);

            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), opacityPane);
            fadeTransition1.setFromValue(0);
            fadeTransition1.setToValue(0.15);
            fadeTransition1.play();

            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), drawerPane);
            translateTransition1.setByX(+600);
            translateTransition1.play();
        });

        opacityPane.setOnMouseClicked(event -> {

            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), opacityPane);
            fadeTransition1.setFromValue(0.15);
            fadeTransition1.setToValue(0);
            fadeTransition1.play();

            fadeTransition1.setOnFinished(event1 -> {
                opacityPane.setVisible(false);
            });

            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), drawerPane);
            translateTransition1.setByX(-600);
            translateTransition1.play();
        });
    }

    @FXML
    private void handleUpButton() throws IOException {
        if (!parentPath.get().equals("")) {
            handleChangePath(new File(parentPath.get()));
        }
    }

    @FXML
    private void handleRootButton() throws IOException {
        handleChangePath(new File(rootsComboBox.getValue()));
    }

    public StringProperty currentDirectoryProperty() {
        return currentDirectory;
    }

    public StringProperty currentPathProperty() {
        return currentPath;
    }

    public void animateMenuOpen() {
        opacityPane.setVisible(true);
        FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), opacityPane);
        fadeTransition1.setFromValue(0);
        fadeTransition1.setToValue(0.15);
        fadeTransition1.play();
        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), drawerPane);
        translateTransition1.setByX(+600);
        translateTransition1.play();
    }

    public void animateMenuClose() {
        opacityPane.setVisible(false);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), opacityPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), drawerPane);
        translateTransition.setByX(-600);
        translateTransition.play();
    }

    private void initializeColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory < > ("nameColumnEntry"));
        nameColumn.setCellFactory(param -> new NameColumnEntryCell());
        nameColumn.setComparator(new NameComparator());
        sizeColumn.setCellValueFactory(cellData -> cellData.getValue().fileSizeProperty());
        sizeColumn.setComparator(new SizeComparator());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().formattedFileDateOfCreationProperty());
    }

    private void initializeFileLists() throws IOException {
        fileList.setItems(FileOperationsUtil.listPathContent(FXCollections.observableArrayList(), new File(currentPath.get()),
                parentPath));
        fileList.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                try {
                    FileListEntry fileListEntry = fileList.getSelectionModel().getSelectedItem();
                    if (fileListEntry != null) {
                        handleChangePath(fileListEntry.getFile());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        fileList.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    FileListEntry fileListEntry = fileList.getSelectionModel().getSelectedItem();
                    if (fileListEntry != null) {
                        handleChangePath(fileListEntry.getFile());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (event.getCode() == KeyCode.DELETE) {
                try {
                    if (fileList.getSelectionModel().getSelectedCells().size() != 0) {
                        handleDeleteAction();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        fileList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        fileList.getSortOrder().add(sizeColumn);
        fileList.sort();
    }

    private void initializeRootsComboBoxes() {
        ObservableList < String > rootsList = FXCollections.observableArrayList();
        Arrays.stream(File.listRoots()).forEach(file -> {
            String name = file.toString();
            if (!name.equals("A:\\")) {
                rootsList.add(file.toString());
            }
        });
        rootsComboBox.setItems(rootsList);
        rootsComboBox.setValue(rootsList.get(0));
        setSizeLabel(rootsList.get(0));

        rootsComboBox.setOnAction(event -> {
            String root = rootsComboBox.getSelectionModel().getSelectedItem();
            try {
                handleChangePath(new File(root));
                setSizeLabel(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setupDragAndDrop() {
        canBeDropped = true;

        fileList.setOnDragDetected(event -> {
            List < FileListEntry > selected = fileList.getSelectionModel().getSelectedItems();
            if (selected.size() != 0) {
                Dragboard db = fileList.startDragAndDrop(TransferMode.COPY_OR_MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putFiles(selected.stream().map(FileListEntry::getFile).collect(Collectors.toList()));
                db.setContent(content);
                event.consume();
            }
        });

        fileList.setOnDragOver(event -> {
            if (event.getGestureSource() != fileList && event.getDragboard().hasFiles() && canBeDropped) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        fileList.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (event.getDragboard().hasFiles()) {
                List < File > files = db.getFiles();
                List < Path > paths = files.stream().map(File::getPath).map(Paths::get).collect(Collectors.toList());

                BooleanProperty isCanceledProperty = new SimpleBooleanProperty(false);

                FileOperation fileOperation;
                if (event.getTransferMode() == TransferMode.COPY) {
                    fileOperation = new CopyFile(paths, isCanceledProperty, Paths.get(currentPath.get()));
                } else {
                    fileOperation = new MoveFile(paths, isCanceledProperty, Paths.get(currentPath.get()));
                }

                try {
                    new Thread(new FileOperationTask(fileOperation, isCanceledProperty)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void setSizeLabel(String root) {
        ResourceBundle bundle = BundleUtil.getInstance().getBundle();

        sizeLabel.setText(FileOperationsUtil.getRootFreeSpace(root) + " k " + bundle.getString("from") + " " +
                FileOperationsUtil.getRootSpace(root) + " k " + bundle.getString("free"));
    }

    private void handleChangePath(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                currentPath.set(file.getPath());
                currentDirectory.set(file.getName());
                if (currentDirectory.get().equals("")) {
                    currentDirectory.set(rootsComboBox.getValue());
                }
                pathTextField.setText(currentPath.get());
            }

            FileOperationsUtil.listPathContent(fileList.getItems(), file, parentPath);
            if (!file.isDirectory()) {
                animateMenuOpen();
            }
            fileList.sort();
        }
    }

    private void deleteSelected(Path path) throws IOException {
        ObservableList < FileListEntry > fileListEntries = fileList.getSelectionModel().getSelectedItems();
        List < Path > pathsToDelete = fileListEntries.stream().
                map(fileListEntry -> Paths.get(fileListEntry.getFile().getPath())).collect(Collectors.toList());
        pathsToDelete.clear();
        pathsToDelete.add(path);

        BooleanProperty isCanceledProperty = new SimpleBooleanProperty(false);
        DeleteFile deleteFiles = new DeleteFile(pathsToDelete, isCanceledProperty);
        new Thread(new FileOperationTask(deleteFiles, isCanceledProperty)).start();
    }
    private void handleDeleteAction() throws IOException {
        if (DialogUtil.deleteDialog()) {
            ObservableList < FileListEntry > fileListEntries = fileList.getSelectionModel().getSelectedItems();
            List < Path > pathsToDelete = fileListEntries.stream().
                    map(fileListEntry -> Paths.get(fileListEntry.getFile().getPath())).collect(Collectors.toList());

            BooleanProperty isCanceledProperty = new SimpleBooleanProperty(false);
            DeleteFile deleteFiles = new DeleteFile(pathsToDelete, isCanceledProperty);
            new Thread(new FileOperationTask(deleteFiles, isCanceledProperty)).start();
            // FileOperationsUtil.listPathContent(fileList.getItems(), new File(currentPath.get()), parentPath);
            //fileList.sort();

            //            ReaderAndWriter readWrite = new ReaderAndWriter();
            //            //readWrite.process("test.txt.encrypted.zip.encrypted.encrypted.encrypted");
            //            readWrite.process(pathsToDelete.get(0).toString());
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg == null) {
            ResourceBundle bundle = BundleUtil.getInstance().getBundle();
            nameColumn.setText(bundle.getString("fileList.columns.name"));
            sizeColumn.setText(bundle.getString("fileList.columns.size"));
            dateColumn.setText(bundle.getString("fileList.columns.date"));
            setSizeLabel(rootsComboBox.getSelectionModel().getSelectedItem());
            try {
                FileOperationsUtil.listPathContent(fileList.getItems(), new File(currentPath.get()), parentPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileList.sort();
        } else {
            System.out.println(currentPath.get());
            System.out.println(((StringProperty) arg).get());
            System.out.println();
            if (((StringProperty) arg).get().equals(currentPath.get())) {
                try {
                    System.out.println("In");
                    FileOperationsUtil.listPathContent(fileList.getItems(), new File(currentPath.get()), parentPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fileList.sort();
            }
        }
    }

    public void clickOpen(MouseEvent mouseEvent) throws IOException {
        ObservableList < FileListEntry > fileListEntries = fileList.getSelectionModel().getSelectedItems();
        List < Path > pathsToOpen = fileListEntries.stream().
                map(fileListEntry -> Paths.get(fileListEntry.getFile().getPath())).collect(Collectors.toList());
        if (pathsToOpen.size() == 0) {
            DialogUtil.noWriteAccessDialog();
        } else {
            animateMenuClose();
            SimpleFileEditor test = new SimpleFileEditor();
            test.setTask(pathsToOpen.get(0));
            test.show();
        }
    }

    public void clickDecryptAndUnzip(MouseEvent mouseEvent) {
        ObservableList < FileListEntry > fileListEntries = fileList.getSelectionModel().getSelectedItems();
        List < Path > pathsToOpen = fileListEntries.stream().
                map(fileListEntry -> Paths.get(fileListEntry.getFile().getPath())).collect(Collectors.toList());
        if (pathsToOpen.size() == 0) {
            DialogUtil.noWriteAccessDialog();
        } else {
            animateMenuClose();
            ReaderAndWriter readWrite = new ReaderAndWriter();
            readWrite.process(pathsToOpen.get(0).toString());
        }
    }

    public void clickZip(MouseEvent mouseEvent) throws IOException {
        ObservableList < FileListEntry > fileListEntries = fileList.getSelectionModel().getSelectedItems();
        List < Path > pathsToOpen = fileListEntries.stream().
                map(fileListEntry -> Paths.get(fileListEntry.getFile().getPath())).collect(Collectors.toList());
        if (pathsToOpen.size() == 0) {
            DialogUtil.noWriteAccessDialog();
        } else {
            animateMenuClose();
            ReaderAndWriter readWrite = new ReaderAndWriter();
            readWrite.Zip(pathsToOpen.get(0).toString());
            // deleteSelected(pathsToOpen.get(0));
        }
    }

    public void clickUnZip(MouseEvent mouseEvent) throws IOException {
        ObservableList < FileListEntry > fileListEntries = fileList.getSelectionModel().getSelectedItems();
        List < Path > pathsToOpen = fileListEntries.stream().
                map(fileListEntry -> Paths.get(fileListEntry.getFile().getPath())).collect(Collectors.toList());
        if (pathsToOpen.size() == 0) {
            DialogUtil.noWriteAccessDialog();
        } else {
            animateMenuClose();
            ReaderAndWriter readWrite = new ReaderAndWriter();
            readWrite.unZip(pathsToOpen.get(0).toString());
            //deleteSelected(pathsToOpen.get(0));

        }
    }

    public void clickEncrypt(MouseEvent mouseEvent) {
        ObservableList < FileListEntry > fileListEntries = fileList.getSelectionModel().getSelectedItems();
        List < Path > pathsToOpen = fileListEntries.stream().
                map(fileListEntry -> Paths.get(fileListEntry.getFile().getPath())).collect(Collectors.toList());
        if (pathsToOpen.size() == 0) {
            DialogUtil.noWriteAccessDialog();
        } else {
            animateMenuClose();
            ReaderAndWriter readWrite = new ReaderAndWriter();
            //deleteSelected(pathsToOpen.get(0));
            readWrite.Encrypt(pathsToOpen.get(0).toString());
        }
    }

    public void clickDecrypt(MouseEvent mouseEvent) {
        ObservableList < FileListEntry > fileListEntries = fileList.getSelectionModel().getSelectedItems();
        List < Path > pathsToOpen = fileListEntries.stream().
                map(fileListEntry -> Paths.get(fileListEntry.getFile().getPath())).collect(Collectors.toList());
        if (pathsToOpen.size() == 0) {
            DialogUtil.noWriteAccessDialog();
        } else {
            animateMenuClose();
            ReaderAndWriter readWrite = new ReaderAndWriter();
            readWrite.Decrypt(pathsToOpen.get(0).toString());
            //deleteSelected(pathsToOpen.get(0));
        }
    }

    private static class NameColumnEntryCell extends TableCell < FileListEntry, NameColumnEntry > {
        @Override
        public void updateItem(NameColumnEntry item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
                setText(null);
            } else {
                Image fxImage = item.getIcon();
                ImageView imageView = new ImageView(fxImage);
                setGraphic(imageView);
                setText(item.getFileName());
            }
        }
    }
}