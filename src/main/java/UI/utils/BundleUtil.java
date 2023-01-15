package UI.utils;

import java.util.Locale;
import java.util.Observable;
import java.util.ResourceBundle;

/**
 * @author vovabyar
 */
public class BundleUtil extends Observable {
    private static BundleUtil ourInstance = new BundleUtil();
    private Locale currentLocale;
    private Locale englishLocale;
    private Locale rusLocale;
    private ResourceBundle bundle;

    public static BundleUtil getInstance() {
        return ourInstance;
    }

    private BundleUtil() {
        this.englishLocale = new Locale("en");
        this.rusLocale = new Locale("ru");
        this.currentLocale = englishLocale;
        this.bundle = ResourceBundle.getBundle("strings", currentLocale);
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public void setCurrentLocale(String locale) {
        if (locale.equals("en")) {
            this.currentLocale = englishLocale;
        } else {
            this.currentLocale = rusLocale;
        }
        updateBundle();
    }

    synchronized private void updateBundle() {
        this.bundle = ResourceBundle.getBundle("strings", currentLocale);
        this.setChanged();
        this.notifyObservers();
    }
}