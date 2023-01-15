package UI.tasks;

import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * @author vovabyar
 */
public class WatchDirTask extends java.util.Observable {
    private static WatchDirTask instance;
    private Map < StringProperty, WatchKey > watchKeyMap;
    private WatchService watcher;

    public static WatchDirTask getInstance() throws IOException {
        if (instance == null) {
            instance = new WatchDirTask();
        }
        return instance;
    }

    private WatchDirTask() throws IOException {
        watchKeyMap = new HashMap < > ();
        this.watcher = FileSystems.getDefault().newWatchService();
    }

    public void call() throws Exception {
        while (true) {
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException ex) {
                break;
            }

            key.pollEvents();
            System.out.println("Cos");

            watchKeyMap.entrySet().stream().filter(entry -> entry.getValue() == key).forEach(entry -> {
                setChanged();
                notifyObservers(entry.getKey());
            });

            if (!key.reset()) {
                break;
            }
        }
    }

    public void addPathStringProperty(StringProperty pathProperty) throws IOException {
        Path dir = Paths.get(pathProperty.get());
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        watchKeyMap.put(pathProperty, key);

        pathProperty.addListener((observable) -> {
            WatchKey old = watchKeyMap.get(pathProperty);
            watchKeyMap.remove(pathProperty);
            if (!watchKeyMap.containsValue(old)) {
                old.cancel();
            }
            Path newDir = Paths.get(pathProperty.get());
            WatchKey newKey = null;
            try {
                newKey = newDir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            } catch (IOException e) {
                e.printStackTrace();
            }
            watchKeyMap.put(pathProperty, newKey);
        });
    }

    public void removePathStringProperty(StringProperty pathProperty) {
        watchKeyMap.get(pathProperty).cancel();
        watchKeyMap.remove(pathProperty);
    }
}