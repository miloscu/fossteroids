/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fossteroids.gamecomponents;

import java.util.Random;
import javafx.scene.shape.Polygon;

/**
 *
 * @author milos
 */
public class Asteroid extends AbstractComponent {
    
    private double rotationalMovement;

    public Asteroid(int x, int y) {
        super(new PolygonFactory().createPolygon(), x, y);

        Random rnd = new Random();

        super.getComponent().setRotate(rnd.nextInt(360));

        int accelerationAmount = 1 + rnd.nextInt(10);
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate();
        }

        this.rotationalMovement = 0.5 - rnd.nextDouble();
    }

    @Override
    public void move() {
        super.move();
        super.getComponent().setRotate(super.getComponent().getRotate() + rotationalMovement);
    }

}
