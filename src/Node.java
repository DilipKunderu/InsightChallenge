/**
 * Author: @DilipKunderu
 */
import java.util.*;

public class Node {
    int index;
    String key;
    long start;
    long end;
    long val;
    int count;
    Set<String> set;

    public Node(String key, long start, long end, long val, int count) {
        this.key = key;
        this.start = start;
        this.end = end;
        this.val = val;
        this.count = count;
        this.set = new HashSet<>();
    }
}
