import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LineStatisticItemTest {

    String testString = "don't worry be         happy  ";
    LineStatisticItem lineStatistic = new LineStatisticItem(testString);

    @Test
    public void findShortestWord() {
        String actual = lineStatistic.getShortestWord();
        String expected = "be";
        assertEquals(expected, actual);
    }

    @Test
    public void findLongestWord() {
        String actual = lineStatistic.getLongestWord();
        String expected = "don't";
        assertEquals(expected, actual);
    }

    @Test
    public void findThisLine() {
        String actual = lineStatistic.getLine();
        String expected = testString;
        assertEquals(expected, actual);
    }

    @Test
    public void findAverageWordLength() {
        double actual = lineStatistic.getAverageWordLength();
        double expected = 4.3;
        Assert.assertTrue(actual == actual);
    }

    @Test
    public void findWordsArr() {
        String[] actual = lineStatistic.getWords();
        assertEquals(actual, new String[]{"don't", "worry", "be", "happy"});
    }

}