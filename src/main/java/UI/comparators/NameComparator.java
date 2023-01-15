package UI.comparators;

import UI.models.NameColumnEntry;

import java.util.Comparator;

/**
 * @author vovabyar
 */
public class NameComparator implements Comparator < NameColumnEntry > {
    @Override
    public int compare(NameColumnEntry o1, NameColumnEntry o2) {
        return o1.getFileName().compareTo(o2.getFileName());
    }
}