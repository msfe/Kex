package tribot;


public class EditingDistance {

	private int closestDistance = 140;
	private int[][] matrix = new int[141][141];
	private int colWordLen;
	private int rowWordLen;
	private int lendif;
	private int col;
	private int row;
	private int end;

	public EditingDistance() {
	}

	public int WFMatrix(String colWord, String rowWord) {

		colWordLen = colWord.length();
		rowWordLen = rowWord.length();
		lendif = colWordLen - rowWordLen;

		for (row = 0; row <= rowWordLen; row++) {
			col = ((row - closestDistance + lendif - 1 > 0) ? row
					- closestDistance + lendif - 1 : 0);
			matrix[col][row] = row;
		}

		for (col = 0; col <= colWordLen; col++) {
			row = ((col - closestDistance - lendif - 1 > 0) ? col
					- closestDistance - lendif - 1 : 0);
			matrix[col][row] = col;
		}

		for (row = 1; row <= rowWordLen; row++) {
			col = ((row - closestDistance + lendif > 0) ? row - closestDistance
					+ lendif : 1);
			end = ((row + closestDistance + lendif < colWordLen) ? row
					+ closestDistance + lendif : colWordLen);
			for (; col <= end; col++) {
				if (colWord.charAt(col - 1) == rowWord.charAt(row - 1)) {
					matrix[col][row] = matrix[col - 1][row - 1];
				} else {
					matrix[col][row] = Math.min(Math.min(matrix[col - 1][row],
							matrix[col][row - 1]), matrix[col - 1][row - 1]) + 1;
					if (col == row + lendif
							&& matrix[col][row] > closestDistance) {
						return 41;
					}
				}

			}
		}

		return matrix[colWordLen][rowWordLen];
	}

	int Distance(String w1, String w2) {
		int i = WFMatrix(w1, w2);
		return i;
	}
}
