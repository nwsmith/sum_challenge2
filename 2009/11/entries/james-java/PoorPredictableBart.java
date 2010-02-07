import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class PoorPredictableBart {


    private static final String historyFileName = "poorPredictableBart.txt";
    private static final String ROCK = "R";
    private static final String PAPER = "P";
    private static final String SCISSORS = "S";

    private static List<String> commands = Arrays.asList("start", "turn", "finish");
    private static List<String> moves = Arrays.asList(ROCK, PAPER, SCISSORS);
    private static String command;
    private static String opponentName;
    private static String theirLastMove;

    private static final int OPPONENT_MOVE_INDEX = 0;
    private static final int MY_MOVE_INDEX = 1;

    private static Map<String, List<List<String>>> moveHistory;

    public static void main(String[] args) throws Exception  {
	init(args);
	generateNextMove();
	writeHistory();
    }

    public static void generateNextMove() {
	List<List<String>> moves;
	String myNextMove;
	if (moveHistory.containsKey(opponentName)) {
	    moves = moveHistory.get(opponentName);
	} else {
	    moves = new ArrayList<List<String>>();
	}

	int moveNumber = moves.size();
	if ((moveNumber == 0) || (command.equals("start"))) {
	    myNextMove = PAPER;
	} else {
	    String opponentLastMove = moves.get(moveNumber - 1).get(OPPONENT_MOVE_INDEX);
	    String myLastMove = moves.get(moveNumber - 1).get(MY_MOVE_INDEX);
	    if (didIWin(myLastMove, opponentLastMove) && myLastMove.equals(PAPER)) {
		myNextMove = PAPER;
	    } else if (isFibonacci(moveNumber)) {
		myNextMove = SCISSORS;
	    } else {
		if (moveNumber % 2 == 0) {
		    myNextMove = opponentLastMove;
		} else {
		    if (moveNumber % 7 == 0) {
			myNextMove = PAPER;
		    } else {
			myNextMove = ROCK;
		    }
		}
	    }
	}
	addMoveToHistory(opponentName, "", myNextMove);
	if (!command.equals("finish")) {
	    System.out.println(myNextMove);
	}
    }

    private static boolean isFibonacci(int num) {
	int fib1 = 0;
	int fib2 = 1;
	int curr = 0;
	while (curr <= num) {
	    curr = fib1 + fib2;
	    fib1 = fib2;
	    fib2 = curr;
	    if (curr == num) {
		return true;
	    }
	}
	return false;
    }

    public static boolean didIWin(String myMove, String theirMove) {
	if (myMove.equals(ROCK)) {
	    return theirMove.equals(SCISSORS);
	} else if (myMove.equals(PAPER)) {
	    return theirMove.equals(ROCK);
	} else {
	    return theirMove.equals(ROCK);
	}
    }

    private static void init(String[] args) throws Exception {
	moveHistory = new HashMap<String, List<List<String>>>();
	if (args.length == 2) {
	    if (!commands.contains(args[1].toLowerCase())) {
		throw new RuntimeException("Invalid input");
	    } else {
		opponentName = args[0];
		command = args[1];
	    }
	} else if (args.length == 3) {
	    if ((!commands.contains(args[1].toLowerCase())) || (!moves.contains(args[2].toUpperCase()))) {
		throw new RuntimeException("Invalid input");
	    } else {
		opponentName = args[0];
		command = args[1];
		theirLastMove = args[2];
	    }
	} else {
	    throw new RuntimeException("Invalid input");
	}
	try {
	    BufferedReader r = new BufferedReader(new FileReader(historyFileName));
	    String line = r.readLine();
	    while (line != null) {
		addMoveToHistory(line);
		line = r.readLine();
	    }
	} catch (FileNotFoundException e) {
	    //@Ignore
	}
	updateLastMove();
    }

    private static void updateLastMove() {
	if ((moveHistory.containsKey(opponentName)) && (!command.equals("start"))) {
	    List<List<String>> moves = moveHistory.get(opponentName);
	    String myMove = moves.get(moves.size() - 1).get(MY_MOVE_INDEX);
	    ArrayList<String> updatedMove = new ArrayList<String>();
	    updatedMove.add(OPPONENT_MOVE_INDEX, theirLastMove);
	    updatedMove.add(MY_MOVE_INDEX, myMove);
	    moves.remove(moves.size() - 1);
	    moves.add(updatedMove);
	}
    }

    private static void writeHistory() {
	try {
	    BufferedWriter w = new BufferedWriter(new FileWriter(historyFileName));
	    for (Map.Entry<String, List<List<String>>> entry : moveHistory.entrySet()) {
		for (List<String> move : entry.getValue()) {
		    String line = entry.getKey() + "," + move.get(OPPONENT_MOVE_INDEX) + "," + move.get(MY_MOVE_INDEX) + "\n";
		    w.write(line);
		}
	    }
	    w.flush();
	} catch (IOException e) {
	    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
	}
    }

    private static void addMoveToHistory(String line) {
	String[] segments = line.split(",");
	String name = segments[0];
	String theirMove = segments[1];
	String myMove = segments[2];
	addMoveToHistory(name, theirMove, myMove);
    }

    private static void addMoveToHistory(String name, String theirMove, String myMove) {
	if (!moveHistory.containsKey(name)) {
	    moveHistory.put(name, new ArrayList<List<String>>());
	}
	ArrayList<String> turn = new ArrayList<String>();
	turn.add(OPPONENT_MOVE_INDEX, theirMove);
	turn.add(MY_MOVE_INDEX, myMove);
	moveHistory.get(name).add(turn);
    }



}
