package fossteroids.main;

import fossteroids.gamecomponents.Asteroid;
import fossteroids.gamecomponents.Projectile;
import fossteroids.gamecomponents.Spaceship;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AsteroidsApplication extends Application {

    private final static int PLAYFIELDWIDTH = 800;
    private final static int PLAYFIELDHEIGHT = 450;
    private static double difficulty = 0.005;

    public static void main(String[] args) {

        launch(AsteroidsApplication.class);
    }

    public static int partsCompleted() {
        // State how many parts you have completed using the return value of this method
        return 4;
    }

    @Override
    public void start(Stage stage) throws Exception {
        

        Pane pane = new Pane();
        pane.setPrefSize(PLAYFIELDWIDTH, PLAYFIELDHEIGHT);
        pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(pane);
        
        Label text = new Label("Points: 0");
        text.setFont(new Font(40));
        text.setTextFill(Color.WHITE);
        
        pane.getChildren().add(text);

        AtomicInteger points = new AtomicInteger();

        Spaceship spaceship = new Spaceship(PLAYFIELDWIDTH / 2, PLAYFIELDHEIGHT / 2);
        pane.getChildren().add(spaceship.getComponent());

        List<Asteroid> asteroids = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Random rnd = new Random();
            Asteroid asteroid = new Asteroid(rnd.nextInt(PLAYFIELDWIDTH / 3), rnd.nextInt(PLAYFIELDHEIGHT / 3));
            asteroids.add(asteroid);
        }
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getComponent()));

        Map<KeyCode, Boolean> pressedKeysMap = new HashMap();
        pressedKeysMap.put(KeyCode.LEFT, Boolean.FALSE);
        pressedKeysMap.put(KeyCode.RIGHT, Boolean.FALSE);
        scene.setOnKeyPressed(event -> {
            KeyCode kc = event.getCode();
            pressedKeysMap.put(kc, Boolean.TRUE);
        });
        scene.setOnKeyReleased(event -> {
            KeyCode kc = event.getCode();
            pressedKeysMap.put(kc, Boolean.FALSE);
        });

        List<Projectile> projectiles = new ArrayList<>();

        new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (Math.random() < difficulty) {
                    Asteroid asteroid = new Asteroid(PLAYFIELDWIDTH, PLAYFIELDHEIGHT);
                    if (!asteroid.collide(spaceship)) {
                        asteroids.add(asteroid);
                        pane.getChildren().add(asteroid.getComponent());
                    }
                }

                if (pressedKeysMap.getOrDefault(KeyCode.LEFT, false)) {
                    spaceship.turnLeft();
                }

                if (pressedKeysMap.getOrDefault(KeyCode.RIGHT, false)) {
                    spaceship.turnRight();
                }

                if (pressedKeysMap.getOrDefault(KeyCode.SPACE, false)) {
                    spaceship.accelerate();
                }

                if (pressedKeysMap.getOrDefault(KeyCode.CONTROL, false)) {
                    spaceship.decelerate();
                }

                if (pressedKeysMap.getOrDefault(KeyCode.D, false) && projectiles.size() < 1) {
                    // we shoot
                    Projectile projectile = new Projectile((int) spaceship.getComponent().getTranslateX(), (int) spaceship.getComponent().getTranslateY());
                    projectile.getComponent().setRotate(spaceship.getComponent().getRotate());
                    projectiles.add(projectile);

                    projectile.accelerate();
                    projectile.setMovement(projectile.getMovement().normalize().multiply(3));

                    pane.getChildren().add(projectile.getComponent());
                }

                spaceship.move();

                asteroids.forEach(asteroid
                        -> {
                    asteroid.move();
                    if (spaceship.collide(asteroid)) {
                        stop();
                    }
                }
                );

                projectiles.forEach(projectile
                        -> projectile.move());

                projectiles.forEach(projectile -> {
                    asteroids.forEach(asteroid -> {
                        if (projectile.collide(asteroid)) {
                            projectile.setAlive(false);
                            asteroid.setAlive(false);
                        }
                    });
                    if (!projectile.isAlive()) {
                        text.setText("Points: " + points.addAndGet(1000));
                        difficulty = difficulty + 0.001;
                    }

                });

                projectiles.stream()
                        .filter(projectile -> !projectile.isAlive())
                        .forEach(projectile -> pane.getChildren().remove(projectile.getComponent()));
                projectiles.removeAll(projectiles.stream()
                        .filter(projectile -> !projectile.isAlive())
                        .collect(Collectors.toList()));

                asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .forEach(asteroid -> pane.getChildren().remove(asteroid.getComponent()));
                asteroids.removeAll(asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .collect(Collectors.toList()));

            }
        }.start();

        stage.setTitle("FOSSteroids");
        stage.setScene(scene);
        stage.show();
    }

    public static int getPlayfieldWidth() {
        return PLAYFIELDWIDTH;
    }

    public static int getPlayfieldHeight() {
        return PLAYFIELDHEIGHT;
    }

}
