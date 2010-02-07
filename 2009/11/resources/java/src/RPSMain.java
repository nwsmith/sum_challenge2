import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: nsmith
 * Date: Dec 1, 2009
 * Time: 10:35:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class RPSMain {
    public static void main(String[] args) {
        JFrame frame = new JFrame("RPSForm");
        RPSForm form = new RPSForm();
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("program.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Cannot find properties.");
        }
        form.populateDropdowns(properties);

        form.txtPlayerOneScore.setDoubleBuffered(true);
        form.txtPlayerTwoScore.setDoubleBuffered(true);
        form.txtTieCount.setDoubleBuffered(true);
        form.txtSpread.setDoubleBuffered(true);
        RPSRunner runner = new RPSRunner();
        RPSBean data = new RPSBean();
        data.setPlayerOneScore("0");
        data.setPlayerTwoScore("0");
        data.setTieCount("0");
        data.setSpread("0");

        form.setData(data);
        runner.setRpsBean(data);
        runner.setRpsForm(form);
        runner.setFrame(frame);
        form.setRunner(runner);

        frame.setContentPane(form.basePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
