package mk.ukim.finki.av5.wordcount;

public class LineCounter {

    private int lines;
    private int words;
    private int chars;

    public LineCounter(int lines, int words, int chars) {
        this.lines = lines;
        this.words = words;
        this.chars = chars;
    }

    public LineCounter(String line) {
        this.lines = 1;
        this.words = line.split("\\s+").length;
        this.chars = line.length();
    }

    public LineCounter sum(LineCounter right) {
        return new LineCounter(this.lines + right.lines, this.words + right.words, this.chars + right.chars);

    }

    @Override
    public String toString() {
        return String.format("Lines: %d, Words: %d, Chars: %d\n", lines, words, chars);
    }

}
