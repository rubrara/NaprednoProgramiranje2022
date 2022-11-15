package mk.ukim.finki.xExam.kolokviumi.vlezni;

import java.io.*;
import java.util.Comparator;


// treba da se izbroj vo koj string ima najmnogu karakteri c (ignore case) i toj da se ispecati na system.out
class LineProcessor {

    private int countCharOccurrences(String line, char c) {
        return (int) line.toLowerCase().chars().filter(ch -> (char) ch == c).count();
    }

    public void readLines(InputStream is, OutputStream os, char c) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        PrintWriter writer = new PrintWriter(os);

        Comparator<String> comparator = Comparator.comparing(str -> countCharOccurrences(str, c));

        writer.println(br.lines().max(comparator.thenComparing(Comparator.naturalOrder())).orElse(""));
    }
}

public class LineProcessorTest {

    public static void main(String[] args) {
        LineProcessor lineProcessor = new LineProcessor();

        try {
            lineProcessor.readLines(System.in, System.out, 'a');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
