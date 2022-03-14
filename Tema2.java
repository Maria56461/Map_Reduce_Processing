import java.io.*;
import java.util.*;
import java.util.concurrent.CyclicBarrier;

public class Tema2 {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length < 3) {
            System.err.println("Usage: Tema2 <workers> <in_file> <out_file>");
            return;
        }

        Integer nrWorkers = Integer.valueOf(args[0]);
        Integer fragDim = 0;
        ArrayList<File> filesArray = new ArrayList<File>();
        Integer nrFiles = 0;
        ArrayList<Task> tasks = new ArrayList<Task>();
        Thread[] threads = new Thread[nrWorkers];
        Thread[] threads2 = new Thread[nrWorkers];
        CyclicBarrier barrier = new CyclicBarrier(nrWorkers);
        Map<File, StringBuilder> fullTexts = new HashMap<File, StringBuilder>();
        ArrayList<Dex> results_collected;
        ArrayList<Result> final_res_collected;

        try {
            File file = new File(args[1]);
            int nrLine = 0;
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                // citire fisier de intrare
                String data = myReader.nextLine();
                nrLine++;
                if (nrLine == 1) {
                    fragDim = Integer.valueOf(data);
                } else if (nrLine == 2) {
                    nrFiles = Integer.valueOf(data);
                    filesArray = new ArrayList<File>(nrFiles);
                } else {
                    File fileIn = new File("../" + data);
                    filesArray.add(fileIn);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        for (int t = 0; t < nrFiles; t++) {
            // parcurg fisier cu fisier
            StringBuilder fullText = new StringBuilder();
            FileReader fr = new FileReader(filesArray.get(t));
            BufferedReader br = new BufferedReader(fr);
            int c = 0;
            ArrayList<String> array = new ArrayList<String>();
            ArrayList<ArrayList<String>> fragments = new ArrayList<ArrayList<String>>();
            while ((c = br.read()) != -1) {
                char cc = (char) c;
                fullText.append(cc);
                String character = String.valueOf(cc);
                array.add(character);
                if (array.size() == fragDim) {
                    fragments.add(array);
                    array = new ArrayList<String>();
                }
            }
            fullTexts.put(filesArray.get(t), fullText);
            if (array.size() > 0) {
                fragments.add(array);
            }

            Integer offset = 0;
            StringBuilder s = new StringBuilder("");
            for (int j = 0; j < fragments.size(); j++) {
                for (int i = 0; i < fragments.get(j).size(); i++) {
                    s.append(fragments.get(j).get(i));
                }
                Task task = new Task(filesArray.get(t).getPath(), offset, s.length(), s);
                offset += s.length();
                tasks.add(task);
                s = new StringBuilder("");
            }
            // pana aici am creat vectorul de taskuri
        }
        results_collected = new ArrayList<Dex>(tasks.size());
        for (int i = 0; i < nrWorkers; i++) {
            int start = i * (int) Math.ceil((double) tasks.size() / (double) nrWorkers);
            int end = Math.min((i + 1) * (int) Math.ceil((double) tasks.size() /
                    (double) nrWorkers), tasks.size());
            ArrayList<Task> taskArrayList = new ArrayList<Task>(end - start);
            for (int j = start; j < end; j++) {
                taskArrayList.add(tasks.get(j));
            }
            threads[i] = new Worker(nrWorkers, end - start, i, taskArrayList, barrier, fullTexts);
        }

        for (int i = 0; i < nrWorkers; i++) {
            threads[i].start();
        }

        for (int i = 0; i < nrWorkers; i++) {
            threads[i].join();
        }

        for (int i = 0; i < nrWorkers; i++) {
            results_collected.addAll(((Worker) threads[i]).getResults());
        }

        ArrayList<Task2> tasks2 = new ArrayList<Task2>(nrFiles);
        for (int i = 0; i < results_collected.size(); i++) {
            int j = 0;
            for (j = 0; j < tasks2.size(); j++) {
                if (tasks2.get(j).file.equals(results_collected.get(i).file)) {
                    tasks2.get(j).dictionaries.add(results_collected.get(i).dictionary);
                    tasks2.get(j).maxWordsLists.add(results_collected.get(i).maxWords);
                    break;
                }
            }
            if (j == tasks2.size()) {
                Task2 newTask = new Task2();
                newTask.file = results_collected.get(i).file;
                newTask.dictionaries = new ArrayList<Map<Integer, Integer>>();
                newTask.dictionaries.add(results_collected.get(i).dictionary);
                newTask.maxWordsLists = new ArrayList<ArrayList<String>>();
                newTask.maxWordsLists.add(results_collected.get(i).maxWords);
                tasks2.add(newTask);
            }
        }
        // pana aici am creat al doilea vector de taskuri

        final_res_collected = new ArrayList<>(tasks2.size());
        for (int i = 0; i < nrWorkers; i++) {
            int start = i * (int) Math.ceil((double) tasks2.size() / (double) nrWorkers);
            int end = Math.min((i + 1) * (int) Math.ceil((double) tasks2.size() /
                    (double) nrWorkers), tasks2.size());
            ArrayList<Task2> taskArrayList = new ArrayList<Task2>(end - start);
            for (int j = start; j < end; j++) {
                taskArrayList.add(tasks2.get(j));
            }
            threads2[i] = new Worker2(nrWorkers, end - start, i, taskArrayList, barrier);
        }

        for (int i = 0; i < nrWorkers; i++) {
            threads2[i].start();
        }

        for (int i = 0; i < nrWorkers; i++) {
            threads2[i].join();
        }

        for (int i = 0; i < nrWorkers; i++) {
            final_res_collected.addAll(((Worker2) threads2[i]).getResults());
        }

        Collections.sort(final_res_collected, Result::compareTo);
        Collections.sort(final_res_collected, Collections.reverseOrder());

        File outFile = new File(args[2]);
        try {
            FileWriter myWriter = new FileWriter(args[2]);
            for (int i = 0; i < tasks2.size(); i++) {
                myWriter.write(final_res_collected.get(i).fileName + ","
                        + final_res_collected.get(i).rang + "," +
                        final_res_collected.get(i).maxLen + "," +
                        final_res_collected.get(i).nrLargestWords + "\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
