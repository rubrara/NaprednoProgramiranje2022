package mk.ukim.finki.labs.mapsAndSets;

import java.io.InputStream;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.Scanner;
import java.util.TreeSet;

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
        Scanner sc = new Scanner(inputStream);

        TreeMap<String, TreeSet<String>> anagrams = new TreeMap<>();

        while (sc.hasNextLine()) {
            String word = sc.nextLine();

            boolean check = false;
            for (String key : anagrams.keySet()) {
                check = isAnagram(key, word);
                if (check) {
                    anagrams.get(key).add(word);
                    break;
                }
            }

            if(!check) {
                TreeSet<String> value = new TreeSet<>();
                value.add(word);
                anagrams.put(word, value);
            }
        }

        sc.close();

        StringBuilder sb = new StringBuilder();

        anagrams.entrySet().stream().filter(e -> e.getValue().size() >= 5)
                .forEach(entry -> sb.append(String.join(" ", entry.getValue())).append("\n"));

        System.out.println(sb);
    }

    public static boolean isAnagram(String word1, String word2) {
        char[] first = word1.toCharArray();
        char[] second = word2.toCharArray();

        Arrays.sort(first);
        Arrays.sort(second);

        return Arrays.equals(first, second);
    }
}

