package test;

import main.Helpers;
import main.StrassenSequential;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrassenSequentialTest {

	@Test
	void multiplyIdentity() {
		float[][] a = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
		float[][] b = {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
		float[][] actual = StrassenSequential.multiply(a, b);
		assertArrayEquals(a, actual);
	}

	@Test
	void multiplyRandom() {
		int n = 16;
		float[][] a = Helpers.createRandomMatrix(n);
		float[][] b = Helpers.createRandomMatrix(n);
		float[][] strassen = StrassenSequential.multiply(a, b);
		float[][] simple = Helpers.simpleMatrixMultiplication(a, b);
		for (int i = 0; i < strassen.length; i++) {
			assertArrayEquals(strassen[i], simple[i], 0.001f);
		}
	}
}