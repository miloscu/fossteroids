/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fossteroids.gamecomponents;

import fossteroids.main.AsteroidsApplication;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/**
 *
 * @author milos
 */
public abstract class AbstractComponent {

    private Polygon character;
    private Point2D movement;
    private boolean alive;

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public AbstractComponent(Polygon polygon, int x, int y) {
        this.character = polygon;
        this.character.setTranslateX(x);
        this.character.setTranslateY(y);
        this.character.setFill(Color.WHITE);
        
        this.alive = true;

        this.movement = new Point2D(0, 0);
        
    }

    public Polygon getComponent() {
        return character;
    }

    public void turnLeft() {
        this.character.setRotate(this.character.getRotate() - 5);
    }

    public void turnRight() {
        this.character.setRotate(this.character.getRotate() + 5);
    }

    public void move() {
        this.character.setTranslateX(this.character.getTranslateX() + this.movement.getX());
        this.character.setTranslateY(this.character.getTranslateY() + this.movement.getY());
        
        if (this.character.getTranslateX() < 0) {
            this.character.setTranslateX(this.character.getTranslateX() + AsteroidsApplication.getPlayfieldWidth());
        }

        if (this.character.getTranslateX() > AsteroidsApplication.getPlayfieldWidth()) {
            this.character.setTranslateX(this.character.getTranslateX() % AsteroidsApplication.getPlayfieldWidth());
        }

        if (this.character.getTranslateY() < 0) {
            this.character.setTranslateY(this.character.getTranslateY() + AsteroidsApplication.getPlayfieldHeight());
        }

        if (this.character.getTranslateY() > AsteroidsApplication.getPlayfieldHeight()) {
            this.character.setTranslateY(this.character.getTranslateY() % AsteroidsApplication.getPlayfieldHeight());
        }
    
    }

    public void accelerate() {
        double changeX = Math.cos(Math.toRadians(this.character.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.character.getRotate()));

        changeX *= 0.06;
        changeY *= 0.06;

        this.movement = this.movement.add(changeX, changeY); //= new Point2D(changeX, changeY) for momentumless steering;
    }

    public void decelerate() {
        this.movement = new Point2D(this.movement.getX() / 1.05, this.movement.getY() / 1.05); //= new Point2D(changeX, changeY) for momentumless steering;

        if (Math.abs(this.movement.getX()) < 0.001 && Math.abs(this.movement.getX()) < 0.001) {
            this.movement = new Point2D(0, 0);
        }
    }

    public boolean collide(AbstractComponent other) {
        Shape collisionArea = Shape.intersect(this.character, other.getComponent());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }

    public Point2D getMovement() {
        return movement;
    }

    public void setMovement(Point2D movement) {
        this.movement = movement;
    }

}
