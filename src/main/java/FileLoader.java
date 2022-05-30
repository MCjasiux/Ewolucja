import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;

public class FileLoader {
    private final String absolutePath;

    public FileLoader() {
        this.absolutePath = new File(".").getAbsolutePath();
    }

    public ArrayList<String> loadNames(String namesFileName) {
        File namesFile = new File(namesFileName);
        ArrayList<String> out = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new FileReader(namesFileName));
            scanner.useDelimiter("\\n");

            if (!namesFile.canRead()) {
                System.out.println("Undable to read file " + namesFileName);
            }

            while (scanner.hasNextLine()) {
                out.add(scanner.nextLine());
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File \"" + namesFileName + "\" not found in " + absolutePath);
        }
        return out;
    }

    public MapProperties loadPreferences(String parametersFileName) {
        MapProperties out;
        File parametersFile = new File(parametersFileName);
        JSONParser parser = new JSONParser();
        MapProperties defaultProperties = new MapProperties(12, 12, 100, 1, 10, (float) 0.5, (float) 0.5, 1, 6);
        try {
            if (!parametersFile.canRead()) {
                System.out.println("Unable to read " + parametersFileName);
            }
            Reader reader = new FileReader(parametersFileName);
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            int height = (int) (long) jsonObject.get("height");
            int width = (int) (long) jsonObject.get("width");
            int startEnergy = (int) (long) jsonObject.get("startEnergy");
            int moveEnergy = (int) (long) jsonObject.get("moveEnergy");
            int plantEnergy = (int) (long) jsonObject.get("plantEnergy");
            int growthRate = (int) (long) jsonObject.get("growthRate");
            int resolution = (int) (long) jsonObject.get("resolution");
            float jungleRatio = (float) (double) jsonObject.get("jungleRatio");
            float reproductionRatio = (float) (double) jsonObject.get("reproductionRatio");
            out = new MapProperties(height, width, startEnergy, moveEnergy, plantEnergy, jungleRatio, reproductionRatio, growthRate, resolution);
            return out;
        } catch (FileNotFoundException e) {
            System.out.println("File \"" + parametersFileName + "\" not found in " + absolutePath);
            return defaultProperties;
        } catch (ParseException e) {
            System.out.println("Parse error in " + parametersFileName);
            e.printStackTrace();
            return defaultProperties;
        } catch (IOException e) {
            System.out.println("Error while reading " + parametersFileName);
            e.printStackTrace();
            return defaultProperties;
        }

    }
}
