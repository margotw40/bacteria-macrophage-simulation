import java.util.*;
import java.math.*;
/**
 * Write a description of class Macrophage here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Macrophage extends Agent
{
    // instance variables - replace the example below with your own
    private double divideTime;
    private double eatTime;
    private double moveTime;
    private String nextEType;
    private static int minBact;

    static final int seed = 8675309;
    static final double INF = Double.POSITIVE_INFINITY;
    static Random rng = new Random(seed);
    private static double interMove;
    private static double interDivide;

    /**
     * Constructor for objects of class Macrophage
     */
    public Macrophage()
    {
        super(Agent.AgentType.MACROPHAGE);
        this.eatTime = INF;
        this.moveTime = exp(interMove);
        this.divideTime = exp(interDivide);
        this.update();
    }

    public Macrophage(double time, int row, int col)
    {
        super(Agent.AgentType.MACROPHAGE);
        this.moveTime = time + exp(interMove);
        this.divideTime = time + exp(interDivide);
        this.setRowCol(row, col);
        this.update();
    }

    public static void setInterDivide(double val)
    {
        interDivide = val;
    }

    public static void setInterMove(double val)
    {
        interMove = val;
    }

    public static void setMinBact(int val)
    {
        minBact = val;
    }
    
    public double getEatTime()
    {
        return this.eatTime;
    }

    public double getMoveTime()
    {
        return this.moveTime;
    }   

    public double getDivideTime()
    {
        return this.divideTime;
    }

    private void update()
    {
        if( eatTime <= moveTime && eatTime <= divideTime )
        {
            nextEType = "EAT";
            this.setNextETime( eatTime );
        }
        else if (moveTime <= divideTime)
        {
            nextEType = "MOVE";
            this.setNextETime( moveTime );
        }
        else
        {
            nextEType = "DIVIDE";
            this.setNextETime( divideTime );
        }
    }
    
    public void setEatTime( double t )
    {
        this.eatTime = t;
        this.update();
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
        ArrayList<Integer> rows = new ArrayList<Integer>();
        ArrayList<Integer> cols = new ArrayList<Integer>();
        ArrayList<Integer> emptyRows = new ArrayList<Integer>();
        ArrayList<Integer> emptyCols = new ArrayList<Integer>();
        int dim = Landscape.getDimension();

        //Goes through the Moore neighborhood and searchs for a records any surrounding bacteria
        for(int r = -1; r <= 1; r++)
        {
            for(int c = -1; c <= 1; c++)
            {
                if(Landscape.getAgent((this.getRow() + r + dim) % dim, (this.getCol() + c + dim) % dim, AgentInterface.AgentType.BACTERIUM) != null && Landscape.getAgent((this.getRow() + r + dim) % dim, (this.getCol() + c + dim) % dim, AgentInterface.AgentType.MACROPHAGE) == null)
                {
                    //If there is a bacterium in any of the cells record its position in the lists
                    rows.add((this.getRow() + r + dim) % dim);
                    cols.add((this.getCol() + c + dim) % dim);
                }
                else if(Landscape.getAgent((this.getRow() + r + dim) % dim, (this.getCol() + c + dim) % dim, AgentInterface.AgentType.BACTERIUM) == null && Landscape.getAgent((this.getRow() + r + dim) % dim, (this.getCol() + c + dim) % dim, AgentInterface.AgentType.MACROPHAGE) == null)
                {
                    //If there is a bacterium in any of the cells record its position in the lists
                    emptyRows.add((this.getRow() + r + dim) % dim);
                    emptyCols.add((this.getCol() + c + dim) % dim);
                }
            }
        }

        int curRow = this.getRow();
        int curCol = this.getCol();

        if(rows.size() > 0) //If there are bacteria go get one of them
        {
            System.out.println("Macrophage hunting down bacterium");
            if(rows.size() == 1)
            {
                //There is only one bacterium so we move to that place preferentially no matter what
                Landscape.moveAgent(curRow, curCol, rows.get(0), cols.get(0), this);
            }
            else
            {
                //Otherwise pick one of the bacteria randomly and move to its position
                int rand = rng.nextInt(rows.size()); 
                Landscape.moveAgent(curRow, curCol, rows.get(rand), cols.get(rand), this);
            }
            this.eatTime = this.moveTime;
        }
        else if(emptyRows.size() > 0)//Otherwise just move to an unoccupied location
        {
            System.out.println("Macrophage going on random walk");
            if(emptyRows.size() == 1)
            {
                //There is only one bacterium so we move to that place preferentially no matter what
                Landscape.moveAgent(curRow, curCol, emptyRows.get(0), emptyCols.get(0), this);
            }
            else
            {
                //Otherwise pick one of the bacteria randomly and move to its position
                int rand = rng.nextInt(emptyRows.size()); 
                Landscape.moveAgent(curRow, curCol, emptyRows.get(rand), emptyCols.get(rand), this);
            }
        }
                
        this.moveTime = this.moveTime + exp(interMove);
        this.update();
    }

    public Macrophage divide()
    {
        int curRow = this.getRow();
        int curCol = this.getCol();
        int dim = Landscape.getDimension();

        ArrayList<Integer> rows = new ArrayList<Integer>();
        ArrayList<Integer> cols = new ArrayList<Integer>();

        int bacCount = 0;
        Macrophage newMac = null;

        //Goes through the Moore neighborhood and searchs for a records any surrounding macrophage
        for(int r = -1; r <= 1; r++)
        {
            for(int c = -1; c <= 1; c++)
            {
                if(Landscape.getAgent((this.getRow() + r + dim) % dim, (this.getCol() + c + dim) 
                    % dim, AgentInterface.AgentType.MACROPHAGE) == null)
                {
                    //If there is a cell without a macrophage
                    rows.add((this.getRow() + r + dim) % dim);
                    cols.add((this.getCol() + c + dim) % dim);
                }

                if(Landscape.getAgent((this.getRow() + r + dim) % dim, (this.getCol() + c + dim) 
                    % dim, AgentInterface.AgentType.BACTERIUM) != null)
                    //Count the number of bacteria in the moore neighborhood
                {
                    bacCount++;
                }
            }
        }

        if(bacCount >= minBact)
        {
            if(rows.size() > 0) //If there are empty spots you can add there
            {
                if(rows.size() == 1)
                {
                    //There is only one empty place so we divide there
                    // plop it there and update landscape  
                    newMac = new Macrophage(this.divideTime, rows.get(0), cols.get(0));
                    Landscape.addAgent(rows.get(0), cols.get(0), newMac);
                }
                else
                {
                    //Otherwise pick one of the empty locations randomly and divide there
                    int rand = rng.nextInt(rows.size()); 
                    newMac = new Macrophage(this.divideTime, rows.get(rand), cols.get(rand));
                    Landscape.addAgent(rows.get(rand), cols.get(rand), newMac);
                }
            }

            if( Landscape.getBac(newMac.getRow(), newMac.getCol()) != null )
            {
                newMac.setEatTime(this.divideTime);
            }
        }

        this.divideTime = this.divideTime + exp(interDivide);
        this.update();
        return newMac;
    }
    
    public Bacterium eat()
    {
        Bacterium a = Landscape.getBac(this.getRow(), this.getCol());
        if( a != null )
        {
            Landscape.deleteAgent(this.getRow(), this.getCol(), (Agent)a);
        }
        this.eatTime = INF;
        this.update();
        return a;
    }
}
