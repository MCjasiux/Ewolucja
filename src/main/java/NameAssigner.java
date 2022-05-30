import java.util.ArrayList;
import java.util.Random;

public class NameAssigner {
    private final ArrayList<String> names;
    private final Random random;

    public NameAssigner(ArrayList<String> names) {
        this.names = names;
        random = new Random();
    }

    public String getName() {
        return this.names.get(random.nextInt(names.size()));
    }
}
