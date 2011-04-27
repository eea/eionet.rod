package eionet.rod.web.action;

import java.util.HashSet;
import java.util.Iterator;

public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        HashSet<Integer> set = new HashSet<Integer>();
        set.add(4);
        set.add(7);
        set.add(9);

        String ret = cnvHashSet(set, ",");
        String a = "a";

    }

    private static String cnvHashSet(HashSet<Integer> hash, String separator) {

        // quick fix
        if (hash == null) {
            return "";
        }

        StringBuffer s = new StringBuffer();
        for(Iterator<Integer> it = hash.iterator(); it.hasNext(); ) {
            Integer id = it.next();
            if (id != null)
                s.append(id);
            if (it.hasNext())
                s.append(separator);

        }

        return s.toString();
    }

}
