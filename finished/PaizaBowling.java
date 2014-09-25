import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;


public class PaizaBowling {

	private static final String GUTTER = "G";

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String line = br.readLine();
		int[] nums = Arrays.stream(line.split("\\s+")).mapToInt(x -> Integer.parseInt(x)).toArray();
		int numFrames = nums[0];
		int numPins   = nums[1];
		int numThrows = nums[2];

        line = br.readLine();
		int[] arrayOfPinsTaken = Arrays.stream(line.split("\\s+"))
									.mapToInt(x -> GUTTER.equals(x) ? 0 : Integer.parseInt(x)).toArray();

		PaizaBowling pb = new PaizaBowling(numPins, numFrames);
		pb.throwAll(arrayOfPinsTaken);
		System.out.println(pb.getScore());
    }


	private final ScoreSheet scoreSheet;

	public PaizaBowling(int numPins, int numFrames) {
		scoreSheet = new ScoreSheet(numPins, numFrames);
	}

	public void throwAll(int[] arrayOfPinsTaken) {
		scoreSheet.keepScoreAll(arrayOfPinsTaken);
	}

	public int getScore() {
		return scoreSheet.totalScore();
	}

	private static class ScoreSheet {

		private final int numPins;
		private final int numFrames;
		private final Frame[] frames;

		public ScoreSheet(int numPins, int numFrames) {
			this.numPins   = numPins;
			this.numFrames = numFrames;

			frames = new Frame[numFrames];
			for (int i = 0; i < numFrames; i++) {
				frames[i] = new Frame();
			}
			frames[numFrames - 1].setIsFinal(true);
		}

		public void keepScoreAll(int[] arrayOfPinsTaken) {
			int indexThrow = 0;
			for (Frame frame : frames) {
				frame.keepScore(Arrays.copyOfRange(arrayOfPinsTaken, indexThrow, indexThrow + 3));
				indexThrow += frame.numThrows();
			}
		}

		public int totalScore() {
			return Arrays.stream(frames).mapToInt(frame -> frame.getScore()).sum();
		}

		private class Frame {

			private int[] arrayOfPinsTaken;
			private boolean isFinal = false;

			public void keepScore(int[] arrayOfPinsTaken) {
				this.arrayOfPinsTaken = arrayOfPinsTaken;
			}

			public int numThrows() {
				if (isFinal()) {
					return arrayOfPinsTaken.length;
				} else if (isStrike()) {
					return 1;
				}
				return 2;
			}

			public int getScore() {
				if (! isFinal()) {
					int numThrowsToFollowForBonus = 0;
					if (isStrike()) {
						numThrowsToFollowForBonus = 2;
					} else if (isSpare()) {
						numThrowsToFollowForBonus = 1;
					}

					return sumOfPins(numThrows() + numThrowsToFollowForBonus);
				} else {
					int retval = sumOfPins(3);
					if (isStrike()) {
						retval += arrayOfPinsTaken[1] + arrayOfPinsTaken[2];
						if (arrayOfPinsTaken[1] == numPins) {
							retval += arrayOfPinsTaken[2];
						}
					} else if (isSpare()) {
						retval += arrayOfPinsTaken[2];
					}

					return retval;
				}
			}

			private int sumOfPins(int numThrowsToCount) {
				int numThrows = Math.min(arrayOfPinsTaken.length, numThrowsToCount);
				return Arrays.stream(arrayOfPinsTaken).limit(numThrows).sum();
			}

			private boolean isStrike() {
				return sumOfPins(1) == numPins;
			}

			private boolean isSpare() {
				return ! isStrike() && sumOfPins(2) == numPins;
			}

			public boolean isFinal() {
				return isFinal;
			}

			public void setIsFinal(boolean value) {
				isFinal = value;
			}
		}
	}
}
