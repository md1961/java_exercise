package finished;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;


public class GarapagosMobileTyper {

	private static final String[] INPUT_STRINGS = {
		"4444433555E5556661111999996667775553E",
		"7244499992222222277777777E77726655E",
	};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // String line = br.readLine();

		GarapagosMobileTyper gmTyper = new GarapagosMobileTyper();
		for (String inputString : INPUT_STRINGS) {
			System.out.printf("%s ==> %s\n", inputString, gmTyper.type(inputString));
		}
    }

	private static final Map<Character, String> mapKeysAndCharacters = new HashMap<Character, String>() {
		{
			put('1', ".@-_/:~1" );
			put('2', "abcABC2"  );
			put('3', "defDEF3"  );
			put('4', "ghiGHI4"  );
			put('5', "jklJKL5"  );
			put('6', "mnoMNO6"  );
			put('7', "pqrsPQRS7");
			put('8', "tuvTUV8"  );
			put('9', "wxyzWXYZ9");
		}
	};

	private static final char END_OF_CHAR = 'E';

	public String type(String inputString) {
		StringBuilder sbRetval = new StringBuilder();

		for (int i = 0, size = inputString.length(); i < size; ) {
			char cTyped = inputString.charAt(i++);
			int count = 1;
			while (true) {
				char c = inputString.charAt(i++);
				if (c != cTyped) {
					if (c != END_OF_CHAR) {
						i--;
					}
					sbRetval.append(charOut(cTyped, count));
					break;
				}
				count++;
			}
		}

		return sbRetval.toString();
	}

	private char charOut(char cTyped, int count) {
		String charOutSet = mapKeysAndCharacters.get(cTyped);
		int index = (count - 1) % charOutSet.length();
		return charOutSet.charAt(index);
	}
}
