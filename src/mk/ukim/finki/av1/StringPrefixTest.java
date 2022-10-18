package mk.ukim.finki.av1;

class StringPrefix {

    static boolean isPrefix (String str1, String str2) {
        if (str1.length() > str2.length()) return false;

        for (int i = 0; i < str1.length(); i++) {
            if (str1.charAt(i)!=str2.charAt(i)) return false;
        }
        return true;
    }

}

public class StringPrefixTest {
    public static void main(String[] args) {
        String str1 = "Nameredno";
        String str2 = "NamerednoProg";

        System.out.println(str2.startsWith(str1));

        System.out.println(StringPrefix.isPrefix(str1,str2));
    }
}
