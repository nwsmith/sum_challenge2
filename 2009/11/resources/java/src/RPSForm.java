import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: nsmith
 * Date: Dec 1, 2009
 * Time: 9:54:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class RPSForm {
    public JTextPane txtPlayerOneScore;
    public JTextPane txtPlayerTwoScore;
    public JPanel basePanel;
    protected JTextField turnCount;
    private JButton runButton;
    private JButton resetButton;
    public JTextPane txtTieCount;
    protected JTextPane txtSpread;
    private JComboBox cmbProgramOne;
    private JComboBox cmbPlayerOne;
    private JComboBox cmbProgramTwo;
    private JComboBox cmbPlayerTwo;
    public JProgressBar turnProgress;
    private RPSRunner runner;

    public RPSForm() {
        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (turnCount.getText() == null || "".equals(turnCount.getText())) {
                    JOptionPane.showMessageDialog(basePanel,
                            "Turn count is missing",
                            "Oops",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    turnProgress.setMaximum(Integer.valueOf(turnCount.getText()));
                    turnProgress.setValue(Integer.valueOf(turnCount.getText()));
                    runner.run();
                }
            }
        });
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });

        cmbPlayerOne.addActionListener(new ComboBoxActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultComboBoxModel playerModel = (DefaultComboBoxModel) cmbPlayerOne.getModel();
                DefaultComboBoxModel programModel = (DefaultComboBoxModel) cmbProgramOne.getModel();
                updateDropdowns(playerModel, programModel);
            }
        });
        cmbPlayerTwo.addActionListener(new ComboBoxActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultComboBoxModel playerModel = (DefaultComboBoxModel) cmbPlayerTwo.getModel();
                DefaultComboBoxModel programModel = (DefaultComboBoxModel) cmbProgramTwo.getModel();
                updateDropdowns(playerModel, programModel);
            }
        });
    }

    public void setData(RPSBean data) {
        txtPlayerOneScore.setText(data.getPlayerOneScore());
        txtPlayerTwoScore.setText(data.getPlayerTwoScore());
        txtTieCount.setText(data.getTieCount());
        txtSpread.setText(data.getSpread());
        turnCount.setText(data.getTurnCount());
    }

    public void getData(RPSBean data) {
        data.setPlayerOne(cmbPlayerOne.getModel().getSelectedItem().toString());
        data.setProgramOne(cmbProgramOne.getModel().getSelectedItem().toString());
        data.setPlayerTwo(cmbPlayerTwo.getModel().getSelectedItem().toString());
        data.setProgramTwo(cmbProgramTwo.getModel().getSelectedItem().toString());
        data.setPlayerOneScore(txtPlayerOneScore.getText());
        data.setPlayerTwoScore(txtPlayerTwoScore.getText());
        data.setTieCount(txtTieCount.getText());
        data.setSpread(txtSpread.getText());
        data.setTurnCount(turnCount.getText());
    }

    public boolean isModified(RPSBean data) {
        if (txtPlayerOneScore.getText() != null ? !txtPlayerOneScore.getText().equals(data.getPlayerOneScore()) : data.getPlayerOneScore() != null)
            return true;
        if (txtPlayerTwoScore.getText() != null ? !txtPlayerTwoScore.getText().equals(data.getPlayerTwoScore()) : data.getPlayerTwoScore() != null)
            return true;
        if (txtTieCount.getText() != null ? !txtTieCount.getText().equals(data.getTieCount()) : data.getTieCount() != null)
            return true;
        if (txtSpread.getText() != null ? !txtSpread.getText().equals(data.getSpread()) : data.getSpread() != null)
            return true;
        if (turnCount.getText() != null ? !turnCount.getText().equals(data.getTurnCount()) : data.getTurnCount() != null)
            return true;
        return false;
    }

    private abstract static class ComboBoxActionListener implements ActionListener {
        protected void updateDropdowns(DefaultComboBoxModel playerModel, DefaultComboBoxModel programModel) {
            Object selectedPlayer = playerModel.getSelectedItem();
            int i = playerModel.getIndexOf(selectedPlayer);
            Object selectedProgram = programModel.getElementAt(i);
            programModel.setSelectedItem(selectedProgram);
        }

    }

    protected void populateDropdowns(Properties dropdownInfo) {
        DefaultComboBoxModel playerOneModel = new DefaultComboBoxModel();
        DefaultComboBoxModel playerTwoModel = new DefaultComboBoxModel();
        DefaultComboBoxModel programOneModel = new DefaultComboBoxModel();
        DefaultComboBoxModel programTwoModel = new DefaultComboBoxModel();

        List<Map.Entry<Object, Object>> entries = new ArrayList<Map.Entry<Object, Object>>();
        entries.addAll(dropdownInfo.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<Object, Object>>() {
            public int compare(Map.Entry<Object, Object> o1, Map.Entry<Object, Object> o2) {
                return ((String) o1.getKey()).compareTo((String) o2.getKey());
            }
        });

        for (Map.Entry<Object, Object> entry : entries) {
            playerOneModel.addElement(entry.getKey());
            programOneModel.addElement(entry.getValue());
            playerTwoModel.addElement(entry.getKey());
            programTwoModel.addElement(entry.getValue());
        }

        cmbPlayerOne.setModel(playerOneModel);
        cmbPlayerTwo.setModel(playerTwoModel);
        cmbProgramOne.setModel(programOneModel);
        cmbProgramTwo.setModel(programTwoModel);
    }

    protected void setRunner(RPSRunner runner) {
        this.runner = runner;
    }

    protected void reset() {
        RPSBean bean = new RPSBean();
        getData(bean);
        bean.setPlayerOneScore("0");
        bean.setPlayerTwoScore("0");
        bean.setTieCount("0");
        bean.setSpread("0");
        setData(bean);
        txtPlayerOneScore.setBackground(UIManager.getColor("Panel.background"));
        txtPlayerTwoScore.setBackground(UIManager.getColor("Panel.background"));
        txtTieCount.setBackground(UIManager.getColor("Panel.backgroun"));
    }

    public void setPlayerOneWinner() {
        txtPlayerOneScore.setBackground(Color.getHSBColor(0.394f, 0.984f, 0.667f));
        txtPlayerTwoScore.setBackground(Color.RED);
    }

    public void setPlayerTwoWinner() {
        txtPlayerOneScore.setBackground(Color.RED);
        txtPlayerTwoScore.setBackground(Color.getHSBColor(0.394f, 0.984f, 0.667f));
    }

    public void setTie() {
        txtPlayerOneScore.setBackground(Color.getHSBColor(0.394f, 0.984f, 0.667f));
        txtPlayerTwoScore.setBackground(Color.getHSBColor(0.394f, 0.984f, 0.667f));
    }

    private void createUIComponents() {
//	txtPlayerOneScore = new JTextPane();
//	txtPlayerOneScore.setDoubleBuffered(true);
    }

}
