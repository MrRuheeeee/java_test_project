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

    public Enemy(int health, int currentLevel) {
        this.pos = WAYPOINTS[0].cpy(); // Start y position
        this.health = health;
        this.movement = WAYPOINTS[1].cpy().nor().scl(speed);
        this.currentLevel = currentLevel;
        setSpeedAndColor();
    }
    
    private void setSpeedAndColor() {
        if (this.health <= 0) {
            this.alive = false;
        } else if (this.health == 1) {
            this.color = Color.RED;
            this.speed = 1.8f;
        } else if (this.health == 2) {
            this.color = Color.ORANGE;
            this.speed = 2.2f;
        } else if (this.health == 3) {
            this.color = Color.YELLOW;
            this.speed = 2.8f;
        } else if (this.health == 4) {
            this.color = Color.BLUE;
            this.speed = 4.0f;
        } else if (this.health <= (this.currentLevel / 2)) {
            this.color = colorBrown;
            this.speed = 2.3f;
        } else if (this.health <= (10 * (this.currentLevel + 1))) {
            this.color = colorSkyBlue;
            this.speed = 1.4f;
        } else if (this.health <= (20 * (this.currentLevel + 1))) {
            this.color = Color.DARK_GRAY;
            this.speed = 1.0f;
        } else {
            // shouldnt exist, but just in case its white and stands out
            this.color = Color.WHITE;
            this.speed = 0.5f;
        }
        System.out.println("enemy health: " + this.health); 
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

    public void hit(int bulletDamage) {
        this.health -= bulletDamage;
        System.out.println(this.health);
        setSpeedAndColor(); // Update speed & color       

    }

    public void draw(Graphics g) {
        g.setColor(this.color);
        int drawX = (int) pos.x - 15;  // Center the circle at (x, y)
        int drawY = (int) pos.y - 15;  // Center the circle at (x, y)
        g.fillOval(drawX, drawY, 30, 30);
    }
}