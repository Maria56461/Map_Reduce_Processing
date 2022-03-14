import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

public class Worker2 extends java.lang.Thread{

    private int numberOfThreads;
    private int size;
    private int id;
    private ArrayList<Task2> taskArrayList;
    private CyclicBarrier barrier;
    private ArrayList<Result> results = new ArrayList<Result>(size);

    public ArrayList<Result> getResults() {
        return results;
    }

    public Integer fibo(Integer k) {
        if (k <= 1) {
            return k;
        }
        return fibo(k - 1) + fibo(k - 2);
    }

    public Worker2(int numberOfThreads, int size, int id, ArrayList<Task2> taskArrayList,
                   CyclicBarrier barrier) {
        this.numberOfThreads = numberOfThreads;
        this.size = size;
        this.id = id;
        this.taskArrayList = taskArrayList;
        this.barrier = barrier;
    }

    @Override
    public String toString() {
        return "Worker2{" +
                "numberOfThreads=" + numberOfThreads +
                ", size=" + size +
                ", id=" + id +
                ", taskArrayList=" + taskArrayList +
                ", barrier=" + barrier +
                '}';
    }

    @Override
    public void run() {

        for (int i = 0; i < size; i++) {
            Task2 currTask = taskArrayList.get(i);
            float rang;
            float suma = 0;
            float nrWords = 0;
            Integer maxLen = 0;
            Integer nrLargestWords = 0;
            for (int j = 0; j < currTask.dictionaries.size(); j++) {
                HashMap<Integer, Integer> currMap = (HashMap) currTask.dictionaries.get(j);
                Iterator hmIterator = currMap.entrySet().iterator();
                while (hmIterator.hasNext()) {
                    Map.Entry mapElement = (Map.Entry) hmIterator.next();
                    suma += fibo(((Integer)mapElement.getKey()) + 1) * ((Integer) mapElement.getValue());
                    nrWords = nrWords + ((Integer) mapElement.getValue());
                    if (((Integer) mapElement.getKey()) > maxLen) {
                        maxLen = (Integer) mapElement.getKey();
                        nrLargestWords = (Integer) mapElement.getValue();
                    }
                    else if (((Integer) mapElement.getKey()) == maxLen) {
                        nrLargestWords += (Integer) mapElement.getValue();
                    }
                }
            }

            rang = suma / nrWords;
            String rang_res;
            if (String.format("%.2f", rang).contains(",")) {
                rang_res = String.format("%.2f", rang).replaceAll(",", ".");
            }
            else {
                rang_res = String.format("%.2f", rang);
            }
            String filename[] = currTask.file.split("/");
            String fileRes = filename[filename.length - 1];
            Result res = new Result(fileRes, rang_res, maxLen.toString(), nrLargestWords.toString());
            results.add(res);
        }

    }
}
