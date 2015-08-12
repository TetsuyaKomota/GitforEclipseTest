package komota.supers;

public final class NormalDistribution {

    private double mean;
    private double variance;
    private double[] mean2d;
    private double[][] variance2d;

    public NormalDistribution(double mean, double variance) {
        if (variance < 0.0) {
            throw new IllegalArgumentException("Variance must be non-negative. Given variance: " + variance);
        }
        this.mean = mean;
        this.variance = variance;
    }

    public double frequencyOf(double value) {
        if (this.variance == 0.0) {
            return this.mean == value ? 1.0 : 0.0;
        }
        return Math.exp(-0.5 * Math.pow(value - this.mean, 2.0) / this.variance)
                / Math.sqrt(2.0 * Math.PI * this.variance);
    }

    public double random() {
        double c = Math.sqrt(-2.0 * Math.log(Math.random()));
        if (Math.random() < 0.5) {
            return c * Math.sin(2.0 * Math.PI * Math.random()) * this.variance + this.mean;
        }
        return c * Math.cos(2.0 * Math.PI * Math.random()) * this.variance + this.mean;
    }
    public NormalDistribution(double[] mean, double[][] variance) {
        if (variance[0][0] < 0.0) {
            throw new IllegalArgumentException("Variance must be non-negative. Given variance: " + variance);
        }
        this.mean2d = mean;
        this.variance2d = variance;
    }

    public double frequencyOf_2D(double[] value) {
        if (this.variance == 0.0) {
            return (this.mean2d[0] == value[0] && this.mean2d[1] == value[1]) ? 1.0 : 0.0;
        }

        double m1 = value[0] - mean2d[0];
        double m2 = value[1] - mean2d[1];

        double lambda = this.variance2d[0][0]*variance2d[1][1] - variance2d[1][0]*variance2d[0][1];

        double temp = (m1*m1*variance2d[1][1] + m2*m2*variance2d[0][0] - m1*m2*(variance2d[0][1]+variance2d[1][0]))/lambda;

        return Math.exp(-0.5 * temp) / Math.sqrt(2.0 * Math.PI * lambda);
    }

    public double random_2D() {
        double c = Math.sqrt(-2.0 * Math.log(Math.random()));
        if (Math.random() < 0.5) {
            return c * Math.sin(2.0 * Math.PI * Math.random()) * this.variance + this.mean;
        }
        return c * Math.cos(2.0 * Math.PI * Math.random()) * this.variance + this.mean;
    }
}