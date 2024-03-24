package main;

public class StrassenSequential {
	private static final int THRESHOLD = 1;
	
	public static float[][] multiply(float[][] a, float[][]b) {
		int n = a.length;

		if (a.length <= THRESHOLD) {
			return Helpers.simpleMatrixMultiplication(a, b);
		}

		int h = n / 2;
		float[][] a11 = new float[h][h];
		float[][] a12 = new float[h][h];
		float[][] a21 = new float[h][h];
		float[][] a22 = new float[h][h];
		float[][] b11 = new float[h][h];
		float[][] b12 = new float[h][h];
		float[][] b21 = new float[h][h];
		float[][] b22 = new float[h][h];
		Helpers.splitMatrix(a, a11, a12, a21, a22);
		Helpers.splitMatrix(b, b11, b12, b21, b22);

		float[][] p1 = multiply(Helpers.matrixAddition(a11, a22), Helpers.matrixAddition(b11, b22));
		float[][] p2 = multiply(Helpers.matrixAddition(a21, a22), b11);
		float[][] p3 = multiply(a11, Helpers.matrixSubtraction(b12, b22));
		float[][] p4 = multiply(a22, Helpers.matrixSubtraction(b21, b11));
		float[][] p5 = multiply(Helpers.matrixAddition(a11, a12), b22);
		float[][] p6 = multiply(Helpers.matrixSubtraction(a21, a11), Helpers.matrixAddition(b11, b12));
		float[][] p7 = multiply(Helpers.matrixSubtraction(a12, a22), Helpers.matrixAddition(b21, b22));

		float[][] c11 = Helpers.matrixAddition(Helpers.matrixSubtraction(Helpers.matrixAddition(p1, p4), p5), p7);
		float[][] c12 = Helpers.matrixAddition(p3, p5);
		float[][] c21 = Helpers.matrixAddition(p2, p4);
		float[][] c22 = Helpers.matrixAddition(Helpers.matrixAddition(Helpers.matrixSubtraction(p1, p2), p3), p6);

		float[][] res = new float[n][n];
		Helpers.joinMatrix(res, c11, c12, c21, c22);

		return res;
	}
}
