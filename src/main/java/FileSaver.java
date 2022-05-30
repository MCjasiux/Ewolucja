import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class FileSaver {
    FileWriter writer;
    private final String absolutePath;
    private final String fileName;

    public FileSaver(String fileName) {
        this.fileName = fileName;
        this.absolutePath = new File(".").getAbsolutePath();
        try {
            writer = new FileWriter(fileName);
            writer.write("Day;Animals;Plants;Average energy;Average death age;Average number of children;Genotype 0;Genotype 2;Genotype 2;Genotype 3;Genotype 4;Genotype 5;Genotype 6;Genotype 7\n");
            writer.flush();
        } catch (FileNotFoundException e) {
            System.out.println("File " + fileName + " not found in " + absolutePath);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An error occurred while opening " + fileName);
            e.printStackTrace();
        }
    }

    public void saveRecord(MapInfo info) {
        String record = info.getMapAge() + ";";
        record += info.getAnimalCount() + ";";
        record += info.getGrassCount() + ";";
        record += info.getAverageEnergy() + ";";
        record += info.getAverageDeathAge() + ";";
        record += info.getAverageChildrenCount() + ";";

        int[] genotypes = info.getGenotypes();
        for (int i = 0; i < genotypes.length; i++) {
            record += genotypes[i] + ";";
        }
        record += "\n";
        //  System.out.println(record);
        try {
            writer.write(record);
            writer.flush();
        } catch (IOException e) {
            System.out.println("An error occured (" + fileName + ")");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An error occured (" + fileName + ")");
            e.printStackTrace();
        }
    }

}
