package finished;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Map;
import java.util.HashMap;
import java.util.OptionalLong;
import java.util.StringJoiner;

import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class CharHistogram {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();

		CharHistogram charHistogram = new CharHistogram(line);
		System.out.println(charHistogram.getHistogram());
    }


	private static final char CH_GROUP_START = '(';
	private static final char CH_GROUP_END   = ')';

	private static final String RE_NUMERIC_AT_HEAD = "\\A(\\d+)";

	private static final Pattern PATTERN_NUMERIC_AT_HEAD = Pattern.compile(RE_NUMERIC_AT_HEAD);

	private final String str;
	private int pos;
	private OptionalLong currentNum;

	public CharHistogram(String str) {
		this.str = str;
	}

	public String getHistogram() {
		pos = 0;
		clearCurrentNum();

		Map<Character, Long> mapCharCounts = countChars();

		StringJoiner sj = new StringJoiner("\n");
		for (char ch = 'a'; ch <= 'z'; ch++) {
			long count = mapCharCounts.getOrDefault(ch, 0L);
			sj.add(String.format("%c %d", ch, count));
		}

		return sj.toString();
	}

	private Map<Character, Long> countChars() {
		Map<Character, Long> mapCharCounts = new HashMap<>();

		while (! eos()) {
			if (currentChar() == CH_GROUP_END) {
				advanceToNextChar();
				return mapCharCounts;
			}

			long multiplier = 1;
			if (currentNum().isPresent()) {
				multiplier = currentNum().getAsLong();
				advanceToNextChar();
			}

			if (currentChar() == CH_GROUP_START) {
				advanceToNextChar();
				Map<Character, Long> localMapCharCounts = countChars();
				for (Map.Entry<Character, Long> entry : localMapCharCounts.entrySet()) {
					char ch    = entry.getKey();
					long count = entry.getValue();
					addCountTo(mapCharCounts, ch, count * multiplier);
				}
			} else {
				addCountTo(mapCharCounts, currentChar(), multiplier);
				advanceToNextChar();
			}
		}

		return mapCharCounts;
	}

	private void addCountTo(Map<Character, Long> mapCharCounts, char ch, long count) {
		long currentCount = mapCharCounts.getOrDefault(ch, 0L);
		mapCharCounts.put(ch, currentCount + count);
	}

	private char currentChar() {
		return str.charAt(pos);
	}

	private String currentString() {
		return str.substring(pos);
	}

	private OptionalLong currentNum() {
		if (! currentNum.isPresent()) {
			Matcher m = PATTERN_NUMERIC_AT_HEAD.matcher(currentString());
			if (m.find()) {
				currentNum = OptionalLong.of(Long.parseLong(m.group(1)));
			}
		}

		return currentNum;
	}

	private void clearCurrentNum() {
		currentNum = OptionalLong.empty();
	}

	private boolean eos() {
		return pos >= str.length();
	}

	private void advanceToNextChar() {
		if (currentNum().isPresent()) {
			pos += numberOfDigitsOf(currentNum().getAsLong());
			clearCurrentNum();
		} else {
			pos++;
		}
	}

	private int numberOfDigitsOf(long i) {
		return String.valueOf(i).length();
	}
}
