package main;

public class Helpers {
	public static float[][] simpleMatrixMultiplication(float[][] a, float[][] b) {
		int n = a.length;
		float[][] res = new float[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < n; k++) {
					res[i][j] += a[i][k] * b[k][j];
				}
			}
		}

		return res;
	}

	public static float[][] matrixAddition(float[][] a, float[][] b) {
		int size = a.length;
		float[][] res = new float[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				res[i][j] = a[i][j] + b[i][j];
			}
		}

		return res;
	}

	public static float[][] matrixSubtraction(float[][] a, float[][] b) {
		int size = a.length;
		float[][] res = new float[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				res[i][j] = a[i][j] - b[i][j];
			}
		}

		return res;
	}

	public static void splitMatrix(float[][] m, float[][] m11, float[][] m12, float[][] m21, float[][] m22) {
		int n = m.length;
		int h = n / 2;

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < h; j++) {
				m11[i][j] = m[i][j];
				m12[i][j] = m[i][j + h];
				m21[i][j] = m[i + h][j];
				m22[i][j] = m[i + h][j + h];
			}
		}
	}

	public static void joinMatrix(float[][] res, float[][] m11, float[][] m12, float[][] m21, float[][] m22) {
		int n = res.length;
		int h = n / 2;

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < h; j++) {
				res[i][j] = m11[i][j];
				res[i][j + h] = m12[i][j];
				res[i + h][j] = m21[i][j];
				res[i + h][j + h] = m22[i][j];
			}
		}
	}

	public static float[][] createRandomMatrix(int n) {
		int paddedLength = nextPowerOf2(n);
		float[][] m = new float[paddedLength][paddedLength];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				m[i][j] = (float)Math.random()*10;
			}
		}
		return m;
	}

	public static int nextPowerOf2(int n) {
		n--;

		n |= n >>> 1;
		n |= n >>> 2;
		n |= n >>> 4;
		n |= n >>> 8;
		n |= n >>> 16;

		return n + 1;
	}
}
