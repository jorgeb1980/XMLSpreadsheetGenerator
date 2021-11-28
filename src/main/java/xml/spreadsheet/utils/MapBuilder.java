package xml.spreadsheet.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** This class deals with some problems related to MapBuilder.of and
 * null values
 */
public class MapBuilder {
    public static Map of(Object... args) {
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
