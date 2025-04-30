package model;

import org.apache.commons.math3.stat.*;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.distribution.TDistribution;
import java.util.List;

public class StatisticsCalculator {
    public static double calculateMean(List<Double> data) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        data.forEach(stats::addValue);
        return stats.getMean();
    }

    public static double calculateGeometricMean(List<Double> data) {
        double product = 1.0;
        for (double num : data) {
            product *= num;
        }
        return Math.pow(product, 1.0 / data.size());
    }

    public static double calculateStandardDeviation(List<Double> data) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        data.forEach(stats::addValue);
        return stats.getStandardDeviation();
    }

    public static double calculateRange(List<Double> data) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        data.forEach(stats::addValue);
        return stats.getMax() - stats.getMin();
    }

    public static double calculateCovariance(List<Double> data1, List<Double> data2) {
    double[] array1 = data1.stream().mapToDouble(Double::doubleValue).toArray();
    double[] array2 = data2.stream().mapToDouble(Double::doubleValue).toArray();
    
    return new Covariance().covariance(array1, array2);
}

    public static double calculateVariance(List<Double> data) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        data.forEach(stats::addValue);
        return stats.getVariance();
    }

    public static double calculateVariationCoefficient(List<Double> data) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        data.forEach(stats::addValue);
        return stats.getStandardDeviation() / stats.getMean();
    }

    public static double[] calculateConfidenceInterval(List<Double> data, double confidenceLevel) {
    DescriptiveStatistics stats = new DescriptiveStatistics();
    data.forEach(stats::addValue);
    
    double mean = stats.getMean();
    double stdDev = stats.getStandardDeviation();
    double n = stats.getN();
    
    TDistribution tDistribution = new TDistribution(n - 1);
    double criticalValue = tDistribution.inverseCumulativeProbability(1 - (1 - confidenceLevel) / 2);
    
    double marginOfError = criticalValue * stdDev / Math.sqrt(n);
    
    return new double[]{mean - marginOfError, mean + marginOfError};
    }
}
