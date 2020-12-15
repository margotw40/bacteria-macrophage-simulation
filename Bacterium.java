import java.util.*;
import java.math.*;
/**
 * Write a description of class Macrophage here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bacterium extends Agent
{
    private double divideTime;
    private double moveTime;
    private String nextEType;
    private double rate;
    private double init;

    private static double consumptionRateMean;
    private static double rateSD;
    private static double initResourceMean;
    private static double resourceSD;

    static final int seed = 40291947;
    static final double INF = Double.POSITIVE_INFINITY;
    static Random rng = new Random(seed);
    private static double interMove;
    private static double interDivide;

    /**
     * Constructor for objects of class Bacterium
     */
    public Bacterium()
    {
        super(Agent.AgentType.BACTERIUM);
        this.moveTime = exp(interMove);
        this.divideTime = exp(interDivide);
        this.rate = normal(consumptionRateMean, rateSD);
        this.init = normal(initResourceMean, resourceSD);
        this.update();
    }
    
    public Bacterium(double time, int row, int col)
    {
        super(Agent.AgentType.BACTERIUM);
        this.moveTime = time + exp(interMove);
        this.divideTime = time + exp(interDivide);
        this.setRowCol(row, col);
        this.update();
    }

    public static void setConsumptionMean(double val)
    {
        consumptionRateMean = val;
    }

    public static void setRateSD(double val)
    {
        rateSD = val;
    }

    public static void setInitMean(double val)
    {
        initResourceMean = val;
    }

    public static void setResourceSD(double val)
    {
        resourceSD = val;
    }

    public static void setInterDivide(double val)
    {
        interDivide = val;
    }

    public static void setInterMove(double val)
    {
        interMove = val;
    }

    public double getDivideTime()
    {
        return this.divideTime;
    }

    public double getMoveTime()
    {
        return this.moveTime;
    }

    private void update()
    {
        if( divideTime <= moveTime )
        {
            nextEType = "DIVIDE";
            this.setNextETime( divideTime );
        }
        else
        {
            nextEType = "MOVE";
            this.setNextETime( moveTime );
        }
    }

    public String getNextEType()
    {
        return this.nextEType;
    }

    public static double exp(double m)
    {
        return(-m * Math.log(1.0 - rng.nextDouble()));
    }

    public void move()
    {
        int curRow = this.getRow();
        int curCol = this.getCol();
        int dim = Landscape.getDimension();

        ArrayList<Integer> rows = new ArrayList<Integer>();
        ArrayList<Integer> cols = new ArrayList<Integer>();

        //Goes through the Moore neighborhood and searchs for a records any surrounding bacteria
        for(int r = -1; r <= 1; r++)
        {
            for(int c = -1; c <= 1; c++)
            {
                if(Landscape.getAgent((this.getRow() + r + dim) % dim, (this.getCol() + c + dim) % dim, AgentInterface.AgentType.BACTERIUM) == null)
                {
                    //If there is an empty cell record its position in the lists
                    rows.add((this.getRow() + r + dim) % dim);
                    cols.add((this.getCol() + c + dim) % dim);
                }

            }
        }

        if(rows.size() > 0) //If there are empty cells move to one of them
        {
            if(rows.size() == 1)
            {
                //There is only one empty place so we move to that place preferentially no matter what
                Landscape.moveAgent(curRow, curCol, rows.get(0), cols.get(0), this);
                }
            else
            {
                //Otherwise pick one of the empty locations randomly and move to its position
                int rand = rng.nextInt(rows.size()); 
                Landscape.moveAgent(curRow, curCol, rows.get(rand), cols.get(rand), this);
            }
        }

        if( Landscape.getMac(this.getRow(), this.getCol()) != null )
        {
            Macrophage mac = Landscape.getMac(this.getRow(), this.getCol());
            mac.setEatTime(this.moveTime);
        }
        
        this.moveTime = this.moveTime + exp(interMove);
        this.update();
    }

    public Bacterium divide()
    {
        int curRow = this.getRow();
        int curCol = this.getCol();
        int dim = Landscape.getDimension();

        ArrayList<Integer> rows = new ArrayList<Integer>();
        ArrayList<Integer> cols = new ArrayList<Integer>();

        //Goes through the Moore neighborhood and searchs for a records any surrounding bacteria
        for(int r = -1; r <= 1; r++)
        {
            for(int c = -1; c <= 1; c++)
            {
                if(Landscape.getAgent((this.getRow() + r + dim) % dim, (this.getCol() + c + dim) % dim, AgentInterface.AgentType.BACTERIUM) == null)
                {
                    //If there is a bacterium in any of the cells record its position in the lists
                    rows.add((this.getRow() + r + dim) % dim);
                    cols.add((this.getCol() + c + dim) % dim);
                }
            }
        }
        Bacterium newBac = null;
        if(rows.size() > 0) //If there are empty spots you can add there
        {
            if(rows.size() == 1)
            {
                //There is only one empty place so we divide there
                // plop it there and update landscape  
                newBac = new Bacterium(this.divideTime, rows.get(0), cols.get(0));
                Landscape.addAgent(rows.get(0), cols.get(0), newBac);
            }
            else
            {
                //Otherwise pick one of the empty locations randomly and divide there
                int rand = rng.nextInt(rows.size()); 
                newBac = new Bacterium(this.divideTime, rows.get(rand), cols.get(rand));
                Landscape.addAgent(rows.get(rand), cols.get(rand), newBac);
            }

            if( Landscape.getMac(newBac.getRow(), newBac.getCol()) != null )
            {
                Macrophage mac = Landscape.getMac(newBac.getRow(), newBac.getCol());
                mac.setEatTime(this.moveTime);
            }
        }

        this.divideTime = this.divideTime + exp(interDivide);
        this.update();
        return newBac;
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

