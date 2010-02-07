/**
 * Created by IntelliJ IDEA.
 * User: nsmith
 * Date: Dec 1, 2009
 * Time: 10:49:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Turn {
    private final Move playerOneMove;
    private final Move playerTwoMove;

    public Turn(Move playerOneMove, Move playerTwoMove) {
        this.playerOneMove = playerOneMove;
        this.playerTwoMove = playerTwoMove;
    }

    public Move getPlayerOneMove() {
        return playerOneMove;
    }

    public Move getPlayerTwoMove() {
        return playerTwoMove;
    }

    public int scoreMove() {
        switch (playerOneMove) {
            case ROCK:
                switch (playerTwoMove) {
                    case ROCK:
                        return 0;
                    case PAPER:
                        return 1;
                    case SCISSORS:
                        return -1;
                }
            case PAPER:
                switch (playerTwoMove) {
                    case ROCK:
                        return -1;
                    case PAPER:
                        return 0;
                    case SCISSORS:
                        return 1;
                }
            case SCISSORS:
                switch (playerTwoMove) {
                    case ROCK:
                        return 1;
                    case PAPER:
                        return -1;
                    case SCISSORS:
                        return 0;
                }
            default:
                throw new RuntimeException("unknown move");
        }
    }
}
