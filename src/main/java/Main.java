import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.util.*;

public class Main extends Application {
    private static MapVisualizer visualizer;

    private static JungleMap[] maps;
    private static Scene scene;

    public static void main(String[] args) {
        int primalAnimals;
        try {
            primalAnimals = Integer.parseInt(args[0]);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No argument supplied");
        } catch (Exception e) {
            System.out.println("Invalid argument :" + args[0]);
        } finally {
            primalAnimals = 6;
        }

        FileLoader fileLoader = new FileLoader();
        ArrayList<String> nameSet = fileLoader.loadNames("names.txt");    //pobieranie zestawu imion dla zwierzaków
        MapProperties preferences = fileLoader.loadPreferences("parameters.json");  //pobieranie ustawieñ
        JungleMap map1 = new JungleMap(preferences, nameSet, "statistics1.csv");
        JungleMap map2 = new JungleMap(preferences, nameSet, "statistics2.csv");

        maps = new JungleMap[]{map1, map2};
        for (JungleMap map : maps
        ) {
            List<Vector2d> spots = map.watcher.getFreeSpots();
            for (int i = 0; i < primalAnimals; i++) {
                PrimalAnimal primalAnimal = new PrimalAnimal(spots.get(i), map);
                map.placeAnimal(primalAnimal);
            }
            map.visualizer.refresh();
            map.visualizer.renderControls();
        }

        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Ewolucja");

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        for (JungleMap map : maps
                        ) {
                            if (map.controls.isRunning) {
                                map.tick();
                                map.visualizer.refresh();
                                if (map.controls.isSaving) {
                                    map.saver.saveRecord(map.info);
                                }
                            }
                        }

                    }
                });

            }
        }, 0, 100);

        HBox hbox = new HBox(20);
        for (JungleMap map : maps
        ) {
            VBox mapVisualized = map.visualizer.getFullMap();
            hbox.getChildren().addAll(mapVisualized, map.controls.getControls());
            hbox.setMargin(mapVisualized, new Insets(10));
        }

        scene = new Scene(hbox, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
