package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private double[] fractions;
    private int numTrials;

    /** perform T independent experiments on an N-by-N grid */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        fractions = new double[T];
        numTrials = T;
        for (int i = 0; i < T; i++) {
            Percolation p = pf.make(N);
            int numOpened = 0;
            while (!p.percolates()) {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                if (!p.isOpen(row, col)) {
                    p.open(row, col);
                    numOpened += 1;
                }
            }
            fractions[i] = (double) numOpened / N / N;
        }
    }
    /** sample mean of percolation threshold */
    public double mean() {
        return StdStats.mean(fractions);
    }
    /** sample standard deviation of percolation threshold */
    public double stddev() {
        return StdStats.stddev(fractions);
    }
    /** low endpoint of 95% confidence interval */
    public double confidenceLow() {
        double miu = mean();
        double sigma = stddev();
        return miu - 1.96 * sigma / Math.sqrt(numTrials);
    }
    /** high endpoint of 95% confidence interval */
    public double confidenceHigh() {
        double miu = mean();
        double sigma = stddev();
        return miu + 1.96 * sigma / Math.sqrt(numTrials);
    }
}
