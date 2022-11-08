package mk.ukim.finki.av5.wordcount;

import java.io.*;
import java.util.Scanner;

public class WordCountTest {


    public static void readDataWithScanner(InputStream inputStream) {

        Scanner scanner = new Scanner(inputStream);

        int lines = 0, words = 0, chars = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            ++lines;
            words += line.split("\\s+").length;
            chars += line.length();
        }

        System.out.printf("Lines: %d, Words: %d, Chars: %d\n", lines, words, chars);

    }

    public static void readDataWithBufferedReader(InputStream inputStream) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        int lines = 0, words = 0, chars = 0;

        String line = br.readLine();

        while (line != null) {
           ++lines;
           words += line.split("\\s+").length;
           chars += line.length();
           line = br.readLine();
        }

        System.out.printf("Lines: %d, Words: %d, Chars: %d\n", lines, words, chars);
    }

    public static void readDataWithBufferedReaderWithMapAndReduce(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        LineCounter lc = br.lines()
                .map(LineCounter::new)
                .reduce(new LineCounter(0, 0, 0),
                        LineCounter::sum);

        System.out.println(lc);

    }

    public static void readDataWithBufferedReaderAndConsumer(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        LineConsumer lineConsumer = new LineConsumer();

        br.lines().forEach(lineConsumer);
        System.out.println(lineConsumer);

    }


    public static void main(String[] args) {

        File file = new File("C:\\Users\\koki_\\Desktop\\npProj\\no_2022\\src\\mk\\ukim\\finki\\av5\\files\\text");

        try {
            readDataWithScanner(new FileInputStream(file));
            readDataWithBufferedReader(new FileInputStream(file));
            readDataWithBufferedReaderWithMapAndReduce(new FileInputStream(file));
            readDataWithBufferedReaderAndConsumer(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
