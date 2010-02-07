import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class MorseCode {
    private Map<String, String> allWords;
    private String[][] codes;
    private char[][] letters;
    private int[] len;
    private static final String[] MC = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.",
                                        "....", "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.",
                                        "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--",
                                        "--.."};
    private String code;
    private int size;
    private char[] a;

    public Set<String> in, out;

    public MorseCode() {
        allWords = new HashMap<String, String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("/usr/share/dict/words"));
            String s;
            while ((s = br.readLine()) != null) {
                boolean ok = true;
                for (int i = 0; i < s.length(); i++) {
                    if (!Character.isLetter(s.charAt(i))) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    allWords.put(s.toUpperCase(), s);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        codes = new String[5][26];
        letters = new char[5][26];
        len = new int[5];
        for (int i = 0; i < 26; i++) {
            int k = MC[i].length();
            codes[k][len[k]] = MC[i];
            letters[k][len[k]++] = (char) ('A' + i);
        }
        a = new char[128];
        in = new TreeSet<String>();
        out = new TreeSet<String>();
    }

    private void go(int pos) {
        if (pos == code.length()) {
            String s = new String(a, 0, size);
            if (allWords.containsKey(s)) {
                in.add(allWords.get(s));
            } else {
                out.add(s);
            }
        }
        int kk = 1;
        while (kk < 5 && kk + pos <= code.length()) {
            for (int i = 0; i < len[kk]; i++) {
                if (code.substring(pos, pos + kk).equals(codes[kk][i])) {
                    a[size++] = letters[kk][i];
                    go(pos + kk);
                    size--;
                }
            }
            kk++;
        }
    }

    public static void main(String[] args) {
        if (args.length != 1 || args[0].replaceAll("\\.", "").replaceAll("-", "").length() != 0) {
            System.err.println("Usage:\n\tjava -cp . MorseCode <non-empty-string-of-dots-and-dashes>");
        } else {
            MorseCode mc = new MorseCode();
            mc.code = args[0];
            mc.size = 0;
            mc.go(0);
            System.out.printf("\nFound %d matches, %d in the dictionary\n\n", mc.in.size() + mc.out.size(), mc.in.size());
            if (mc.in.size() > 0) {
                System.out.println("\tIn the dictionary:");
                for (String s : mc.in) {
                    System.out.printf("\t\t%s\n", s);
                }
                System.out.println();
            }
            if (mc.out.size() > 0) {
                System.out.println("\t\"Gibberish\":");
                int kk = 0;
                for (String s : mc.out) {
                    System.out.printf("\t\t%s\n", s);
                }
            }
            System.out.println();
        }
    }
}
