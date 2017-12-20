import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;

public class LineStatisticUtility {

    /**
     * Split words(symbols between 2 spaces) by the all whitespaces
     *
     * @param line target string
     * @return array of words from line
     */
    public static String[] splitWordsByWhitespaces(String line) {
        return line.trim().split("\\s+");
    }

    /**
     * Find longest word(symbols between 2 spaces) of string
     *
     * @param words array of strings from line
     * @return first found longest string
     */
    public static String findLongestWord(String[] words) {
        return Arrays.stream(words).max(Comparator.comparingInt(String::length)).orElse(null);
    }

    /**
     * Find longest word(symbols between 2 spaces) of string
     *
     * @param line target string
     * @return first found longest string
     */
    public static String findLongestWord(String line) {
        return findLongestWord(splitWordsByWhitespaces(line));
    }

    /**
     * Find shortest word(symbols between 2 spaces) of string
     *
     * @param words array of strings from line
     * @return first found shortest string
     */
    public static String findShortestWord(String[] words) {
        return Arrays.stream(words).min(Comparator.comparingInt(String::length)).orElse(null);
    }

    /**
     * Find shortest word(symbols between 2 spaces) of string
     *
     * @param line target string
     * @return first found shortest string
     */
    public static String findShortestWord(String line) {
        return findShortestWord(splitWordsByWhitespaces(line));
    }

    /**
     * Calculate average length of words(symbols between 2 spaces)
     *
     * @param words array of strings from line
     * @param scale required accuracy of average length
     * @return double value rounded to required accuracy
     */
    public static double calcAverageWordsLength(String[] words, int scale) {
        double count = 0;
        for (String word : words) {
            count += word.length();
        }
        return BigDecimal.valueOf(count / words.length).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * Calculate average length of words(symbols between 2 spaces)
     *
     * @param words array of strings from line
     * @return double value rounded to one decimal place (ex: 2.3)
     */
    public static double calcAverageWordsLength(String[] words) {
        return calcAverageWordsLength(words, 1);
    }

    /**
     * Calculate average length of words(symbols between 2 spaces)
     *
     * @param line  target string
     * @param scale required accuracy of average length
     * @return double value rounded to required accuracy
     */
    public static double calcAverageWordsLength(String line, int scale) {
        return calcAverageWordsLength(splitWordsByWhitespaces(line), scale);
    }

    /**
     * Calculate average length of words(symbols between 2 spaces)
     *
     * @param line target string
     * @return double value rounded to one decimal place (ex: 2.6)
     */
    public static double calcAverageWordsLength(String line) {
        return calcAverageWordsLength(line, 1);
    }
}
