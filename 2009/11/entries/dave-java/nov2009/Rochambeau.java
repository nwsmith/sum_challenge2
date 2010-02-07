package nov2009;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.util.Vector;


public class Rochambeau {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String opponent = args[0];
		String turn = args[1];
		String lastMove = null;
		String myMove = null;
		
		Vector allMoves = new Vector();
		
		if (!turn.equals("start")){
			lastMove = args[2];
		}
		
		File opponentFile = new File(opponent+".dav");
		
		if (!turn.equals("start")){
			writeLastMove(opponentFile, lastMove);
		}
		
		if (!turn.equals("finish")){
			if (opponentFile.exists()){
				allMoves = readHistory(opponentFile);
			}
			myMove = getNextMove(allMoves, opponent);
			System.out.println(myMove);
		
			writeInformation(opponentFile, myMove);
		}

	}
	
	public static void writeInformation(File opponentFile, String myMove){
		
		try{
			FileWriter fw = new FileWriter(opponentFile, true);
			BufferedWriter out = new BufferedWriter(fw);
			out.write(myMove + " ");
			out.flush();
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public static void writeLastMove(File opponentFile, String lastMove){
		
		try{
			FileWriter fw = new FileWriter(opponentFile, true);
			BufferedWriter out = new BufferedWriter(fw);
			out.write(lastMove);
			out.newLine();
			out.flush();
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}	
	
	public static Vector readHistory(File opponentFile){
		FileReader fr;
		Vector allMoves = new Vector();
		if (!opponentFile.exists()){
			return allMoves;
		}
		try {
			fr = new FileReader(opponentFile);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			while ((line = br.readLine()) != null && !line.equals("")){
				String[] moves = line.split("\\s");
				allMoves.add(new Move(moves[0], moves[1]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allMoves;

	}
	
	public static String getNextMove(Vector allMoves, String opponent){
		if (allMoves.isEmpty()){
			return calculateSum(opponent, 0); 
		} else {
			Vector historicNextMoves = new Vector();
			int lastIndex = allMoves.size()-1;
			Move lastMove = (Move)allMoves.elementAt(lastIndex);
			
			//gets all the historic next moves that followed the same throws as last round
			for (int i = 0; i < lastIndex; i++){
				if (lastMove.equals((Move)allMoves.elementAt(i))){
					historicNextMoves.add((Move)allMoves.elementAt(i+1));
				}
			}
			
			//gets all the historic next moves that followed a round that matched opponents last throw
			if (historicNextMoves.isEmpty()){
				for (int i = 0; i < lastIndex; i++){
					if (lastMove.equalsTheirMove((Move)allMoves.elementAt(i))){
						historicNextMoves.add((Move)allMoves.elementAt(i+1));
					}
				}				
			}
			//gets all the historic next moves that followed a round that matched my last throw
			if (historicNextMoves.isEmpty()){
				for (int i = 0; i < lastIndex; i++){
					if (lastMove.equalsTheirMove((Move)allMoves.elementAt(i))){
						historicNextMoves.add((Move)allMoves.elementAt(i+1));
					}
				}				
			}			
			
			if (historicNextMoves.isEmpty()){
				return calculateSum(opponent, allMoves.size());
			}
			
			int rockCount = 0;
			int paperCount = 0;
			int scissorsCount = 0;
			for (int j = 0; j < historicNextMoves.size(); j++){
				Move tempMove = (Move)historicNextMoves.elementAt(j);
				if (tempMove.getTheirMove().equals("R")){
					rockCount++;
				} else if (tempMove.getTheirMove().equals("P")){
					paperCount++;
				} else if (tempMove.getTheirMove().equals("S")){
					scissorsCount++;
				}
			}
			
			if (rockCount >= paperCount  && rockCount >= scissorsCount){
				return "P";
			} if (paperCount >= rockCount && paperCount >= scissorsCount){
				return "S";
			} if (scissorsCount >= rockCount && scissorsCount >= paperCount){
				return "R";
			}
			
		}
		
		return calculateSum(opponent, allMoves.size());
		
	}
	
	public static String calculateSum(String opponent, int numberOfPastMoves){
		int total = numberOfPastMoves;
		for (int i = 0; i < opponent.length(); i++){
			total += (int)opponent.charAt(i);
		}
		switch (total%3){
		case 0:
			return "R";
		case 1:
			return "P";
		case 2:
			return "S";
		}
		return "R";
	}
	
	

}
