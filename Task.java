public class Task {

    public String file;
    public Integer offset;
    public Integer dimension;
    public StringBuilder text;

    public Task(String file, Integer offset, Integer dimension, StringBuilder text) {
        this.file = file;
        this.offset = offset;
        this.dimension = dimension;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Task: '" + file + '\'' + " offset = " + offset + " dimension = " + dimension +
                " text = " + text + '\n';
    }
}
