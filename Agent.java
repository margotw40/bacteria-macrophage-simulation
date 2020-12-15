public class Agent implements AgentInterface
{
    private int ID;
    private int row;
    private int col;
    private AgentType type;
    private double nextETime;

    static final double INF = Double.POSITIVE_INFINITY;

    public Agent(AgentType which)
    {
        row = col = -1;
        type = which;
        nextETime = INF;
        ID = 0;
    }

    public int getID() { return(ID); }

    public int getRow() { return(row); }
    public int getCol() { return(col); }

    public double getNextETime() {return nextETime;}
    
    public AgentType getType() { return(type); }

    public void setRowCol(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    public void setNextETime( double time ) { nextETime = time; }
}
