import java.util.ArrayList;
import java.util.Map;

public class Task2 {

    public String file;
    public ArrayList<Map<Integer, Integer>> dictionaries;
    public ArrayList<ArrayList<String>> maxWordsLists;

    @Override
    public String toString() {
        return "Task2: file = " + file + "   dictionaries = " + dictionaries +
                "  maxWordsLists = " + maxWordsLists;
    }
}
