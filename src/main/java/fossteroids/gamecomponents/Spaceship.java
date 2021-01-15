/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fossteroids.gamecomponents;

import javafx.scene.shape.Polygon;

/**
 *
 * @author milos
 */
public class Spaceship extends AbstractComponent{
    
    public Spaceship(int x, int y) {
        super(new Polygon(-5, -5, 10, 0, -5, 5), x, y);
    }
    
}
