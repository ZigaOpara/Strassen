package main;

import mpi.MPI;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.concurrent.ForkJoinPool;

public class StrassenDistributed {
	public static void multiply(float[][] a, float[][] b) {
		int rank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
		int n = a.length;
		float[][] result = new float[n][n];

		Pair<float[][], float[][]>[] scatterSendBuffer = new Pair[7];

		if (rank == 0) {
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

			scatterSendBuffer[0] = new ImmutablePair<>(Helpers.matrixAddition(a11, a22), Helpers.matrixAddition(b11, b22));
			scatterSendBuffer[1] = new ImmutablePair<>(Helpers.matrixAddition(a21, a22), b11);
			scatterSendBuffer[2] = new ImmutablePair<>(a11, Helpers.matrixSubtraction(b12, b22));
			scatterSendBuffer[3] = new ImmutablePair<>(a22, Helpers.matrixSubtraction(b21, b11));
			scatterSendBuffer[4] = new ImmutablePair<>(Helpers.matrixAddition(a11, a12), b22);
			scatterSendBuffer[5] = new ImmutablePair<>(Helpers.matrixSubtraction(a21, a11), Helpers.matrixAddition(b11, b12));
			scatterSendBuffer[6] = new ImmutablePair<>(Helpers.matrixSubtraction(a12, a22), Helpers.matrixAddition(b21, b22));
		}

		Types.SubmatrixDistribution distribution = distributeMatrices(size);
		Pair<float[][], float[][]>[] scatterReceiveBuffer = new Pair[distribution.sendCounts()[rank]];
		MPI.COMM_WORLD.Scatterv(scatterSendBuffer, 0, distribution.sendCounts(), distribution.displacements(), MPI.OBJECT, scatterReceiveBuffer, 0, distribution.sendCounts()[rank], MPI.OBJECT, 0);
		float[][][] gatherSendBuffer = new float[distribution.sendCounts()[rank]][][];
		if (scatterReceiveBuffer.length > 0) {
			ForkJoinPool commonPool = ForkJoinPool.commonPool();
			for (int i = 0; i < scatterReceiveBuffer.length; i++) {
				StrassenParallel t = new StrassenParallel(scatterReceiveBuffer[i].getLeft(), scatterReceiveBuffer[i].getRight());
				gatherSendBuffer[i] = commonPool.invoke(t);
			}
		}
		float[][][] gatherReceiveBuffer = new float[7][][];
		MPI.COMM_WORLD.Gatherv(gatherSendBuffer, 0, distribution.sendCounts()[rank], MPI.OBJECT, gatherReceiveBuffer, 0, distribution.sendCounts(), distribution.displacements(), MPI.OBJECT, 0);
		if (rank == 0) {
			// readability mapping
			float[][] p1 = gatherReceiveBuffer[0];
			float[][] p2 = gatherReceiveBuffer[1];
			float[][] p3 = gatherReceiveBuffer[2];
			float[][] p4 = gatherReceiveBuffer[3];
			float[][] p5 = gatherReceiveBuffer[4];
			float[][] p6 = gatherReceiveBuffer[5];
			float[][] p7 = gatherReceiveBuffer[6];

			float[][] c11 = Helpers.matrixAddition(Helpers.matrixSubtraction(Helpers.matrixAddition(p1, p4), p5), p7);
			float[][] c12 = Helpers.matrixAddition(p3, p5);
			float[][] c21 = Helpers.matrixAddition(p2, p4);
			float[][] c22 = Helpers.matrixAddition(Helpers.matrixAddition(Helpers.matrixSubtraction(p1, p2), p3), p6);

			Helpers.joinMatrix(result, c11, c12, c21, c22);
		}

		MPI.COMM_WORLD.Barrier();
		MPI.Finalize();
	}

	public static Types.SubmatrixDistribution distributeMatrices(int size) {
		int[] sendCounts = new int[size];
		int[] displacements = new int[size];

		int q = 7 / size;
		int r = 7 % size;

		int nextDisplacement = 0;
		for (int i = 0; i < size; i++) {
			sendCounts[i] = q;
			if (i < r) {
				sendCounts[i]++;
			}
			displacements[i] = nextDisplacement;
			nextDisplacement += sendCounts[i];
		}

		return new Types.SubmatrixDistribution(sendCounts, displacements);
	}
}


