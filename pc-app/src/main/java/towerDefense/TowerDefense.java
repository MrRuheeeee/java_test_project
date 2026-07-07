package towerDefense;

import javax.swing.*;


public class TowerDefense {

    public void start() {
        GamePanel panel = new GamePanel();
        GameLogic logic = new GameLogic(panel);
        panel.setLogic(logic);

        JFrame frame = new JFrame("Tower Defense");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        logic.start();
    }
}