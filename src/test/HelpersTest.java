package test;

import main.Helpers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class HelpersTest {
	@Test
	void simpleMatrixMultiplication() {
		float[][] a = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
		float[][] b = {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
		float[][] actual = Helpers.simpleMatrixMultiplication(a, b);
		assertArrayEquals(a, actual);
	}

	@Test
	void matrixAddition() {
		float[][] a = {{1, 2}, {3, 4}};
		float[][] b = {{5, 6}, {7, 8}};
		float[][] expected = {{6, 8}, {10, 12}};
		float[][] actual = Helpers.matrixAddition(a, b);
		assertArrayEquals(expected, actual);
	}

	@Test
	void matrixSubtraction() {
		float[][] a = {{5, 6}, {7, 8}};
		float[][] b = {{1, 2}, {3, 4}};
		float[][] expected = {{4, 4}, {4, 4}};
		float[][] actual = Helpers.matrixSubtraction(a, b);
		assertArrayEquals(expected, actual);
	}

	@Test
	void splitMatrix() {
		float[][] m = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
		float[][] m11 = new float[2][2];
		float[][] m12 = new float[2][2];
		float[][] m21 = new float[2][2];
		float[][] m22 = new float[2][2];
		float[][] exp11 = {{1, 2}, {5, 6}};
		float[][] exp12 = {{3, 4}, {7, 8}};
		float[][] exp21 = {{9, 10}, {13, 14}};
		float[][] exp22 = {{11, 12}, {15, 16}};
		Helpers.splitMatrix(m, m11, m12, m21, m22);
		assertArrayEquals(exp11, m11);
		assertArrayEquals(exp12, m12);
		assertArrayEquals(exp21, m21);
		assertArrayEquals(exp22, m22);
	}

	@Test
	void joinMatrix() {
		float[][] m11 = {{1, 2}, {5, 6}};
		float[][] m12 = {{3, 4}, {7, 8}};
		float[][] m21 = {{9, 10}, {13, 14}};
		float[][] m22 = {{11, 12}, {15, 16}};
		float[][] expected = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
		float[][] actual = new float[4][4];
		Helpers.joinMatrix(actual, m11, m12, m21, m22);
		assertArrayEquals(expected, actual);
	}

	@Test
	void createRandomMatrix() {
		int n = 64;
		float[][] m = Helpers.createRandomMatrix(n);
		assert (m.length == n);
		assert (m[0].length == n);

		n = 5;
		m = Helpers.createRandomMatrix(n);
		assert (m.length == 8);
		assert (m[0].length == 8);
		assert (m[5][0] == 0);
	}

	@Test
	void nextPowerOf2() {
		assert (Helpers.nextPowerOf2(2) == 2);
		assert (Helpers.nextPowerOf2(3) == 4);
		assert (Helpers.nextPowerOf2(5) == 8);
		assert (Helpers.nextPowerOf2(6) == 8);
		assert (Helpers.nextPowerOf2(7) == 8);
	}
}