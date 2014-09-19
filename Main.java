import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int numMoves = Integer.parseInt(br.readLine());

		Reversi reversi = new Reversi();
		for (int i = 0; i < numMoves; i++) {
			String line = br.readLine();
			String[] items = line.split("\\s+");

			int x = Integer.parseInt(items[1]);
			int y = Integer.parseInt(items[2]);

			reversi.putStone(items[0].equals("B") ? Reversi.BLACK : Reversi.WHITE, x, y);
		}

		System.out.println(reversi.judge());
    }
}

class Reversi {

	private static final int NUM_X = 8;
	private static final int NUM_Y = 8;

	private static final Square[] INITIAL_PLACEMENTS = {
		  new Square(4, 4, Square.Status.WHITE)
		, new Square(5, 5, Square.Status.WHITE)
		, new Square(4, 5, Square.Status.BLACK)
		, new Square(5, 4, Square.Status.BLACK)
	};

	public static final Square.Status BLACK = Square.Status.BLACK;
	public static final Square.Status WHITE = Square.Status.WHITE;

	private final Square[][] board = new Square[NUM_X + 2][NUM_Y + 2];

	public Reversi() {
		initBoard();
	}

	private void initBoard() {
		for (int x = 0; x <= NUM_X + 1; x++) {
			setSquareOnBoard(x, 0        , Square.Status.OB);
			setSquareOnBoard(x, NUM_Y + 1, Square.Status.OB);
		}
		for (int y = 0; y <= NUM_Y + 1; y++) {
			setSquareOnBoard(0        , y, Square.Status.OB);
			setSquareOnBoard(NUM_X + 1, y, Square.Status.OB);
		}
		for (int y = 1; y <= NUM_Y; y++) {
			for (int x = 1; x <= NUM_X; x++) {
				setSquareOnBoard(x, y, Square.Status.BLANK);
			}
		}

		for (Square squareInitial : INITIAL_PLACEMENTS) {
			int x = squareInitial.getX();
			int y = squareInitial.getY();
			board[x][y] = squareInitial;
		}
	}

	private void setSquareOnBoard(int x, int y, Square.Status status) {
		board[x][y] = new Square(x, y, status);
	}

	public void putStone(Square.Status color, int x, int y) {
		color.assertToBeColor();

		Square target = board[x][y];
		target.setStatus(color);
		
		reverseOpponentsInAllDirections(target);
	}

	private void reverseOpponentsInAllDirections(Square target) {
		for (Direction direction : Direction.ALL) {
			reverseOpponents(target, direction);
		}
	}

	private void reverseOpponents(Square target, Direction direction) {
		Square.Status color = target.getStatus();
		Cursor cursor = target.createCursor();
		while (true) {
			cursor.moveIn(direction);
			if (statusOn(cursor).isOpponent(color)) {
				continue;
			} else if (statusOn(cursor).isSameColor(color)) {
				doReverseOpponentsInBetweenExclusive(cursor.getOrigin(), cursor, direction);
			}
			break;
		}
	}

	private void doReverseOpponentsInBetweenExclusive(Cursor cursorFrom, Cursor cursorTo, Direction direction) {
		Cursor cursor = cursorFrom.getCopy();
		while (true) {
			cursor.moveIn(direction);
			if (cursor.isSameLocationWith(cursorTo)) {
				break;
			}

			board[cursor.getX()][cursor.getY()].toggleColor();
		}
	}

	private Square.Status statusOn(Cursor cursor) {
		return board[cursor.getX()][cursor.getY()].getStatus();
	}

	public String judge() {
		int numBlacks = 0;
		int numWhites = 0;
		for (int y = 1; y <= NUM_Y; y++) {
			for (int x = 1; x <= NUM_X; x++) {
				Square.Status color = board[x][y].getStatus();
				if (color.isBlack()) {
					numBlacks++;
				} else if (color.isWhite()) {
					numWhites++;
				}
			}
		}

		String message;
		if (numBlacks == numWhites) {
			message = "Draw!";
		} else {
			message = String.format("The %s won!", numBlacks > numWhites ? "black" : "white");
		}

		return String.format("%2d-%2d %s", numBlacks, numWhites, message);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y <= NUM_Y + 1; y++) {
			for (int x = 0; x <= NUM_X + 1; x++) {
				sb.append(board[x][y]);
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	private static abstract class Coordinate {
		protected final int x;
		protected final int y;

		protected Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}

	private static class Direction extends Coordinate {

		public static final Direction[] ALL = {
			  new Direction( 0, -1)
			, new Direction( 0,  1)
			, new Direction(-1,  0)
			, new Direction( 1,  0)
			, new Direction(-1, -1)
			, new Direction(-1,  1)
			, new Direction( 1, -1)
			, new Direction( 1,  1)
		};

		public Direction(int x, int y) {
			super(x, y);
		}
	}

	private static class Square extends Coordinate {

		private static enum Status {
			BLANK("."), BLACK("B"), WHITE("W"), OB("*");

			private final String toS;

			private Status(String toS) {
				this.toS = toS;
			}

			public void assertToBeColor() {
				if (this != BLACK && this != WHITE) {
					throw new IllegalArgumentException("Argument must be BLACK or WHITE");
				}
			}

			public boolean isSameColor(Status color) {
				color.assertToBeColor();
				return color == BLACK ? this == BLACK : this == WHITE;
			}

			public boolean isOpponent(Status color) {
				color.assertToBeColor();
				return color == BLACK ? this == WHITE : this == BLACK;
			}

			public Status getOpponent() {
				this.assertToBeColor();
				return this == BLACK ? WHITE : BLACK;
			}

			public boolean isBlack() {
				return this == BLACK;
			}

			public boolean isWhite() {
				return this == WHITE;
			}

			public String toString() {
				return toS;
			}
		}

		private Status status;

		public Square(int x, int y, Status status) {
			super(x, y);
			this.status = status;
		}

		public Cursor createCursor() {
			return new Cursor(x, y);
		}

		public void toggleColor() {
			status = status.getOpponent();
		}

		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

		public String toString() {
			return status.toString();
		}
	}

	private static class Cursor {

		private final int xOrigin;
		private final int yOrigin;
		private int x;
		private int y;

		public Cursor(int x, int y) {
			xOrigin = x;
			yOrigin = y;
			this.x = x;
			this.y = y;
		}

		public void moveIn(Direction direction) {
			x += direction.getX();
			y += direction.getY();
		}

		public Cursor getOrigin() {
			return new Cursor(xOrigin, yOrigin);
		}

		public Cursor getCopy() {
			return new Cursor(x, y);
		}

		public boolean isSameLocationWith(Cursor other) {
			return this.x == other.x && this.y == other.y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}
}
