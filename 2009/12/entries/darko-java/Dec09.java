import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Dec09 {

    private int n;
    private int[] matchL, matchR;
    private boolean[][] g;
    private boolean[] seen;
    private static final boolean DEBUG = false;

    private boolean bpm(int u) {
	for (int v = 0; v < n; v++) {
	    if (g[u][v]) {
		if (seen[v]) {
		    continue;
		}
		seen[v] = true;
		if (matchR[v] < 0 || bpm(matchR[v])) {
		    matchL[u] = v;
		    matchR[v] = u;
		    return true;
		}
	    }
	}
	return false;
    }

    private void work(String fileName) {
	List<String> firstNames = new ArrayList<String>();
	List<String> lastNames = new ArrayList<String>();
	List<String> emails = new ArrayList<String>();
	Scanner sc = null;
	try {
	    sc = new Scanner(new BufferedReader(new FileReader(fileName), 1 << 20));
	} catch (FileNotFoundException e) {
	    crapOut();
	}
	while (sc.hasNext()) {
	    firstNames.add(sc.next());
	    lastNames.add(sc.next());
	    emails.add(sc.next());
	}
	n = firstNames.size();
	matchL = new int[n];
	matchR = new int[n];
	seen = new boolean[n];
	g = new boolean[n][n];
	if (DEBUG) {
	    System.err.printf("n=%d, starting to build the graph\n", n);
	}
	for (int i = 0; i < n; i++) {
	    matchL[i] = matchR[i] = -1;
	    for (int j = i + 1; j < n; j++) {
		g[i][j] = g[j][i] = !lastNames.get(i).equals(lastNames.get(j));
	    }
	}
	if (DEBUG) {
	    System.err.printf("Graph built, starting to do the matching.");
	}
	int cnt = 0;
	for (int i = 0; i < n; i++) {
	    Arrays.fill(seen, false);
	    if (bpm(i)) {
		cnt++;
	    }
	}
	System.out.printf("Found %d match(es) out of %d possible.\n", cnt, n);
	if (!DEBUG) {
	    for (int i = 0; i < n; i++) {
		if (matchL[i] >= 0) {
		    System.out.printf("%s - %s %s\n", emails.get(matchL[i]), firstNames.get(i), lastNames.get(i));
		}
	    }
	}
	System.out.flush();
    }

    private static void crapOut() {
	System.err.println("Usage:\n\tjava -cp . Dec09 <file_name>");
	System.exit(0);
    }

    public static void main(String[] args) {
	if (args.length != 1) {
	    crapOut();
	}
	new Dec09().work(args[0]);
    }
}
