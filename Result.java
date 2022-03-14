public class Result implements Comparable{

    public String fileName;
    public String rang;
    public String maxLen;
    public String nrLargestWords;

    public Result(String fileName, String rang, String maxLen, String nrLargestWords) {
        this.fileName = fileName;
        this.rang = rang;
        this.maxLen = maxLen;
        this.nrLargestWords = nrLargestWords;
    }

    @Override
    public String toString() {
        return "Result: fileName = " + fileName + "  rang = " + rang + "  maxLen = "
                + maxLen + "  nrLargestWords = " + nrLargestWords;
    }

    public int compareTo(Object o) {

        if (Float.parseFloat(this.rang) > Float.parseFloat(((Result) o).rang)) {
            return 1;
        }
        return -1;
    }
}
