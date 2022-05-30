import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class Controls {
    private VBox vControlBox;
    private Button startStopButton;
    private Button skipButton;
    private TextField skipTextField;
    private Button saveButton;
    public boolean isRunning;
    public boolean isSaving;
    private final JungleMap map;

    public Controls(JungleMap map) {
        this.map = map;
        this.vControlBox = new VBox(10);
        this.startStopButton = new Button("Start/Pause");
        this.skipButton = new Button("Skip");
        this.saveButton = new Button("Start saving");
        this.skipTextField = new TextField();
        this.isRunning = false;
        this.isSaving = false;
        skipTextField.setPrefColumnCount(5);
        skipTextField.setText("1");
        vControlBox.getChildren().addAll(new VBox(startStopButton, skipButton, saveButton), skipTextField);
        startStopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                isRunning = !isRunning;
            }
        });
        skipButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int daysToSkip = getSkipDaysCount();
                for (int i = 0; i < daysToSkip; i++) {
                    map.tick();
                    if (map.controls.isSaving) {
                        map.saver.saveRecord(map.info);
                    }
                }
                map.visualizer.refresh();
            }
        });
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                isSaving = true;
            }
        });
    }

    public VBox getControls() {
        return vControlBox;
    }

    public int getSkipDaysCount() {
        try {
            return Integer.parseInt(skipTextField.getText());

        } catch (NumberFormatException e) {
            System.out.println("Invalid integer");
            return 0;
        }
    }
}
