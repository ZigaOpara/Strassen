package test;

import main.Helpers;
import main.StrassenParallel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.*;

class StrassenParallelTest {
	static ForkJoinPool commonPool;

	@BeforeAll
	static void setUp() {
		commonPool = ForkJoinPool.commonPool();
	}

	@Test
	void multiplyIdentity() {
		float[][] a = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
		float[][] b = {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
		StrassenParallel t = new StrassenParallel(a, b, 1);
		float[][] actual = commonPool.invoke(t);
		assertArrayEquals(a, actual);
	}

	@Test
	void multiplyRandom() {
		int n = 16;
		float[][] a = Helpers.createRandomMatrix(n);
		float[][] b = Helpers.createRandomMatrix(n);
		StrassenParallel t = new StrassenParallel(a, b, 2);
		float[][] strassen = commonPool.invoke(t);
		float[][] simple = Helpers.simpleMatrixMultiplication(a, b);
		for (int i = 0; i < strassen.length; i++) {
			assertArrayEquals(strassen[i], simple[i], 0.001f);
		}
	}
}