import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class LineStatisticUtilityTest {

    //task point : word - symbols between 2 spaces
    String testLine1 = "строка для тестов номер 1.";
    String testLine2 = "        строка    для тестов    № 1.         !          ";
    String[] wordsArr = {"мама", "мыла", "рамку"};

    @Test
    public void findLongestWord() {
        String actual = LineStatisticUtility.findLongestWord(testLine1);
        String expected = "строка";
        assertEquals(expected, actual);
    }

    @Test
    public void findLongestWord1() throws Exception {
        String actual = LineStatisticUtility.findLongestWord(wordsArr);
        String expected = "рамку";
        assertEquals(expected, actual);
    }

    @Test
    public void findShortestWord() throws Exception {
        String actual = LineStatisticUtility.findShortestWord(testLine2);
        String wrong = "строка";
        assertNotEquals(actual, wrong);
    }

    @Test
    public void findShortestWordInEmptyString() throws Exception {
        String actual = LineStatisticUtility.findShortestWord("");
        String expected = "";
        assertEquals(actual, expected);
    }

    @Test (expected = NullPointerException.class)
    public void findShortestWordInNullString() throws Exception {
        String nullString = java.lang.String.valueOf(null);
        String actual = LineStatisticUtility.findShortestWord(nullString);
        String expected = "";
        assertEquals(actual, expected);
    }

    @Test
    public void splitWordsByWhitespaces() throws Exception {
        String[] actual = LineStatisticUtility.splitWordsByWhitespaces("   мама     мыла" +
                "               рамку   ");
        assertEquals(actual, wordsArr);
    }

    @Test
    public void calcAverageWordsLength() throws Exception {
        double actual = LineStatisticUtility.calcAverageWordsLength(testLine1);
        double expected = 4.4;
        Assert.assertTrue(actual == expected);
    }

    @Test
    public void calcAverageWordsLength3() throws Exception {
        double actual = LineStatisticUtility.calcAverageWordsLength("");
        double expected = 0.0;
        Assert.assertTrue(actual == expected);
    }
}
