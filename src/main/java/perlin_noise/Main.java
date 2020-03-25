package perlin_noise;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import perlin_noise.worldgen.World;

/**
 * Created by theod on 18-12-2016.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();

        WritableImage image = new WritableImage(640, 640);

        // PerlinNoise gen = new PerlinNoise(true);
        // PerlinNoise gen = new PerlinNoise(16,9);
        // PerlinNoiseOctaved gen = new PerlinNoiseOctaved(5, 5, 5);
        // gen.generateNoise(image.getPixelWriter(), 640,640);
        World world = new World(5, 5, 7, .01, .7);
        world.generate(640, 640);
        world.draw(image.getPixelWriter());

        ImageView view = new ImageView(image);

        root.getChildren().add(view);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Perlin Noise");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
