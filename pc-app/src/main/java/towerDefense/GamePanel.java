package towerDefense;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.*;
import towerDefense.towers.ArrowTower;
import towerDefense.towers.CannonTower;
import towerDefense.towers.MagicTower;
import towerDefense.towers.SuperTower;
import towerDefense.towers.Tower;

public class GamePanel extends JPanel {

    // private static final Color BUTTON_COLOR = 

    public boolean playButton = false;
    public boolean doubleSpeed = false;

    private static final int GRID_SIZE = 40;
    private final boolean[][] occupied;
    private BufferedImage backgroundImage;
    private final ArrayList<Rectangle> pathRects;
    private Tower selectedTowerRange = null;
    private boolean placeMode = false;
    private boolean placeSwitchOn = false;
    private String activeTower;
    
    public static boolean gameOver = false;
    public static boolean looseGame = false;

    private GameLogic logic;

    public GamePanel() {
        setLayout(null);
        setPreferredSize(new Dimension(800, 600));
        occupied = new boolean[800 / GRID_SIZE][600 / GRID_SIZE];
        pathRects = new ArrayList<>();
        initPathRects();
        createBackgroundImage();
        markPathOccupied();
        markMenuOccupied();
        
        // region Arrow Tower Button
        JButton buyArrowTowerButton = menuBuyTowerButton(Color.RED, "Arrow");
        buyArrowTowerButton.setBounds(655, 45, 130, 30);
        buyArrowTowerButton.addActionListener(_ -> {
            if (Objects.equals(activeTower, "Arrow")) {
                placeMode = !placeMode;
            }
            else {
                placeMode = true;
            }
            if (placeMode) {
                activeTower = "Arrow";
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Objects.equals(activeTower, "Arrow") && placeMode) {
                    placeTower(e.getX(), e.getY());
                }
            }
        });
        this.add(buyArrowTowerButton);
        // endregion

        // region Cannon Tower Button
        JButton buyCannonTowerButton = menuBuyTowerButton(Color.LIGHT_GRAY, "Cannon");
        buyCannonTowerButton.setBounds(655, 85, 130, 30);
        buyCannonTowerButton.addActionListener(_ -> {
            if (Objects.equals(activeTower, "Cannon")) {
                placeMode = !placeMode;
            }
            else {
                placeMode = true;
            }
            if (placeMode) {
                activeTower = "Cannon";
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Objects.equals(activeTower, "Cannon") && placeMode) {
                    placeTower(e.getX(), e.getY());
                }
            }
        });
        this.add(buyCannonTowerButton);
        // endregion

        // region Magic Tower Button
        JButton buyMagicTowerButton = menuBuyTowerButton(Color.BLUE, "Magic");
        buyMagicTowerButton.setBounds(655, 125, 130, 30);
        buyMagicTowerButton.addActionListener(_ -> {
            if (Objects.equals(activeTower, "Magic")) {
                placeMode = !placeMode;
            }
            else {
                placeMode = true;
            }
            if (placeMode) {
                activeTower = "Magic";
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Objects.equals(activeTower, "Magic") && placeMode) {
                    placeTower(e.getX(), e.getY());
                }
            }
        });
        this.add(buyMagicTowerButton);
        // endregion

        // region Super Tower Button
        JButton buySuperTowerButton = menuBuyTowerButton(Color.GREEN, "Super");
        buySuperTowerButton.setBounds(655, 165, 130, 30);
        buySuperTowerButton.addActionListener(_ -> {
            if (Objects.equals(activeTower, "Super")) {
                placeMode = !placeMode;
            }
            else {
                placeMode = true;
            }
            if (placeMode) {
                activeTower = "Super";
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Objects.equals(activeTower, "Super") && placeMode) {
                    placeTower(e.getX(), e.getY());
                }
            }
        });
        this.add(buySuperTowerButton);
        // endregion

        // region Buy Tower Toggle Switch
        JToggleButton slideSwitch = getJToggleButton();

        this.add(slideSwitch);
        // endregion

        // region Upgrade Button
        JButton upgradeButton = getJButton();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Objects.equals(activeTower, "upgradeButton") && placeMode) {
                    // upgrade logic can be put heree lol
                    System.out.println("upgrade");
                    upgradeTower(e.getX(), e.getY());
                }
            }
        });
        this.add(upgradeButton);

        // endregion

        // region Sell Button
        JButton sellButton = getButton();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Objects.equals(activeTower, "sellButton") && placeMode) {
                    sellTower(e.getX(), e.getY());
                }
            }
        });
        this.add(sellButton);
        // endregion

        // region Start/Stop Button
        JButton startStopButton = getStartStopButton();

        this.add(startStopButton);
        // endregion

        // region Speed Control Button
        JButton speedControlButton = getSpeedControlButton();

        this.add(speedControlButton);
        // endregion

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleTowerClick(e.getX(), e.getY());
            }
        });

    }

    private JToggleButton getJToggleButton() {
        JToggleButton slideSwitch = new JToggleButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2d.setColor(TD_Colors.DARK_GREEN.color);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());

                // Circle
                int circleX = isSelected() ? getWidth() - getHeight() + 2 : 2;
                Color frameColor = (isSelected())
                    ? TD_Colors.DARK_GRAY.color
                    : TD_Colors.DARK_GREEN.color;
                g2d.setColor(frameColor);
                g2d.fillOval(circleX, 2, getHeight() - 4, getHeight() - 4);

                placeSwitchOn = isSelected();

                g2d.dispose();
            }
        };
        slideSwitch.setContentAreaFilled(false);
        slideSwitch.setBorderPainted(false);
        slideSwitch.setFocusPainted(false);
        slideSwitch.setBounds(650, 10, 20, 20); // breiter als hoch
        return slideSwitch;
    }

    private JButton getStartStopButton() {
        JButton startStopButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // background
                g2d.setColor(TD_Colors.DARK_GRAY.color);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // frame;
                g2d.setColor(TD_Colors.DARK_GREEN.color);
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                // form
                g2d.setColor(TD_Colors.DARK_GREEN.color);
                if (playButton) {
                    // x, y, width, height, horizontal-, vertical- roundness of corners
                    g2d.fillRoundRect(6, 6, 24, 24, 8, 8);

                }
                else {
                    int[] xPoints = {7, 7, 30};
                    int[] yPoints = {7, 30, 18};
                    g2d.fillPolygon(xPoints, yPoints, 3);
                }



                g2d.dispose();
                // super.paintComponent(g);
            }
        };
        startStopButton.setContentAreaFilled(false);
        startStopButton.setBorderPainted(false);
        //startStopButton.setFont(new Font("Arial", Font.BOLD, 1));
        startStopButton.setFocusPainted(false);
        startStopButton.setBounds(2, 2, 36, 36);

        startStopButton.addActionListener(_ -> {
            if (playButton) {
                playButton = false;
                logic.timer.stop();
            }
            else {
                playButton = true;
                logic.timer.start();
            }
        });
        return startStopButton;
    }

    private JButton getSpeedControlButton() {
        JButton speedControlButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // background
                g2d.setColor(TD_Colors.DARK_GRAY.color);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // frame
                g2d.setColor(TD_Colors.DARK_GREEN.color);
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                // form
                g2d.setColor(TD_Colors.DARK_GREEN.color);
                String text;
                if (doubleSpeed) {
                    text = "2x";
                }
                else {
                    text = "1x";
                }
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = getWidth() - fm.stringWidth(text) - 8;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(text, textX, textY);

                g2d.dispose();
                // super.paintComponent(g);
            }
        };
        speedControlButton.setContentAreaFilled(false);
        speedControlButton.setBorderPainted(false);
        speedControlButton.setFocusPainted(false);
        speedControlButton.setFont(new Font("Arial", Font.BOLD, 18));
        speedControlButton.setBounds(42, 2, 36, 36);

        speedControlButton.addActionListener(_ -> {
            if (!doubleSpeed) {
                doubleSpeed = true;
                logic.timerSpeed = 16;
                System.out.print("true");
            }
            else {
                doubleSpeed = false;
                logic.timerSpeed = 33;
                System.out.print("false");
            }
            if (logic.timer != null) {
                logic.timer.setDelay(logic.timerSpeed);
            }
        });
        return speedControlButton;
    }

    private JButton getButton() {
        JButton sellButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                if (placeMode && Objects.equals(activeTower, "sellButton")) {
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                }

                // Arrow
                g2d.setColor(TD_Colors.CUSTOM_ORANGE.color);
                int[] xPoints = {11, 7, 15};
                int[] yPoints = {16, 7, 7};
                g2d.fillPolygon(xPoints, yPoints, 3);

                // Frame
                Color frameColor = Color.LIGHT_GRAY;
                g2d.setColor(frameColor);
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                // Text right-aligned
                String text = logic.sellPrice + " $";
                g2d.setFont(getFont());
                g2d.setColor(frameColor);
                FontMetrics fm = g2d.getFontMetrics();
                int textX = getWidth() - fm.stringWidth(text) - 10;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(text, textX, textY);

                g2d.dispose();
                repaint();
            }
        };
        sellButton.setContentAreaFilled(false);
        sellButton.setBorderPainted(false);
        sellButton.setFocusPainted(false);
        sellButton.setFont(new Font("Arial", Font.BOLD, 14));
        sellButton.setForeground(TD_Colors.DARK_GREEN.color);
        sellButton.setBounds(725, 205, 60, 25);
        sellButton.addActionListener(_ -> {
            if (Objects.equals(activeTower, "sellButton")) {
                placeMode = !placeMode;
            }
            else {
                placeMode = true;
            }
            if (placeMode) {
                activeTower = "sellButton";
            }
        });
        return sellButton;
    }

    private JButton getJButton() {
        JButton upgradeButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                if (placeMode && Objects.equals(activeTower, "upgradeButton")) {
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                }

                // Arrow
                g2d.setColor(TD_Colors.DARK_GREEN.color);
                int[] xPoints = {11, 7, 15};
                int[] yPoints = {7, 16, 16};
                g2d.fillPolygon(xPoints, yPoints, 3);

                // Frame
                Color frameColor = (logic != null && logic.money >= logic.upgradePrice)
                    ? TD_Colors.DARK_GREEN.color  // green
                    : TD_Colors.CUSTOM_ORANGE.color; // orange
                g2d.setColor(frameColor);
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                // Text right-aligned
                String text = logic.upgradePrice + " $";
                g2d.setFont(getFont());
                g2d.setColor(frameColor);
                FontMetrics fm = g2d.getFontMetrics();
                int textX = getWidth() - fm.stringWidth(text) - 10;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(text, textX, textY);

                g2d.dispose();
                repaint();
            }
        };
        upgradeButton.setContentAreaFilled(false);
        upgradeButton.setBorderPainted(false);
        upgradeButton.setFocusPainted(false);
        upgradeButton.setFont(new Font("Arial", Font.BOLD, 14));
        upgradeButton.setForeground(TD_Colors.DARK_GREEN.color);
        upgradeButton.setBounds(655, 205, 60, 25);
        upgradeButton.addActionListener(_ -> {
            if (Objects.equals(activeTower, "upgradeButton")) {
                placeMode = !placeMode;
            }
            else {
                placeMode = true;
            }
            if (placeMode) {
                activeTower = "upgradeButton";
            }
        });
        return upgradeButton;
    }

    public void setLogic(GameLogic logic) {
        this.logic = logic;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g.drawImage(backgroundImage, 0, 0, null);
        drawGrid(g);
        if (logic != null) {
            drawObjects(g);
        }
        
        drawMenuBackground(g2d);

        menuMoneyDisplay(g2d);

        levelDisplay(g2d);

        if (gameOver) {
            GameEndScreen(g2d);
        }
        
    }


    private void drawObjects(Graphics g) {
        // only show selected tower range ;) 
        if (selectedTowerRange != null) {
            selectedTowerRange.drawRange(g);
            repaint();
        }
        for (Tower tower : logic.tower) {
            tower.draw(g);
        }
        for (Enemy enemy : logic.enemies) {
            enemy.draw(g);
        }
        for (Bullet bullet : logic.bullets) {
            bullet.draw(g);
        }
    }

    private void initPathRects() {
        pathRects.add(new Rectangle(0, 480, 240, 40));
        pathRects.add(new Rectangle(200, 40, 40, 440));
        pathRects.add(new Rectangle(200, 40, 400, 40));
        pathRects.add(new Rectangle(560, 40, 40, 200));
        pathRects.add(new Rectangle(440, 200, 120, 40));
        pathRects.add(new Rectangle(440, 200, 40, 200));
        pathRects.add(new Rectangle(440, 400, 200, 40));
        pathRects.add(new Rectangle(640, 400, 40, 200));
    }

    private void createBackgroundImage() {
        backgroundImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = backgroundImage.createGraphics();
        drawBackground(g2d);
        g2d.dispose();
    }

    // region mark Occupied Space

    private void markPathOccupied() {
        for (int x = 0; x < 800 / GRID_SIZE; x++) {
            for (int y = 0; y < 600 / GRID_SIZE; y++) {
                int centerX = x * GRID_SIZE + GRID_SIZE / 2;
                int centerY = y * GRID_SIZE + GRID_SIZE / 2;
                for (Rectangle rect : pathRects) {
                    if (rect.contains(centerX, centerY)) {
                        occupied[x][y] = true;
                        break;
                    }
                }
            }
        }
    }

    private void markMenuOccupied() {
        int startCol = 640 / GRID_SIZE;
        int endCol = 800 / GRID_SIZE;
        int startRow = 0; // starting at 0
        int endRow = 6; // six blocks down

        for (int col = startCol; col < endCol; col++) {
            for (int row = startRow; row < endRow; row++) {
                occupied[col][row] = true;
            }
        }
        // also the Start/Stop Button
        occupied[0][0] = true;
    }

    // endregion

    private void drawGrid(Graphics g) {
        g.setColor(new Color(0, 0, 0, 30)); // transparent black
        for (int x = 0; x < 800; x += GRID_SIZE) {
            g.drawLine(x, 0, x, 600);
        }
        for (int y = 0; y < 600; y += GRID_SIZE) {
            g.drawLine(0, y, 800, y);
        }
    }

    private void drawBackground(Graphics g) {
        g.setColor(new Color(34, 177, 76));
        g.fillRect(0, 0, 800, 600);

        g.setColor(Color.GRAY);
        g.fillRect(0, 480, 240, 40);
        g.fillRect(200, 40, 40, 440);
        g.fillRect(200, 40, 400, 40);
        g.fillRect(560, 40, 40, 200);
        g.fillRect(440, 200, 120, 40);
        g.fillRect(440, 200, 40, 200);
        g.fillRect(440, 400, 200, 40);
        g.fillRect(640, 400, 40, 200);
    }

    private void placeTower(int mouseX, int mouseY) {
        int gridX = mouseX / GRID_SIZE;
        int gridY = mouseY / GRID_SIZE;

        if (gridX < 0 || gridX >= 800 || gridY < 0 || gridY >= 600) {
            return;
        }

        if (!occupied[gridX][gridY]) {
            Vector3d tp = new Vector3d(gridX * GRID_SIZE, gridY * GRID_SIZE, 0);
            if (Objects.equals(activeTower, "Arrow") && logic.money >= logic.towerPriceArrow) {
                logic.tower.add(new ArrowTower(tp));
                logic.money -= logic.towerPriceArrow;
                logic.towerPriceArrow += 30;
            } else if (Objects.equals(activeTower, "Cannon") && logic.money >= logic.towerPriceCannon) {
                logic.tower.add(new CannonTower(tp));
                logic.money -= logic.towerPriceCannon;
                logic.towerPriceCannon += 40;
            } else if (Objects.equals(activeTower, "Magic") && logic.money >= logic.towerPriceMagic) {
                logic.tower.add(new MagicTower(tp));
                logic.money -= logic.towerPriceMagic;
                logic.towerPriceMagic += 60;
            } else if (Objects.equals(activeTower, "Super") && logic.money >= logic.towerPriceSuper) {
                logic.tower.add(new SuperTower(tp));
                logic.money -= logic.towerPriceSuper;
                logic.towerPriceSuper += 80;
            } else {
                System.out.println("no money broke boyyyy");
                return;
            }
            occupied[gridX][gridY] = true;
            repaint();
            if (!placeSwitchOn) {
                placeMode = false;
            }
            System.out.println("placed Tower!");
        }
        else {
            System.out.println("Denied Tower placement!");
        }
    }

    private void sellTower(int mouseX, int mouseY) {
        int gridX = mouseX / GRID_SIZE;
        int gridY = mouseY / GRID_SIZE;

        if (occupied[gridX][gridY]) {
            Vector3d tp = new Vector3d(gridX * GRID_SIZE, gridY * GRID_SIZE, 0);
            for (Tower t : logic.tower) {
                // looks for identical position
                if (t.pos.idt(tp)) {
                    logic.money += logic.sellPrice;
                    logic.sellPrice += 10;
                    logic.tower.remove(t);
                    occupied[gridX][gridY] = false;
                    repaint();
                    break;
                }
            }
        }
    }

    private void upgradeTower(int mouseX, int mouseY) {
        int gridX = mouseX / GRID_SIZE;
        int gridY = mouseY / GRID_SIZE;

        if (occupied[gridX][gridY]) {
            Vector3d tp = new Vector3d(gridX * GRID_SIZE, gridY * GRID_SIZE, 0);
            for (Tower t : logic.tower) {
                // looks for identical position
                if (t.pos.idt(tp)) {
                    if (logic.money >= logic.upgradePrice) {
                        if (t.Upgrade()) {
                            logic.money -= logic.upgradePrice;
                            logic.upgradePrice += 1;
                            repaint();
                        }
                    }                    
                    break;
                }
            }
        }
    }

    private void handleTowerClick(int mouseX, int mouseY) {
        int gridX = mouseX / GRID_SIZE;
        int gridY = mouseY / GRID_SIZE;

        if (placeMode) {
            return;
        }

        if (occupied[gridX][gridY]) {
            Vector3d tp = new Vector3d(gridX * GRID_SIZE, gridY * GRID_SIZE, 0);
            for (Tower t : logic.tower) {
                // looks for identical position
                if (t.pos.idt(tp)) {
                    if (selectedTowerRange == t) {
                        selectedTowerRange = null;
                    }else {
                        selectedTowerRange = t;
                    }          
                    break;
                }
            }
        }
    }

    // region Game End Screen
    private void GameEndScreen(Graphics2D g2d) {
        logic.timer.stop();
        for (Component c : getComponents()) {
            c.setVisible(false);
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(0, 0, 0, 167)); // transparent black
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.setFont(new Font("Arial", Font.BOLD, 67));
        String finalText;
        if (GamePanel.looseGame) {
            g2d.setColor(TD_Colors.CUSTOM_ORANGE.color);
            finalText = "YOU LOOSE!";
        } else {
            g2d.setColor(TD_Colors.DARK_GREEN.color);
            finalText = "YOU WIN!";
        }
        FontMetrics fm = g2d.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(finalText)) / 2;
        int textY = getHeight() / 2;
        g2d.drawString(finalText, textX, textY);
        
        g2d.dispose();
    }
    // endregion

    // region Level Display
    private void levelDisplay(Graphics2D g2d) {
        String currentLevelText = "Round: " + logic.currentLevel + "/" + (logic.levels.length - 1);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        //int textWidth = g2d.getFontMetrics().stringWidth(currentLevelText);
        int textHeight = g2d.getFontMetrics().getAscent();
        int levelX = 5; // grows to the left
        int levelY = (GRID_SIZE - textHeight) + 40; // center of grid + next grid
        
        g2d.setColor(Color.BLACK);
        g2d.drawString(currentLevelText, levelX, levelY);
    }
    // endregion

    // region Menu Widgets

    // region Menu Background
    private void drawMenuBackground(Graphics2D g2d) {
        int x = 645, y = 4, w = 150, h = 233, arc = 20;
        
        Color frameColor = Color.BLUE;

        // Background
        g2d.setColor(TD_Colors.DARK_GRAY.color);
        g2d.fillRoundRect(x, y, w, h, arc, arc);

        // Frame
        g2d.setColor(frameColor);
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawRoundRect(x, y, w - 1, h - 1, arc, arc);
    }
    // endregion

    // region Money Display
    private void menuMoneyDisplay(Graphics2D g2d) {
        String moneyText = logic.money + "$";
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        int textWidth = g2d.getFontMetrics().stringWidth(moneyText);
        int textHeight = g2d.getFontMetrics().getAscent();
        int moneyX = 780 - textWidth; // grows to the left
        int moneyY = (GRID_SIZE - textHeight); // center of grid
        
        g2d.setColor(TD_Colors.GOLD_YELLOW.color); // goldyellow
        g2d.drawString(moneyText, moneyX, moneyY);
    }
    // endregion

    // region Button layouts
    private JButton menuBuyTowerButton(Color outerColor, String towerKind) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Get current price dynamically from logic
                int currentPrice = getCurrentTowerPrice(towerKind);

                // Background
                if (placeMode && Objects.equals(activeTower, towerKind)) {
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                }

                // Frame
                Color frameColor = (logic != null && logic.money >= currentPrice)
                    ? TD_Colors.DARK_GREEN.color  // green
                    : TD_Colors.CUSTOM_ORANGE.color; // orange
                g2d.setColor(frameColor);
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                // Tower Icon
                g2d.setColor(outerColor);
                g2d.fillRect(7, 5, 20, 20);
                g2d.setColor(Color.WHITE);
                g2d.fillOval(12, 10, 10, 10);

                // Text right-aligned
                String text = currentPrice + "$";
                g2d.setFont(getFont());
                g2d.setColor(frameColor);
                FontMetrics fm = g2d.getFontMetrics();
                int textX = getWidth() - fm.stringWidth(text) - 10;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(text, textX, textY);

                g2d.dispose();
                repaint();
            }
        };
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(TD_Colors.DARK_GREEN.color);
        return button;
    }
    // endregion

    // endregion

    // Helper method to get current tower price
    private int getCurrentTowerPrice(String towerType) {
        if (logic == null) return 0;
        return switch (towerType) {
            case "Arrow" -> logic.towerPriceArrow;
            case "Cannon" -> logic.towerPriceCannon;
            case "Magic" -> logic.towerPriceMagic;
            case "Super" -> logic.towerPriceSuper;
            default -> 0;
        };
    }
}