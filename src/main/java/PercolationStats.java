import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by David on 03/01/2017.
 */

public class PercolationStats {

    private final double[] percolationThresholds;
    private final int T;
    private final int N;
    private double mean;
    private double stddev;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials){
        if(n <= 0 || trials <= 0){
            throw new IllegalArgumentException("Size and trials arguments should be positive number");
        }
        this.N = n;
        this.T = trials;
        this.percolationThresholds = new double[T];
    }

    // sample mean of percolation threshold
    public double mean(){
        //calculate mean for all thresholds calculated for each trial
        if(mean <= 0.0){
            mean = Arrays.stream(this.percolationThresholds).average().getAsDouble();
        }
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        if(stddev <= 0.0) {
            stddev = Arrays.stream(this.percolationThresholds)
                    .map(t -> Math.pow(t - mean(), 2))
                    .sum() / (T-1);
        }
        return stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo(){
        return mean() - ((1.96*stddev)/Math.sqrt(T));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return mean() + ((1.96*stddev)/Math.sqrt(T));
    }

    private int randomIndex(Random generator){
        return (generator.nextInt(this.N)+1);
    }

    private double calculationThresholdForTrial(Random randomGenerator, int trial) {
        //get new percolation
        Percolation percolation = new Percolation(this.N);
        while(!percolation.percolates()){
            percolation.open(this.randomIndex(randomGenerator), this.randomIndex(randomGenerator));
        }
        return (double)percolation.numberOfOpenSites() / (double)(this.N*this.N);
    }

    public static void main(String[] args) {
        //get size
        final int size = Integer.parseInt(args[0]);

        //get trials
        final int trials = Integer.parseInt(args[1]);
        final PercolationStats percolationStats = new PercolationStats(size, trials);
        final long currentTime = System.currentTimeMillis();
        final Random randomGenerator = new Random(currentTime);

        final long start = System.currentTimeMillis();
        IntStream.range(0, trials).parallel().forEach(t -> {
            double percolationThreshold = percolationStats.calculationThresholdForTrial(randomGenerator, t);
            percolationStats.percolationThresholds[t] = percolationThreshold;
        });

        final long end = System.currentTimeMillis();
        System.out.println(String.format("mean\t\t\t\t\t= %f",percolationStats.mean()));
        System.out.println(String.format("stddev\t\t\t\t\t= %f",percolationStats.stddev()));
        System.out.println(String.format("95 confidence interval\t= [%f,%f]",percolationStats.confidenceLo(), percolationStats.confidenceHi()));
        System.out.println(String.format("TS\t\t\t\t\t= %d",end-start));

    }

}
