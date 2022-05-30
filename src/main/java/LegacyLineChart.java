import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class LegacyLineChart {
    private final NumberAxis xAxis;
    private final NumberAxis yAxis;
    private final LineChart<Number, Number> lineChart;
    private XYChart.Series children;
    private XYChart.Series descedants;

    public LegacyLineChart() {
        xAxis = new NumberAxis();
        xAxis.setLabel("Jungle age");
        yAxis = new NumberAxis();
        lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        children = new XYChart.Series();
        children.setName("Children");
        descedants = new XYChart.Series();
        descedants.setName("Descendants");
        lineChart.getData().addAll(children, descedants);
        lineChart.setCreateSymbols(false);
        xAxis.setForceZeroInRange(false);

    }

    public void addData(int day, int childrenCount, int descendantsCount) {
        children.getData().add(new XYChart.Data(day, childrenCount));
        descedants.getData().add(new XYChart.Data(day, descendantsCount));
    }

    public LineChart getLineChart() {
        return lineChart;
    }

    public void hide() {
        lineChart.setVisible(false);
    }

    public void show() {
        lineChart.setVisible(true);
    }

    public void clear() {
        children.getData().clear();
        descedants.getData().clear();
    }
}
