import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by IntelliJ IDEA.
 * User: nsmith
 * Date: Dec 1, 2009
 * Time: 10:38:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class RPSRunner {
    private RPSBean rpsBean;
    private RPSForm rpsForm;
    private Deque<Turn> turns;
    private int turnCount;
    private JFrame frame;

    public RPSRunner() {
        turns = new ArrayDeque<Turn>();
    }

    public RPSForm getRpsForm() {
        return rpsForm;
    }

    public void setRpsForm(RPSForm rpsForm) {
        this.rpsForm = rpsForm;
    }

    public RPSBean getRpsBean() {
        return rpsBean;
    }

    public void setRpsBean(RPSBean rpsBean) {
        this.rpsBean = rpsBean;
    }

    public void run() {
        rpsForm.getData(rpsBean);
        int turnCount = Integer.valueOf(rpsBean.getTurnCount());
        this.turnCount = turnCount;
        rpsBean.setTurnsRemaining(rpsBean.getTurnCount());
        rpsForm.setData(rpsBean);
        runStart();
        doScore(1);

        for (int i = 1; i < turnCount; ++i) {
            runTurn();
            doScore(i+1);
        }

        runFinish();
        displayWinner();
    }

    private void displayWinner() {
        rpsForm.getData(rpsBean);
        int s1 = Integer.valueOf(rpsBean.getPlayerOneScore());
        int s2 = Integer.valueOf(rpsBean.getPlayerTwoScore());
        if (s1 > s2) {
            rpsForm.setPlayerOneWinner();
        } else if (s1 < s2) {
            rpsForm.setPlayerTwoWinner();
        } else {
            rpsForm.setTie();
        }
    }

    private void runStart() {
        String commandTemplate = "%s %s start";
        Move p1 = getMove(String.format(commandTemplate, rpsBean.getProgramOne(), rpsBean.getPlayerTwo()));
        Move p2 = getMove(String.format(commandTemplate, rpsBean.getProgramTwo(), rpsBean.getPlayerOne()));
        turns.add(new Turn(p1, p2));
    }

    private void runTurn() {
        String commandTemplate = "%s %s turn %s";
        Turn last = turns.peekLast();
        Move p1 = getMove(String.format(commandTemplate, rpsBean.getProgramOne(), rpsBean.getPlayerTwo(), last.getPlayerTwoMove()));
        Move p2 = getMove(String.format(commandTemplate, rpsBean.getProgramTwo(), rpsBean.getPlayerOne(), last.getPlayerOneMove()));
        turns.add(new Turn(p1, p2));
    }

    private void runFinish() {
        String commandTemplate = "%s %s finish";
        runCommand(String.format(commandTemplate, rpsBean.getProgramOne(), rpsBean.getPlayerTwo()));
        runCommand(String.format(commandTemplate, rpsBean.getProgramTwo(), rpsBean.getPlayerOne()));
    }

    private void doScore(int currentTurn) {
        int s1 = Integer.valueOf(rpsBean.getPlayerOneScore());
        int s2 = Integer.valueOf(rpsBean.getPlayerTwoScore());
        int t = Integer.valueOf(rpsBean.getTieCount());
        Turn last = turns.peekLast();
        int score = last.scoreMove();
        if (score < 0) {
            s1++;
            rpsBean.setPlayerOneScore(String.valueOf(s1));
            rpsForm.setData(rpsBean);
        }
        if (score > 0) {
            s2++;
            rpsBean.setPlayerTwoScore(String.valueOf(s2));
            rpsForm.setData(rpsBean);
        }
        if (score == 0) {
            t++;
            rpsBean.setTieCount(String.valueOf(t));
            rpsForm.setData(rpsBean);
        }
        if (s1 > s2) {
            rpsForm.txtPlayerOneScore.setBackground(Color.getHSBColor(0.394f, 0.984f, 0.667f));
            rpsForm.txtPlayerTwoScore.setBackground(UIManager.getColor("Panel.background"));
            rpsForm.txtTieCount.setBackground(UIManager.getColor("Panel.background"));
        }
        if (s1 < s2) {
            rpsForm.txtPlayerTwoScore.setBackground(Color.getHSBColor(0.394f, 0.984f, 0.667f));
            rpsForm.txtPlayerOneScore.setBackground(UIManager.getColor("Panel.background"));
            rpsForm.txtTieCount.setBackground(UIManager.getColor("Panel.background"));
        }
        if (s1 == s2) {
            rpsForm.txtTieCount.setBackground(Color.getHSBColor(0.394f, 0.984f, 0.667f));
            rpsForm.txtPlayerOneScore.setBackground(UIManager.getColor("Panel.background"));
            rpsForm.txtPlayerTwoScore.setBackground(UIManager.getColor("Panel.background"));
        }

        rpsForm.txtPlayerOneScore.paintAll(rpsForm.txtPlayerOneScore.getGraphics());
        rpsForm.txtTieCount.paintAll(rpsForm.txtTieCount.getGraphics());
        rpsForm.txtPlayerTwoScore.paintAll(rpsForm.txtPlayerTwoScore.getGraphics());

        int spread = Math.abs(s1 - s2);
        int turnsRemaining = turnCount - currentTurn;
        rpsBean.setTurnsRemaining(String.valueOf(turnsRemaining));
        rpsForm.turnProgress.setValue(turnsRemaining);
        rpsForm.turnProgress.setString(String.valueOf(turnsRemaining));
        rpsBean.setSpread(String.valueOf(spread));
        rpsForm.setData(rpsBean);
        rpsForm.txtSpread.paintAll(rpsForm.txtSpread.getGraphics());
        rpsForm.turnProgress.paintAll(rpsForm.turnProgress.getGraphics());
    }

    private Move getMove(String command) {
        return Move.getMove(runCommand(command));
    }

    private String runCommand(String command) {
        BufferedReader in = null;
        BufferedReader err = null;
        try {
            Process p = Runtime.getRuntime().exec(command);
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            err = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String move = in.readLine();
            return move;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(rpsForm.basePanel,
                    e.getMessage(),
                    "IO Exception",
                    JOptionPane.ERROR_MESSAGE);

            try {
                String l = err.readLine();
                do {
                    System.err.println(l);
                    l = err.readLine();
                } while (l != null);
            } catch (IOException e1) {
                System.err.println("who cares.");
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (err != null) {
                    err.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("this sucks");
            }
        }

    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }
}
