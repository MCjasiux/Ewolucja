public class MapProperties {
    public final int height;
    public final int width;
    public final int startEnergy;
    public final int moveEnergy;
    public final int plantEnergy;
    public final float jungleRatio;
    public final int jungleWidth;
    public final int jungleHeight;
    public final float reproductionRatio;   //liczba  reprezentu¹ca u³amek energii startowej poni¿ej której zwierzêta nie mog¹ siê rozmna¿aæ
    public final Vector2d jungleOffset;
    public final int growthRate;    //liczba traw rosn¹cych ka¿dego dnia w d¿ungli i na sawannie
    public final int resolution;    //szerokoœæ i wysokoœæ w pikselach jednego pola
    public final Vector2d size;

    public MapProperties(int height, int width, int startEnergy, int moveEnergy, int plantEnergy, float jungleRatio, float reproductionRatio, int growthRate, int resolution) {
        this.height = height;
        this.width = width;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;
        this.reproductionRatio = reproductionRatio;
        this.growthRate = growthRate;
        this.resolution = resolution;
        this.jungleWidth = (int) (width * jungleRatio);
        this.jungleHeight = (int) (height * jungleRatio);
        this.size = new Vector2d(width, height);
        this.jungleOffset = new Vector2d((int) (width - jungleWidth) / 2, (int) (height - jungleHeight) / 2);
    }
}