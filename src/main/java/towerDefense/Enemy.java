package towerDefense;

import java.awt.Color;
import java.awt.Graphics;


public class Enemy {

    public Vector3d pos;
    public Vector3d movement;
    public boolean alive = true;
    public float progress = 0; // Track the enemy's progress along the path
    public boolean gameLoose = false;
    public int health;
    public int currentLevel;

    private float speed;
    private Color color;
    private int waypointIndex = 0; // Track the current waypoint index for movement
    private final Color colorBrown = new Color(165, 42, 42);
    private final Color colorSkyBlue = new Color(135, 206, 235);


    static final Vector3d[] WAYPOINTS = {
        new Vector3d(-20, 500, 0), // Start (links outside)
        new Vector3d(220, 500, 0), // right
        new Vector3d(220, 60, 0),  // up
        new Vector3d(580, 60, 0),  // right
        new Vector3d(580, 220, 0), // down
        new Vector3d(460, 220, 0), // left
        new Vector3d(460, 420, 0), // down
        new Vector3d(660, 420, 0), // right
        new Vector3d(660, 620, 0),  // down (outside = finish)
        new Vector3d(0, 0, 0),  // extra waypoint - might not be too clean but it works ;)
    };

    public Enemy(TD_Colors color, int currentLevel) {
        this.pos = WAYPOINTS[0].cpy(); // Start y position
        this.color = color.color;
        setSpeedAndHealth();
        this.movement = WAYPOINTS[1].cpy().nor().scl(speed);
        this.currentLevel = currentLevel;
    }
    
    private void setSpeedAndHealth() {
        if (color == Color.RED) {
            this.health = 1;
            this.speed = 1.8f;
        } else if (color == Color.ORANGE) {
            this.health = 1;
            this.speed = 2.5f;
        } else if (color == Color.YELLOW) {
            this.health = 1;
            this.speed = 3.2f;
        } else if (color == Color.BLUE) {
            this.health = 1;
            this.speed = 4.0f;
        } else if (color.equals(colorBrown)) {
            this.health = 10;
            this.speed = 2.3f;
        } else if (color.equals(colorSkyBlue)) {
            this.health = 200;
            this.speed = 1.4f;
        } else if (color == Color.DARK_GRAY) {
            this.health = 300;
            this.speed = 1.0f;
        }
        this.speed *= (1.0f + (0.01f * this.currentLevel));
    }

    public void move() {
        progress += speed;
        Vector3d destination = WAYPOINTS[waypointIndex].cpy();

        double distance = pos.dst(destination);

        if (distance < speed) {
            // Move to the next waypoint
            pos = destination;
            waypointIndex++;
            if (waypointIndex >= (WAYPOINTS.length - 1)) {
                GamePanel.gameOver = true;
                GamePanel.looseGame = true;
                alive = false; // Enemy has reached the end of the path
            } else {
                movement = WAYPOINTS[waypointIndex].cpy().sub(pos).nor().scl(speed);
            }
        } else {
            // Move towards the current waypoint
            pos.add(movement);
        }

    }

    public int hit(int bulletHealth) {
        
        // Handle what happens when the enemy is hit by a bullet (e.g., reduce health, check for death)
        if (color == Color.RED) {
            alive = false;          // Enemy is destroyed
            bulletHealth--;
        } else if (color == Color.ORANGE) {
            color = Color.RED;      // Further damage indication...
            bulletHealth--;
        } else if (color == Color.YELLOW) {
            color = Color.ORANGE;
            bulletHealth--;
        } else if (color == Color.BLUE) {
            color = Color.YELLOW;
            bulletHealth--;
        } else if (color.equals(colorBrown)) {
            color = Color.BLUE;
            bulletHealth--;
        } else if (color.equals(colorSkyBlue)) {
            if (this.health > 1) {
                this.health--;
                bulletHealth--;
            } else {
                alive = false;
                bulletHealth--;
            }
            return bulletHealth;
        } else if (color == Color.DARK_GRAY) {
            if (this.health > 0 && bulletHealth >= 2) {
                this.health -= bulletHealth + 1;
                bulletHealth = 0;
                return bulletHealth;
            } else if (this.health <= 0) {
                color = Color.RED;
                bulletHealth--;
            }
        }
        setSpeedAndHealth(); // Update speed based on new color (health) after being hit
        return bulletHealth;

    }

    public void draw(Graphics g) {
        g.setColor(color);
        int drawX = (int) pos.x - 15;  // Center the circle at (x, y)
        int drawY = (int) pos.y - 15;  // Center the circle at (x, y)
        g.fillOval(drawX, drawY, 30, 30);
    }
}