public class July2009 {
    public static void main(String[] args) {
        if (args.length != 1 || args[0].length() != 1 ||
            args[0].charAt(0) < 'A' || args[0].charAt(0) > 'Z') {
            System.err.println("Usage: java -cp . July2009 <letter(A-Z)>");
        } else {
            char lim = args[0].charAt(0);
            String s = "A";
            for (char c = 'B'; c <= lim; c++) {
                s = c + s + c;
            }
            s = s + s.substring(1);
            int a = s.indexOf(lim);
            int b = s.substring(a + 1).indexOf(lim) + a + 2;
            int c = s.indexOf('A');
            int d = lim - 'A';
            for (int i = c; i <= c + 2 * d; i++) {
                System.out.println(s.substring(a,b).replaceAll("[^" + s.charAt(i) + "]", " "));
            }
        }
    }
}
