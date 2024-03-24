package test;

import main.StrassenDistributed;
import main.Types;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class StrassenDistributedTest {

	@Test
	void multiply() {
	}

	@Test
	void distributeMatrices() {
		int[] expectedSendCounts = {2, 2, 2, 1};
		int[] expectedDisplacements = {0, 2, 4, 6};
		Types.SubmatrixDistribution expected = new Types.SubmatrixDistribution(expectedSendCounts, expectedDisplacements);
		Types.SubmatrixDistribution actual = StrassenDistributed.distributeMatrices(4);
		assertArrayEquals(expected.sendCounts(), actual.sendCounts());
		assertArrayEquals(expected.displacements(), actual.displacements());
	}
}