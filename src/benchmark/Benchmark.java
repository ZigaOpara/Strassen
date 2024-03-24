package benchmark;

import main.Helpers;
import main.StrassenDistributed;
import main.StrassenParallel;
import mpi.MPI;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;

public class Benchmark {
	public static void main(String[] args) {
		int rank;
		Instant startTime;
		Instant endTime;
		Duration duration;
		String d;

		try {
			MPI.Init(args);
			rank = MPI.COMM_WORLD.Rank();
		} catch (mpi.MPIException e) {
			rank = -1;
		}
		int size = Integer.parseInt(args[args.length-1]);
		if (size <= 0) {
			System.out.println("Invalid size");
			return;
		}

		boolean isRoot = rank == 0 || rank == -1;

		float[][] a = Helpers.createRandomMatrix(size);
		float[][] b = Helpers.createRandomMatrix(size);

		if (isRoot) {

			// Sequential
			System.out.println("Starting sequential");
			startTime = Instant.now();
			main.StrassenSequential.multiply(a, b);
			endTime = Instant.now();
			duration = Duration.between(startTime, endTime);
			d = DurationFormatUtils.formatDuration(duration.toMillis(), "mm:ss.SSS");
			System.out.printf("Sequential execution duration: %s%n", d);

			// Parallel
			System.out.println("Starting parallel");
			ForkJoinPool commonPool = ForkJoinPool.commonPool();
			StrassenParallel t = new StrassenParallel(a, b);
			startTime = Instant.now();
			commonPool.invoke(t);
			endTime = Instant.now();
			duration = Duration.between(startTime, endTime);
			d = DurationFormatUtils.formatDuration(duration.toMillis(), "mm:ss.SSS");
			System.out.printf("Parallel execution duration: %s%n", d);
		}

		// Distributed
		if (isRoot) System.out.println("Starting distributed");
		startTime = Instant.now();
		StrassenDistributed.multiply(a, b);
		if (MPI.COMM_WORLD.Rank() != 0) return;
		endTime = Instant.now();
		duration = Duration.between(startTime, endTime);
		d = DurationFormatUtils.formatDuration(duration.toMillis(), "mm:ss.SSS");
		System.out.printf("Distributed execution duration: %s%n", d);
	}
}
