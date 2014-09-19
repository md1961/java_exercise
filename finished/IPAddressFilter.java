import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class IPAddressFilter {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String strIPAddresFilter = br.readLine();
		IPAddressFilter ipFilter = new IPAddressFilter(strIPAddresFilter);

		String strNumLog = br.readLine();
		int numLog = Integer.parseInt(strNumLog);

		for (int i = 0; i < numLog; i++) {
			String log = br.readLine();
			ApacheLogEntry logEntry = new ApacheLogEntry(log);
			if (ipFilter.passes(logEntry.getIpAddress())) {
				System.out.println(logEntry);
			}
		}
    }

	private static final int NUM_OCTETS = 4;
	private static final String OCTET_DELIMITER = "\\.";

	private final OctetFilter[] octetFilters = new OctetFilter[NUM_OCTETS];

	public IPAddressFilter(String strFilter) {
		interpretFilter(strFilter);
	}

	private void interpretFilter(String strFilter) {
		String[] strOctetFilters = strFilter.split(OCTET_DELIMITER);
		if (strOctetFilters.length != NUM_OCTETS) {
			throw new IllegalArgumentException(
						String.format("Illegal number of octets (%d) in '%s'", strOctetFilters.length, strFilter));
		}

		for (int i = 0; i < NUM_OCTETS; i++) {
			String strOctetFilter = strOctetFilters[i];

			if (i <= 1 && ! OctetFilter.isLegalLiteralOctet(strOctetFilter)) {
				throw new IllegalArgumentException(
							String.format("Illegal literal '%s' at octet No.%d", strOctetFilter, i + 1));
			} else if (i >= 2 && ! OctetFilter.isLegalFilter(strOctetFilter)) {
				throw new IllegalArgumentException(
							String.format("Illegal filter '%s' at octet No.%d", strOctetFilter, i + 1));
			}

			octetFilters[i] = new OctetFilter(strOctetFilter);
		}
	}

	public boolean passes(String ipAddress) {
		String[] octets = ipAddress.split(OCTET_DELIMITER);
		if (octets.length != NUM_OCTETS) {
			throw new IllegalArgumentException(String.format("Illegal number of octets in '%s'", ipAddress));
		}

		for (int i = 0; i < NUM_OCTETS; i++) {
			if (! octetFilters[i].passes(octets[i])) {
				return false;
			}
		}

		return true;
	}

	private static class OctetFilter {

		private static final int LEGAL_LITERAL_OCTET_FROM =   0;
		private static final int LEGAL_LITERAL_OCTET_TO   = 255;

		private static final String FILTER_TO_PASS_ALL = "*";

		private static final String RE_FILTER_TO_PASS_RANGE = "\\A\\[(\\d+)-(\\d+)]\\z";
		private static final Pattern PATTERN_TO_FILTER_TO_PASS_RANGE = Pattern.compile(RE_FILTER_TO_PASS_RANGE);

		private static final String RE_INTEGER = "\\A\\d+\\z";
		private static final Pattern PATTERN_INTEGER = Pattern.compile(RE_INTEGER);

		public static boolean isLegalFilter(String strFilter) {
			return isFilterToPassAll(strFilter) || isLegalLiteralOctet(strFilter)
				|| matcherForLegalRangeFilter(strFilter).matches();
		}

		public static boolean isLegalLiteralOctet(String strFilter) {
			if (! isInteger(strFilter)) {
				return false;
			}

			int octetValue = Integer.parseInt(strFilter);
			return LEGAL_LITERAL_OCTET_FROM <= octetValue && octetValue <= LEGAL_LITERAL_OCTET_TO;
		}

		private static boolean isInteger(String str) {
			return PATTERN_INTEGER.matcher(str).matches();
		}

		private static boolean isFilterToPassAll(String strFilter) {
			return strFilter.equals(FILTER_TO_PASS_ALL);
		}

		private static Matcher matcherForLegalRangeFilter(String strFilter) {
			return PATTERN_TO_FILTER_TO_PASS_RANGE.matcher(strFilter);
		}

		private final String strFilter;
		private final boolean isRangeFilter;
		private final int octetValueFrom;
		private final int octetValueTo;

		public OctetFilter(String strFilter) {
			this.strFilter = strFilter;

			Matcher m = matcherForLegalRangeFilter(strFilter);
			isRangeFilter = m.matches();
			octetValueFrom = isRangeFilter ? Integer.parseInt(m.group(1)) : 0;
			octetValueTo   = isRangeFilter ? Integer.parseInt(m.group(2)) : 0;
		}

		public boolean passes(String octet) {
			if (isFilterToPassAll(strFilter)) {
				return true;
			}

			if (isInteger(octet) && octet.equals(strFilter)) {
				return true;
			}

			if (isRangeFilter) {
				int octetValue = Integer.parseInt(octet);
				return octetValueFrom <= octetValue && octetValue <= octetValueTo;
			}

			return false;
		}
	}
}

class ApacheLogEntry {

	private static final String RE_IP_ADDRESS     = "(?<ip>[\\d.]+)";
	private static final String RE_IDENT_USER     = "[^ ]+";
	private static final String RE_AUTH_USER      = "[^ ]+";
	private static final String RE_TIMESTAMP      = "\\[(?<timestamp>[^ ]+) +(?<timezone>[^ ]+)\\]";
	private static final String RE_REQUEST_HEADER = "\"(?<method>[^ ]+) +(?<filename>[^ ]+) +(?<protocol>[^ ]+)\"";

	private static final Pattern PATTERN_TO_INTERPRET_LOG;

	static {
		String[] reArray = {RE_IP_ADDRESS, RE_IDENT_USER, RE_AUTH_USER, RE_TIMESTAMP, RE_REQUEST_HEADER};
		String reLogInterpret = "\\A" + String.join(" +", reArray);
		PATTERN_TO_INTERPRET_LOG = Pattern.compile(reLogInterpret);
	}

	private final String ipAddress;
	private final String strTimestamp;
	private final String strTimezone;
	private final String requestMethod;
	private final String filename;
	private final String protocol;

	public ApacheLogEntry(String log) {
		Matcher m = PATTERN_TO_INTERPRET_LOG.matcher(log);
		if (! m.find()) {
			throw new IllegalArgumentException(String.format("Illegal log entry '%s'", log));
		}

		ipAddress     = m.group("ip");
		strTimestamp  = m.group("timestamp");
		strTimezone   = m.group("timezone");
		requestMethod = m.group("method");
		filename      = m.group("filename");
		protocol      = m.group("protocol");

	}

	public String getIpAddress() {
		return ipAddress;
	}

	public String toString() {
		return String.format("%s %s %s", ipAddress, strTimestamp, filename);
	}
}
