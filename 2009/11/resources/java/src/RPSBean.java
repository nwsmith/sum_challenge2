/**
 * Created by IntelliJ IDEA.
 * User: nsmith
 * Date: Dec 1, 2009
 * Time: 10:26:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class RPSBean {

    private String playerOne;
    private String programOne;
    private String playerTwo;
    private String programTwo;
    private String playerOneScore;
    private String playerTwoScore;
    private String turnCount;
    private String _tieCount;
    private String _turnsRemaining;
    private String spread;

    public String getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(final String playerOne) {
        this.playerOne = playerOne;
    }

    public String getProgramOne() {
        return programOne;
    }

    public void setProgramOne(final String programOne) {
        this.programOne = programOne;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(final String playerTwo) {
        this.playerTwo = playerTwo;
    }

    public String getProgramTwo() {
        return programTwo;
    }

    public void setProgramTwo(final String programTwo) {
        this.programTwo = programTwo;
    }

    public String getPlayerOneScore() {
        return playerOneScore;
    }

    public void setPlayerOneScore(final String playerOneScore) {
        this.playerOneScore = playerOneScore;
    }

    public String getPlayerTwoScore() {
        return playerTwoScore;
    }

    public void setPlayerTwoScore(final String playerTwoScore) {
        this.playerTwoScore = playerTwoScore;
    }

    public String getTurnCount() {
        return turnCount;
    }

    public void setTurnCount(final String turnCount) {
        this.turnCount = turnCount;
    }

    public String getTieCount() {
	return _tieCount;
    }

    public void setTieCount(final String tieCount) {
	_tieCount = tieCount;
    }

    public String getTurnsRemaining() {
	return _turnsRemaining;
    }

    public void setTurnsRemaining(final String turnsRemaining) {
	_turnsRemaining = turnsRemaining;
    }

    public String getSpread() {
        return spread;
    }

    public void setSpread(final String spread) {
        this.spread = spread;
    }
}
