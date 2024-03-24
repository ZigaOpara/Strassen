package main;

import mpi.MPI;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;

public class Main {
	public static void main(String[] args) {
		String executionType = args[args.length - 2];
		int size = Integer.parseInt(args[args.length - 1]);

		if (size <= 0) {
			System.out.println("Invalid size");
			return;
		}

		float[][] a = Helpers.createRandomMatrix(size);
		float[][] b = Helpers.createRandomMatrix(size);

		Instant startTime = Instant.now();
		switch(executionType) {
			case "simple":
				Helpers.simpleMatrixMultiplication(a, b);
				break;
			case "sequential":
				StrassenSequential.multiply(a, b);
				break;
			case "parallel":
				ForkJoinPool commonPool = ForkJoinPool.commonPool();
				StrassenParallel t = new StrassenParallel(a, b);
				commonPool.invoke(t);
				break;
			case "distributed":
				try {
					MPI.Init(args);
				} catch (mpi.MPIException e) {
					System.out.println("Cannot initiate MPI");
					return;
				}
				StrassenDistributed.multiply(a, b);
				if (MPI.COMM_WORLD.Rank() != 0) return;
				break;
			default:
				System.out.println("Invalid execution type");
				return;
		}
		Instant endTime = Instant.now();
		Duration duration = Duration.between(startTime, endTime);

		String d = DurationFormatUtils.formatDuration(duration.toMillis(), "mm:ss.SSS");
		System.out.printf("%s execution duration: %s%n", executionType, d);
	}
}

