package xml.spreadsheet.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** This class replaces Map.of due to problems with null values, and a limit on the number of parameters */
public class MapBuilder {

    /**
     * Creates a Map upon a list of pairs key/value
     * @param args Even-sized list of objects; every odd element must be a String
     * @return Map that excludes any pair that had any null element, that is silently discarded
     */
    public static Map<String, Object> mapOf(Object... args) {
        // Make pairs
        List<Object[]> pairs = new LinkedList<>();
        if (args.length % 2 != 0) throw new IllegalArgumentException("Arguments number must be even");
        for (int i = 0; i < args.length; i += 2) {
            if (args[i] != null && args[i + 1] != null) {
                if (!(args[i] instanceof String)) throw new IllegalArgumentException("Keys must be strings");
                pairs.add(new Object[]{ args[i], args[i + 1] });
            }
        }
        Map<String, Object> ret = new HashMap<>();
        for (Object[] pair: pairs) {
            ret.put((String) pair[0], pair[1]);
        }
        return ret;
    }
}
