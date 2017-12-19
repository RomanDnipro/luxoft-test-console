import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileStatisticForEachLine {
    private File file;
    private ArrayList<LineStatisticItem> lineStatisticItems;

    public FileStatisticForEachLine(File txtFile) throws IOException {
        this.file = txtFile;
        lineStatisticItems = new ArrayList<>();
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(txtFile.getPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            lines = Files.readAllLines(Paths.get(txtFile.getPath()), StandardCharsets.ISO_8859_1);
        }
        for (String line : lines) {
            lineStatisticItems.add(new LineStatisticItem(line));
        }
    }

    @Override
    public String toString() {
        return '\n' +
                "Statistic Of File " + file.getPath() + ":\n" +
                "{\n" +
                lineStatisticItems +
                '}' + '\n' + '\n';
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public ArrayList<LineStatisticItem> getLineStatisticItems() {
        return lineStatisticItems;
    }

    public void setLineStatisticItems(ArrayList<LineStatisticItem> lineStatisticItems) {
        this.lineStatisticItems = lineStatisticItems;
    }
}
