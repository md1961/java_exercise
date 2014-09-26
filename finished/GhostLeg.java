import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;


public class GhostLeg {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int[] nums = splitToIntArray(br.readLine());
		int verticalLength = nums[0];
		int numVerticals   = nums[1];
		int numBranches    = nums[2];

		GhostLeg ghostLeg = new GhostLeg(verticalLength, numVerticals);

		for (int i = 0; i < numBranches; i++) {
			nums = splitToIntArray(br.readLine());
			int indexVerticalFrom = nums[0] - 1;
			int depthFrom         = nums[1];
			int depthTo           = nums[2];
			int indexVerticalTo = indexVerticalFrom + 1;

			ghostLeg.addBranch(indexVerticalFrom, depthFrom, indexVerticalTo, depthTo);
		}

		int indexVerticalToGoal = 0;
		int indexVerticalToStart = ghostLeg.traceFromBottom(indexVerticalToGoal);
		System.out.println(indexVerticalToStart + 1);
    }

	private static int[] splitToIntArray(String s) {
		return Arrays.stream(s.split("\\s+")).mapToInt(n -> Integer.parseInt(n)).toArray();
	}


	private final int verticalLength;
	private final Vertical[] verticals;

	public GhostLeg(int verticalLength, int numVerticals) {
		this.verticalLength = verticalLength;
		verticals = IntStream.range(0, numVerticals).mapToObj(index -> new Vertical(index)).toArray(Vertical[]::new);
	}

	public void addBranch(int indexVerticalFrom, int depthFrom, int indexVerticalTo, int depthTo) {
		Vertical verticalFrom = findVerticalByIndex(indexVerticalFrom);
		Vertical verticalTo   = findVerticalByIndex(indexVerticalTo  );

		verticalFrom.addOneWayBranch(depthFrom, verticalTo  , depthTo  );
		verticalTo  .addOneWayBranch(depthTo  , verticalFrom, depthFrom);
	}

	private Vertical findVerticalByIndex(int index) {
		return verticals[index];
	}

	public int traceFromBottom(int indexVertical) {
		Cursor cursor = new Cursor(findVerticalByIndex(indexVertical), verticalLength);
		cursor.traceUpward();
		return cursor.getVertical().getIndex();
	}

	private class Vertical {

		private final int index;
		private final List<OneWayBranch> branches;

		public Vertical(int index) {
			this.index = index;
			branches = new ArrayList<>();
		}

		public void addOneWayBranch(int depth, Vertical verticalTo, int depthTo) {
			branches.add(new OneWayBranch(depth, verticalTo, depthTo));
			Collections.sort(branches);
		}

		public void traceUpward(Cursor cursor) {
			Optional<OneWayBranch> branch = getBranchAbove(cursor.getDepth());
			if (branch.isPresent()) {
				branch.get().deliver(cursor);
			} else {
				cursor.setDepth(0);
			}
		}

		private Optional<OneWayBranch> getBranchAbove(int depth) {
			return branches.stream().filter(br -> br.getDepth() < depth)
									.reduce((prev, curr) -> curr)
									;
		}

		public int getIndex() {
			return index;
		}
	}

	private class OneWayBranch implements Comparable<OneWayBranch> {

		private final int depth;
		private final Vertical verticalTo;
		private final int depthTo;

		public OneWayBranch(int depth, Vertical verticalTo, int depthTo) {
			this.depth      = depth;
			this.verticalTo = verticalTo;
			this.depthTo    = depthTo;
		}

		public void deliver(Cursor cursor) {
			cursor.setVertical(verticalTo);
			cursor.setDepth(depthTo);
		}

		@Override
		public int compareTo(OneWayBranch other) {
			return this.depth - other.depth;
		}

		public int getDepth() {
			return depth;
		}
	}

	private class Cursor {

		private Vertical vertical;
		private int depth;

		public Cursor(Vertical vertical, int depth) {
			this.vertical = vertical;
			this.depth    = depth;
		}

		public void traceUpward() {
			while (depth > 0) {
				vertical.traceUpward(this);
			} 
		}

		public Vertical getVertical() {
			return vertical;
		}

		public void setVertical(Vertical vertical) {
			this.vertical = vertical;
		}

		public int getDepth() {
			return depth;
		}

		public void setDepth(int depth) {
			this.depth = depth;
		}
	}
}
