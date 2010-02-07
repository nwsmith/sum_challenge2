package com.solium.bots;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class LanceBot {
    private HashMap<String, ArrayList<Result>> _results = new HashMap<String, ArrayList<Result>>();
    private HashMap<String, MoveCode> _mostOverallPopularMoveForBot = new HashMap<String, MoveCode>();
    private HashMap<String, HashMap<MoveCode, MoveCode>> _mostPopularMoveForMyMove = new HashMap<String, HashMap<MoveCode, MoveCode>>();
    private HashMap<String, Integer> _howManyGamesWithBot = new HashMap<String, Integer>();
    private HashMap<String, Integer> _lossesInARow = new HashMap<String, Integer>();
    private static final String LANCEBOT_TXT = "./lancebot.txt";
    private Random _random = new Random(System.currentTimeMillis());

    public static void main(String[] args) {
	final String botname = args[0];
	final String command = args[1];
	if (args.length == 3) {
	    final String lastMove = args[2];
	    final MoveCode moveCode = new LanceBot(botname).processMove(botname, MoveCode.valueOf(lastMove), command);
	    if (moveCode != null) {
		System.out.println(moveCode);
	    }
	} else if (args.length == 2) {
	    System.out.println(new LanceBot(botname).firstMove(botname));
	}
    }

    public LanceBot(String botname) {
	botname = botname.replaceAll(",", "");
	initData(botname);
    }

    public MoveCode firstMove(String botname) {
	final MoveCode myMove = MoveCode.P;
	_results.get(botname).add(new Result(myMove, MoveCode.UNKNOWN));
	writeFile();
	return myMove;
    }

    public MoveCode processMove(String botname, MoveCode lastMove, String command) {
	updateLastMove(botname, lastMove);
	if (command.equals("finish")) {
	    writeFile();
	    return null;
	}
	MoveCode myMove = decideOnNextMove(botname, lastMove);
	myMove = adjustNextMove(botname, myMove);
	_results.get(botname).add(new Result(myMove, MoveCode.UNKNOWN));
	writeFile();
	return myMove;
    }

    public MoveCode adjustNextMove(String botname, MoveCode myMove) {
	boolean isOnLosingStreak = false;
	if (_lossesInARow.containsKey(botname)) {
	    final int loseThreshold = Math.abs(_random.nextInt(5)+1);
	    isOnLosingStreak = _lossesInARow.get(botname) > loseThreshold;

	}
	if (isOnLosingStreak) {
	    System.err.println("We're on a losing streak, need to adjust");
	    final HashMap<MoveCode, MoveCode> moveCodeMoveCodeHashMap = _mostPopularMoveForMyMove.get(botname);
	    final MoveCode theirPossibleMove = moveCodeMoveCodeHashMap.get(myMove);
	    if (theirPossibleMove != null) {
		if (changeStrat(botname)) {
		    return theirPossibleMove;
		} else {
		    return theirPossibleMove.getMoveToBeat();
		}
	    } else {
		return myMove;
	    }
	} else {
	        return myMove;
	}

    }

    private boolean changeStrat(String botname) {
	return _howManyGamesWithBot.containsKey(botname) && _howManyGamesWithBot.get(botname) % 5 == 0;
    }

    public MoveCode decideOnNextMove(String botname, MoveCode lastMove) {
	final int value = _random.nextInt(100);
	final Integer howManyGames = _howManyGamesWithBot.get(botname);
	final MoveCode mostPopularMove = _mostOverallPopularMoveForBot.get(botname);
	System.err.println("we've played " + botname + ": " + howManyGames);

	if (howManyGames == 0 || value < 2) {
	    System.err.println("we haven't played before or we're in our 2% range to play random");
	    return MoveCode.values()[_random.nextInt(3)];
	} else if (howManyGames > 0 && howManyGames <= 3) {
	    System.err.println("we've played before 1 to 3 times so just throw back their last move");
	    return lastMove;
	} else if (value >= 5 && value < 45) {
	    System.err.println("Lets beat their most popular move");
	    if (mostPopularMove != null) {
		return mostPopularMove.getMoveToBeat();
	    } else {
		return MoveCode.values()[Math.abs(_random.nextInt(2))];
	    }
	} else {
	    if (changeStrat(botname)) {
		System.err.println("We've changed strategies, going for their last move");
		return lastMove;
	    } else if (_random.nextInt(30) > 15 && mostPopularMove != null) {
		return mostPopularMove.getMoveToBeat();
	    } else {
		return lastMove.getMoveToBeat();
	    }
	}
    }

    public void writeFile() {
	try {
	    BufferedWriter out = new BufferedWriter(new FileWriter(LANCEBOT_TXT));
	    for (String botname : _results.keySet()) {
		final ArrayList<Result> resultTreeSet = _results.get(botname);
		for (Result result : resultTreeSet) {
		    final String row = botname + "," + result.getMyMove() + "," + result.getTheirMove() + "\n";
		    out.append(row);
		}
	    }
	    out.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void updateLastMove(String botname, MoveCode lastMove) {
	final ArrayList<Result> resultArrayList = _results.get(botname);
	for (Result result : resultArrayList) {
	    if (MoveCode.UNKNOWN == result.getTheirMove()) {
		result.setTheirMove(lastMove);
	    }
	}
    }

    public void initData(String botnamePlaying) {
	_results.put(botnamePlaying, new ArrayList<Result>());
	_mostPopularMoveForMyMove.put(botnamePlaying, new HashMap<MoveCode, MoveCode>());
	try {
	    BufferedReader in = new BufferedReader(new FileReader(LANCEBOT_TXT));
	    String str;
	    while ((str = in.readLine()) != null) {

		final String[] strings = str.split(",");
		final String botnameFromFile = strings[0];
		final String myMove = strings[1];
		final String theirMove = strings[2];
		final Result result = new Result(MoveCode.valueOf(myMove), MoveCode.valueOf(theirMove));
		final ArrayList<Result> resultArrayList;
		if (_results.containsKey(botnameFromFile)) {
		    resultArrayList = _results.get(botnameFromFile);
		} else {
		    resultArrayList = new ArrayList<Result>();
		}
		resultArrayList.add(result);
		_results.put(botnameFromFile, resultArrayList);
	    }
	    in.close();
	} catch (IOException e) {
	}

	for (String botname : _results.keySet()) {
	    final ArrayList<Result> resultArrayList = _results.get(botname);
	    final HashMap<MoveCode, MoveCode> mostPopularMoveMap = _mostPopularMoveForMyMove.get(botname);

	    HashMap<MoveCode, Integer> moveCount = new HashMap<MoveCode, Integer>();
	    HashMap<MoveCode, HashMap<MoveCode, Integer>> moveCountForMyMove = new HashMap<MoveCode, HashMap<MoveCode, Integer>>();
	    MoveCode myLastMove = null;
	    Boolean prevTurnWasLoss = null;
	    for (Result result : resultArrayList) {
		Integer count = 0;
		final MoveCode theirMove = result.getTheirMove();
		if (_howManyGamesWithBot.containsKey(botname)) {
		    _howManyGamesWithBot.put(botname, _howManyGamesWithBot.get(botname) + 1);
		} else {
		    _howManyGamesWithBot.put(botname, 1);
		}
		if (MoveCode.UNKNOWN == theirMove) {
		    continue;
		}
		if (moveCount.containsKey(theirMove)) {
		    count = moveCount.get(theirMove);
		}

		if (prevTurnWasLoss == null) {
		    prevTurnWasLoss = result.loss();
		    if (result.loss()) {
			if (_lossesInARow.containsKey(botname)) {
			    Integer numLosses = _lossesInARow.get(botname);
			    numLosses++;
			    _lossesInARow.put(botname, numLosses);
			} else {
			    _lossesInARow.put(botname, 1);
			}
		    }
		} else {
		    if (result.loss()) {
			if (_lossesInARow.containsKey(botname)) {
			    Integer numLosses = _lossesInARow.get(botname);
			    numLosses++;
			    _lossesInARow.put(botname, numLosses);
			} else {
			    _lossesInARow.put(botname, 1);
			}
		    } else {
			_lossesInARow.put(botname, 0);
		    }
		}

		count++;
		moveCount.put(theirMove, count);
		if (myLastMove != null) {
		    Integer countForLastMove = 1;
		    if (moveCountForMyMove.containsKey(myLastMove)) {
			final HashMap<MoveCode, Integer> moveCountForMyLastMove = moveCountForMyMove.get(myLastMove);
			if (moveCountForMyLastMove.containsKey(theirMove)) {
			    countForLastMove = moveCountForMyLastMove.get(theirMove);
			    countForLastMove++;
			}
			moveCountForMyLastMove.put(theirMove, countForLastMove);
		    } else {
			final HashMap<MoveCode, Integer> moveCountMap = new HashMap<MoveCode, Integer>();
			moveCountMap.put(theirMove, countForLastMove);
			moveCountForMyMove.put(myLastMove, moveCountMap);
		    }
		}

		myLastMove = result.getMyMove();
	    }
	    MoveCode mostPopular = null;
	    for (MoveCode moveCode : moveCount.keySet()) {
		if (mostPopular == null || moveCount.get(mostPopular) < moveCount.get(moveCode)) {
		    mostPopular = moveCode;
		}
	    }
	    _mostOverallPopularMoveForBot.put(botname, mostPopular);

	    for (MoveCode myMoveCode : moveCountForMyMove.keySet()) {
		final HashMap<MoveCode, Integer> moveCountMap = moveCountForMyMove.get(myMoveCode);
		MoveCode mostPopularResponse = null;
		for (MoveCode code : moveCountMap.keySet()) {
		    if (mostPopularResponse == null || moveCountMap.get(mostPopularResponse) < moveCountMap.get(code)) {
			mostPopularResponse = code;
		    }
		}
		HashMap<MoveCode, MoveCode> myMoveToHisMoveMap;
		if (_mostPopularMoveForMyMove.containsKey(botname)) {
		    myMoveToHisMoveMap = _mostPopularMoveForMyMove.get(botname);
		} else {
		    myMoveToHisMoveMap = new HashMap<MoveCode, MoveCode>();
		    _mostPopularMoveForMyMove.put(botname, myMoveToHisMoveMap);
		}
		myMoveToHisMoveMap.put(myMoveCode, mostPopularResponse);
	    }
	}
	System.err.println(_mostOverallPopularMoveForBot);
	System.err.println(_mostPopularMoveForMyMove);
    }
}
