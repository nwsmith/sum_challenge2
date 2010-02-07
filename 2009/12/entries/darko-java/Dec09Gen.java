import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Random;

public class Dec09Gen {
    public static void main(String[] args) throws IOException {
	Random rnd = new Random();
	String[] strings = new String[10]; // small community
	for (int i = 0; i < strings.length; i++) {
	    strings[i] = "";
	    for (int j = 0; j < 10; j++) {
		strings[i] += (char) (rnd.nextInt(26) + 'A');
	    }
	}
	for (int tc = 1; tc < 10; tc++) {
	    int size = 1000 + rnd.nextInt(1001); // between 1000 and 2000
	    PrintWriter pw = new PrintWriter(new FileWriter("dec09_" + tc + ".in"));
	    for (int i = 0; i < size; i++) {
		pw.println(String.format("%s %s <%s>", strings[rnd.nextInt(strings.length)],
					 strings[rnd.nextInt(strings.length)], "email" + i));
	    }
	    pw.close();
	}

	for (int tc = 10; tc < 20; tc++) {
	    int size = 20 + rnd.nextInt(21); // between 20 and 40
	    PrintWriter pw = new PrintWriter(new FileWriter("dec09_" + tc + ".in"));
	    for (int i = 0; i < size; i++) {
		pw.println(String.format("%s %s <%s>", strings[rnd.nextInt(5)], // even smaller community
					 strings[rnd.nextInt(5)], "email" + i));
	    }
	    pw.close();
	}


	for (int tc = 20; tc < 30; tc++) {
	    int size = 10 + rnd.nextInt(6); // between 10 and 50
	    PrintWriter pw = new PrintWriter(new FileWriter("dec09_" + tc + ".in"));
	    for (int i = 0; i < size; i++) {
		pw.println(String.format("%s %s <%s>", strings[rnd.nextInt(2)], // no comment
					 strings[rnd.nextInt(2)], "email" + i));
	    }
	    pw.close();
	}
    }
}
