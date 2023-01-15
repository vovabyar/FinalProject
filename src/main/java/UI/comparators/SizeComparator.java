package UI.comparators;

import java.util.Comparator;

/**
 * @author vovabyar
 */
public class SizeComparator implements Comparator < String > {
    @Override
    public int compare(String o1, String o2) {
        long size1;
        long size2;
        if (!o1.equals("<DIR>")) {
            size1 = Long.parseLong(o1);
        } else {
            return -1;
        }
        if (!o2.equals("<DIR>")) {
            size2 = Long.parseLong(o2);
        } else {
            return 1;
        }

        if (size1 < size2) {
            return -1;
        } else if (size1 == size2) {
            return 0;
        } else return 1;
    }
}