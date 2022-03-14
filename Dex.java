import java.util.ArrayList;
import java.util.Map;

public class Dex {

    public String file;
    public Map<Integer, Integer> dictionary;
    public ArrayList<String> maxWords;

    @Override
    public String toString() {
        return "Dex: file = " + file + "   dictionary = " + dictionary + "   maxWords = " + maxWords;
    }
}
