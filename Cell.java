import java.util.*;

public class Cell
{
    private Macrophage mac;
    private Bacterium bac;
    private int r;
    private int c;
    private double lastDepleted;
    private double rate;
    private double max;

    static final int seed = 40291947;
    static Random rng = new Random(seed);

    private static double productionRateMean;
    private static double rateSD;
    private static double maxResourceMean;
    private static double resourceSD;

    
    public Cell( int row, int col )
    {
        this.r = row;
        this.c = col;
        this.rate = normal(productionRateMean, rateSD);
        this.max = normal(maxResourceMean, resourceSD);
        this.lastDepleted = 0;
    }

    public static void setRateMean(double val)
    {
        productionRateMean = val;
    }

    public static void setRateSD(double val)
    {
        rateSD = val;
    }

    public static void setResourceMean(double val)
    {
        maxResourceMean = val;
    }

    public static void setResourceSD(double val)
    {
        resourceSD = val;
    }

    public Macrophage getMac()
    {
        return this.mac;
    }

    public Bacterium getBac()
    {
        return this.bac;
    }

    public void setMac( Macrophage m )
    {
        this.mac = m;
    }

    public void setBac( Bacterium b )
    {
        this.bac = b;
    }

    public double getLastDepleted()
    {
        return this.lastDepleted;
    }

    public void setLastDepleted( double time )
    {
        this.lastDepleted = time;
    }

    private double normal(double m, double s)
    { 
        final double p0 = 0.322232431088;     final double q0 = 0.099348462606;
        final double p1 = 1.0;                final double q1 = 0.588581570495;
        final double p2 = 0.342242088547;     final double q2 = 0.531103462366;
        final double p3 = 0.204231210245e-1;  final double q3 = 0.103537752850;
        final double p4 = 0.453642210148e-4;  final double q4 = 0.385607006340e-2;
        double u, t, p, q, z;
        
        u   = rng.nextDouble();
        if (u < 0.5)
            t = Math.sqrt(-2.0 * Math.log(u));
        else
            t = Math.sqrt(-2.0 * Math.log(1.0 - u));
        p   = p0 + t * (p1 + t * (p2 + t * (p3 + t * p4)));
        q   = q0 + t * (q1 + t * (q2 + t * (q3 + t * q4)));
        if (u < 0.5)
            z = (p / q) - t;
        else
            z = t - (p / q);
        return (m + s * z);
    }

}
