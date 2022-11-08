package mk.ukim.finki.av5.wordcount;

import java.util.function.Consumer;

public class LineConsumer implements Consumer<String> {

    private int lines = 0;
    private int words = 0;
    private int chars = 0;

    @Override
    public void accept(String s) {
        ++this.lines;
        this.words += s.split("\\s+").length;
        this.chars += s.length();
    }

    @Override
    public String toString() {
        return String.format("Lines: %d, Words: %d, Chars: %d\n", lines, words, chars);
    }
}
