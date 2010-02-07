import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class RPS {

    public static void main(String[] args) {
	String all = "RPS";
	String beats = "PSR";
	int LB = 4;
	int[] cnt = new int[3];
	if (args.length > 2) {
	    try {
		PrintWriter pw = new PrintWriter(new FileWriter(args[0], true));
		pw.print("finish".equals(args[1]) ? "\n" : args[2]);
		pw.flush();
		pw.close();
	    } catch (IOException e) {
	    }
	}
	List<String> old = new ArrayList<String>();
	try {
	    BufferedReader br = new BufferedReader(new FileReader(args[0]));
	    String s;
	    while ((s = br.readLine()) != null) {
		old.add(s);
	    }
	    br.close();
	} catch (IOException e) {
	    // ignore
	}
	char move = 'P';
	cnt[0] = cnt[1] = cnt[2] = 0;
	switch (args[1].toLowerCase().charAt(0)) {
	    case 's':
		for (String s : old) {
		    if (s.length() > 1) {
			cnt[all.indexOf(s.charAt(0))]++;
		    }
		}
		move = getMove(beats, cnt);
		break;
	    case 't':
		String lastString = old.get(old.size() - 1);
		int kk = lastString.length();
		if (kk > LB) {
		    String last = lastString.substring(kk - LB, kk);
		    for (String s : old) {
			for (int i = 0; i < s.length() - LB - 1; i++) {
			    if (s.substring(i).startsWith(last)) {
				cnt[all.indexOf(s.charAt(i + LB))]++;
			    }
			}
		    }
		}
		move = getMove(beats, cnt);
		break;
	    case 'f':
		move = '\n';
		break;
	}
	System.out.println(move);
    }

    private static char getMove(String beats, int[] cnt) {
	char move;
	int most = -1;
	int cmost = -1;
	for (int i = 0; i < 3; i++) {
	    if (cnt[i] > most) {
		most = cnt[i];
		cmost = i;
	    }
	}
	move = beats.charAt(cmost);
	return move;
    }
}
