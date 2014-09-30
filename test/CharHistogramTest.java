package test;

import static org.junit.Assert.assertEquals;
 
import org.junit.Test;
import org.junit.runner.JUnitCore;

import java.util.Map;
import java.util.HashMap;
import java.util.StringJoiner;

import finished.CharHistogram;
 

public class CharHistogramTest {
 
    public static void main(String[] args) {
        JUnitCore.main(CharHistogramTest.class.getName());
    }

	private static final String INPUT_001 = "5a6(ab7(abc)";
	private static final Map<Character, Long> MAP_EXPECTED_001 = new HashMap<Character, Long>() {
		{
			put('a', 53L);
			put('b', 48L);
			put('c', 42L);
		}
	};
 
    @Test
    public void shortString() {
		doTestGetHistogram(INPUT_001, MAP_EXPECTED_001);
	}

	private static final String INPUT_002 = "abcdefg10h12(ij2(3k))l9mnop4(3(2(6(qq)r)s5tu)7v5w)x15(yz)";
	private static final Map<Character, Long> MAP_EXPECTED_002 = new HashMap<Character, Long>() {
		{
			put('a',   1L);
			put('b',   1L);
			put('c',   1L);
			put('d',   1L);
			put('e',   1L);
			put('f',   1L);
			put('g',   1L);
			put('h',  10L);
			put('i',  12L);
			put('j',  12L);
			put('k',  72L);
			put('l',   1L);
			put('m',   9L);
			put('n',   1L);
			put('o',   1L);
			put('p',   1L);
			put('q', 288L);
			put('r',  24L);
			put('s',  12L);
			put('t',  60L);
			put('u',  12L);
			put('v',  28L);
			put('w',  20L);
			put('x',   1L);
			put('y',  15L);
			put('z',  15L);
		}
	};
 
    @Test
    public void mediumStringWithAllChars() {
		doTestGetHistogram(INPUT_002, MAP_EXPECTED_002);
	}

	private static final String INPUT_003 = "10000(10000(10000(2000(ab)500(dz)c200h)2mu3000(fpr)))";
	private static final Map<Character, Long> MAP_EXPECTED_003 = new HashMap<Character, Long>() {
		{
			put('a', 2000000000000000L);
			put('b', 2000000000000000L);
			put('c',    1000000000000L);
			put('d',  500000000000000L);
			put('f',     300000000000L);
			put('h',  200000000000000L);
			put('m',        200000000L);
			put('p',     300000000000L);
			put('r',     300000000000L);
			put('u',        100000000L);
			put('z',  500000000000000L);
		}
	};
 
    @Test
    public void longString() {
		doTestGetHistogram(INPUT_003, MAP_EXPECTED_003);
	}

	private static void doTestGetHistogram(String input, Map<Character, Long> mapCharCountsExpected) {
        CharHistogram charHistogram = new CharHistogram(input);
		String expected = convertToString(mapCharCountsExpected);
        assertEquals(expected, charHistogram.getHistogram());
    }

	private static String convertToString(Map<Character, Long> mapCharCounts) {
		StringJoiner sj = new StringJoiner("\n");
		for (char ch = 'a'; ch <= 'z'; ch++) {
			long count = mapCharCounts.getOrDefault(ch, 0L);
			sj.add(String.format("%c %d", ch, count));
		}

		return sj.toString();
	}
}
