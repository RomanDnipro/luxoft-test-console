import java.util.Arrays;

public class LineStatisticItem {

    private String line;
    private String[] words;
    private String longestWord;
    private String shortestWord;
    private int lineLength;
    private double averageWordLength;

    /**
     * Initializes fields by line, using LineStatisticUtility class
     * @param line for statistic it
     */
    public LineStatisticItem(String line) {
        this.line = line;
        words = LineStatisticUtility.splitWordsByWhitespaces(line);
        longestWord = LineStatisticUtility.findLongestWord(words);
        shortestWord = LineStatisticUtility.findShortestWord(words);
        lineLength = line.length();
        averageWordLength = LineStatisticUtility.calcAverageWordsLength(words);
    }

    public String getLine() {
        return line;
    }

    public String[] getWords() {
        return words;
    }

    public String getLongestWord() {
        return longestWord;
    }

    public String getShortestWord() {
        return shortestWord;
    }

    public int getLineLength() {
        return lineLength;
    }

    public double getAverageWordLength() {
        return averageWordLength;
    }

    @Override
    public String toString() {
        return "\n\tstatistic of line: '" + line + '\'' + '\n' +
                "\t{" + '\n' +
                "\t\twords=" + Arrays.toString(words) + '\n' +
                "\t\t, longestWord='" + longestWord + '\'' + '\n' +
                "\t\t, shortestWord='" + shortestWord + '\'' + '\n' +
                "\t\t, lineLength=" + lineLength + '\n' +
                "\t\t, averageWordLength=" + averageWordLength + '\n' +
                "\t}" + '\n';
    }
}