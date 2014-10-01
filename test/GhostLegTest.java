package test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;
import org.junit.runner.JUnitCore;

import finished.GhostLeg;


public class GhostLegTest {

    public static void main(String[] args) {
        JUnitCore.main(GhostLegTest.class.getName());
    }

	@Test
	public void case01() {
		int verticalLength = 7;
		int numVerticals   = 4;
		int[][] arrayOfBranchArgs = {
			{0, 3, 1, 1},
			{2, 2, 3, 2},
			{1, 3, 2, 5},
			{2, 4, 3, 4},
			{0, 6, 1, 6},
		};

		GhostLeg ghostLeg = createGhostLeg(verticalLength, numVerticals, arrayOfBranchArgs);
		int indexVerticalToGoal = 0;
		assertThat(ghostLeg.traceFromBottom(indexVerticalToGoal), is(2));
	}

	@Test
	public void case02() {
		int verticalLength = 5;
		int numVerticals   = 5;
		int[][] arrayOfBranchArgs = {
			{2, 3, 3, 4},
			{0, 3, 1, 2},
			{3, 2, 4, 2},
			{1, 1, 2, 2},
			{1, 4, 2, 4},
			{2, 1, 3, 1},
			{0, 4, 1, 3},
			{3, 3, 4, 4},
		};

		GhostLeg ghostLeg = createGhostLeg(verticalLength, numVerticals, arrayOfBranchArgs);
		int indexVerticalToGoal = 0;
		assertThat(ghostLeg.traceFromBottom(indexVerticalToGoal), is(0));
	}

	private static GhostLeg createGhostLeg(int verticalLength, int numVerticals, int[][] arrayOfBranchArgs) {
		GhostLeg ghostLeg = new GhostLeg(verticalLength, numVerticals);
		for (int[] branchArgs : arrayOfBranchArgs) {
			int indexVerticalFrom = branchArgs[0];
			int depthFrom         = branchArgs[1];
			int indexVerticalTo   = branchArgs[2];
			int depthTo           = branchArgs[3];
			ghostLeg.addBranch(indexVerticalFrom, depthFrom, indexVerticalTo, depthTo);
		}

		return ghostLeg;
	}
}
