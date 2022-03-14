import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Worker extends java.lang.Thread {

    private int numberOfThreads;
    private int size;
    private int id;
    private ArrayList<Task> taskArrayList;
    private ArrayList<Dex> results;
    private CyclicBarrier barrier;
    private Map<File, StringBuilder> fullTexts;

    public ArrayList<Dex> getResults() {
        return results;
    }

    public Worker(int numberOfThreads, int size, int id, ArrayList<Task> taskArrayList,
                  CyclicBarrier barrier, Map<File, StringBuilder> fullTexts) {
        this.numberOfThreads = numberOfThreads;
        this.size = size;
        this.id = id;
        this.taskArrayList = taskArrayList;
        this.barrier = barrier;
        this.fullTexts = fullTexts;
    }

    Boolean notSeparator(char c) {

        if ((c != ' ') && (c != ',') && (c != ';') && (c != ':') && (c != '{') && (c != '}')
                && (c != '~') && (c != '/') && (c != '?') && (c != '\\') && (c != '.') && (c != '<')
                && (c != '`') && (c != '>') && (c != '[') && (c != ']') && (c != '(') && (c != ')')
                && (c != '!') && (c != '@') && (c != '#') && (c != '$') && (c != '%') && (c != '^')
                && (c != '&') && (c != '-') && (c != '_') && (c != '+') && (c != '\'') && (c != '=')
                && (c != '*') && (c != '"') && (c != '|') && (c != '\t') && (c != '\r')
                && (c != '\n') && (c != '\0')) {
            return true;
        }

        return false;
    }

    @Override
    public void run() {
        int l = 0;
        results = new ArrayList<Dex>(taskArrayList.size());
        for (int i = 0; i < taskArrayList.size(); i++) {
            Iterator hmIterator = fullTexts.entrySet().iterator();
            Task currTask = taskArrayList.get(i);
            while (hmIterator.hasNext()) {
                Map.Entry mapElement = (Map.Entry)hmIterator.next();
                if (mapElement.getKey().toString().equals(currTask.file)) {
                    StringBuilder fullText = (StringBuilder) mapElement.getValue();
                    if (fullText.length() > currTask.offset + currTask.dimension) {
                        if ((Character.isLetterOrDigit(fullText.charAt(currTask.offset + currTask.dimension))) &&
                                (Character.isLetterOrDigit(fullText.charAt(currTask.offset + currTask.dimension - 1)))) {
                            // daca fragmentul se termina în mijlocul unui cuvant
                            int k;
                            for (k = currTask.offset + currTask.dimension; k < fullText.length();
                                 k++) {
                                if (Character.isLetterOrDigit(fullText.charAt(k))) {
                                    // alipim intregul cuvant la fragmentul curent
                                    currTask.text.append(fullText.charAt(k));
                                    l++;
                                } else break;
                                // cand gasim un separator, ne oprim din parcurgerea caracterelor
                            }
                        }
                    }
                    if (currTask.offset != 0) {
                        if ((Character.isLetterOrDigit(fullText.charAt(currTask.offset - 1))) &&
                                (Character.isLetterOrDigit(fullText.charAt(currTask.offset)))) {
                            // dacă fragmentul incepe in mijlocul unui cuvant
                            int m = 0;
                            for (m = 0; m < currTask.dimension; m++) {
                                if (!(notSeparator(currTask.text.charAt(m)))) {
                                    for (int k = 0; k < currTask.dimension - m; k++) {
                                        currTask.text.setCharAt(k, currTask.text.charAt(k + m));
                                    }
                                    for (int n = 0; n < m; n++) {
                                        currTask.text.deleteCharAt(currTask.dimension - 1 - n);
                                    }
                                    break;
                                }
                            }
                            if (m == currTask.dimension) {
                                currTask.text.delete(0, currTask.dimension);
                            }
                        }
                    }
                }
            }
            Integer maxDim = 0;
            Dex result = new Dex();
            result.file = currTask.file;
            Map<Integer, Integer> dexMap = new HashMap<Integer, Integer>();
            if (currTask.dimension > 0) {
                String[] splitWords = currTask.text.toString().split("\\W+");
                for (int b = 0; b < splitWords.length; b++) {
                    if (splitWords[b].length() > 0) {
                        if (splitWords[b].length() > maxDim) {
                            maxDim = splitWords[b].length();
                            result.maxWords = new ArrayList<String>();
                            result.maxWords.add(splitWords[b]);
                            dexMap.put(splitWords[b].length(), 1);
                        }
                        else if (splitWords[b].length() == maxDim) {
                            result.maxWords.add(splitWords[b]);
                            Iterator iterator = dexMap.entrySet().iterator();
                            while (iterator.hasNext()) {
                                Map.Entry mapElement = (Map.Entry) iterator.next();
                                if ((Integer) mapElement.getKey() == splitWords[b].length()) {
                                    mapElement.setValue((Integer) mapElement.getValue() + 1);
                                    break;
                                }
                            }
                        }
                        else {
                            if (!dexMap.containsKey(splitWords[b].length())) {
                                dexMap.put(splitWords[b].length(), 1);
                            }
                            else {
                                Iterator iterator = dexMap.entrySet().iterator();
                                while (iterator.hasNext()) {
                                    Map.Entry mapElement = (Map.Entry) iterator.next();
                                    if ((Integer) mapElement.getKey() == splitWords[b].length()) {
                                        mapElement.setValue((Integer) mapElement.getValue() + 1);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            result.dictionary = dexMap;
            results.add(result);
        }
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Worker: numberOfThreads = " + numberOfThreads + "   size = " + size +
                "   id = " + id + "   taskArrayList = " + taskArrayList +
                "   barrier = " + barrier;
    }
}

