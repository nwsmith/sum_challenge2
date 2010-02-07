package nov2009;

public class Move {
	private String myMove;
	private String theirMove;
	
	public Move(String myMove, String theirMove) {
		super();
		this.myMove = myMove;
		this.theirMove = theirMove;
	}
	public String getMyMove() {
		return myMove;
	}
	public void setMyMove(String myMove) {
		this.myMove = myMove;
	}
	public String getTheirMove() {
		return theirMove;
	}
	public void setTheirMove(String theirMove) {
		this.theirMove = theirMove;
	}
	
	public boolean equals(Move otherMove){
		return this.myMove.equals(otherMove.getMyMove()) && this.theirMove.equals(otherMove.getTheirMove());
	}
	
	public boolean equalsMyMove(Move otherMove){
		return this.myMove.equals(otherMove.getMyMove());
	}
	
	public boolean equalsTheirMove(Move otherMove){
		return this.theirMove.equals(otherMove.getTheirMove());
	}	


}
