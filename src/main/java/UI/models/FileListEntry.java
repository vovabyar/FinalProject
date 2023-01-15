package UI.models;

import UI.utils.BundleUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;

/**
 * @author vovabyar
 */
public class FileListEntry {
    private NameColumnEntry nameColumnEntry;
    private StringProperty fileSize;
    private StringProperty formattedFileDateOfCreation;
    private FileTime fileDateOfCreation;
    private File file;

    public FileListEntry(File file, Icon swingIcon) throws IOException {
        this.file = file;

        this.nameColumnEntry = new NameColumnEntry(file.getName(), swingIcon);

        String size;
        if (file.isFile()) {
            size = Long.toString(file.length());
        } else {
            size = "<DIR>";
        }
        this.fileSize = new SimpleStringProperty(size);

        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        this.fileDateOfCreation = attr.creationTime();

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, BundleUtil.getInstance().getCurrentLocale());
        this.formattedFileDateOfCreation = new SimpleStringProperty(df.format(fileDateOfCreation.toMillis()));
    }

    public NameColumnEntry getNameColumnEntry() {
        return nameColumnEntry;
    }

    public String getFileSize() {
        return fileSize.get();
    }

    public StringProperty fileSizeProperty() {
        return fileSize;
    }

    public String getFormattedFileDateOfCreation() {
        return formattedFileDateOfCreation.get();
    }

    public StringProperty formattedFileDateOfCreationProperty() {
        return formattedFileDateOfCreation;
    }

    public FileTime getFileDateOfCreation() {
        return fileDateOfCreation;
    }

    public File getFile() {
        return file;
    }
}