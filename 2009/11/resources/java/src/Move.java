/**
 * Created by IntelliJ IDEA.
 * User: nsmith
 * Date: Dec 1, 2009
 * Time: 10:49:58 PM
 * To change this template use File | Settings | File Templates.
 */
public enum Move {
    ROCK,
    PAPER,
    SCISSORS;

    public static Move getMove(String shortName) {
        if ("R".equals(shortName)) {
            return ROCK;
        }
        if ("S".equals(shortName)) {
            return SCISSORS;
        }
        if ("P".equals(shortName)) {
            return PAPER;
        }
        throw new RuntimeException("Can't understand name " + shortName);
    }

    public String toString() {
        switch (this) {
            case ROCK:
                return "R";
            case SCISSORS:
                return "S";
            case PAPER:
                return "P";
        }
        throw new RuntimeException("Can't happen");
    }
}
