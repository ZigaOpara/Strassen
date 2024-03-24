package main;

import java.util.concurrent.RecursiveTask;

public class StrassenParallel extends RecursiveTask<float[][]> {
	private final float[][] a;
	private final float[][] b;
	private static int THRESHOLD = 1;

	public StrassenParallel(float[][] a, float[][] b) {
		this.a = a;
		this.b = b;
	}

	public StrassenParallel(float[][] a, float[][] b, int t) {
		this.a = a;
		this.b = b;
		THRESHOLD = t;
	}

	@Override
	protected float[][] compute() {
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

		StrassenParallel p1 = new StrassenParallel(Helpers.matrixAddition(a11, a22), Helpers.matrixAddition(b11, b22));
		StrassenParallel p2 = new StrassenParallel(Helpers.matrixAddition(a21, a22), b11);
		StrassenParallel p3 = new StrassenParallel(a11, Helpers.matrixSubtraction(b12, b22));
		StrassenParallel p4 = new StrassenParallel(a22, Helpers.matrixSubtraction(b21, b11));
		StrassenParallel p5 = new StrassenParallel(Helpers.matrixAddition(a11, a12), b22);
		StrassenParallel p6 = new StrassenParallel(Helpers.matrixSubtraction(a21, a11), Helpers.matrixAddition(b11, b12));
		StrassenParallel p7 = new StrassenParallel(Helpers.matrixSubtraction(a12, a22), Helpers.matrixAddition(b21, b22));

		StrassenParallel[] tasks = new StrassenParallel[]{p1, p2, p3, p4, p5, p6, p7};
		invokeAll(tasks);

		float[][] c11 = Helpers.matrixAddition(Helpers.matrixSubtraction(Helpers.matrixAddition(p1.join(), p4.join()), p5.join()), p7.join());
		float[][] c12 = Helpers.matrixAddition(p3.join(), p5.join());
		float[][] c21 = Helpers.matrixAddition(p2.join(), p4.join());
		float[][] c22 = Helpers.matrixAddition(Helpers.matrixAddition(Helpers.matrixSubtraction(p1.join(), p2.join()), p3.join()), p6.join());

		float[][] res = new float[n][n];
		Helpers.joinMatrix(res, c11, c12, c21, c22);

		return res;
	}
}
