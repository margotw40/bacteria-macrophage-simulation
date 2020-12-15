import java.util.*;

public class Landscape
{
    static int numRows = 40;
    static int numCols = 40;
//    static Macrophage[][] macLandscape = new Macrophage[numRows][numCols];
//    static Bacterium[][] bacLandscape = new Bacterium[numRows][numCols];
    static ArrayList<Agent> agents = new ArrayList<Agent>();
    static Cell[][] cells = new Cell[numRows][numCols]; // to hold all the cells

    static void addCell( Cell cell, int r, int c )
    {
        cells[r][c] = cell;
    }

    static void setDim( int num )
    {
        numRows = num;
        numCols = num;
    }
    
    /**
    * Returns the Agent instance at the requested cell. Can get type info, and other bits.
    */
    static Agent getAgent(int r, int c, AgentInterface.AgentType t)
    {
        if(t == AgentInterface.AgentType.MACROPHAGE)
        {
            return cells[r][c].getMac();
        }
        return cells[r][c].getBac();
    }
    
    static Macrophage getMac(int r, int c)
    {
        return cells[r][c].getMac();
    }
    
    static Bacterium getBac(int r, int c)
    {
        return cells[r][c].getBac();
    }

    static void deleteAgent(int r, int c, Agent a)
    {
        if(a.getType() == AgentInterface.AgentType.MACROPHAGE)
        {
            cells[r][c].setMac(null);
        }
        else
        {
            cells[r][c].setBac(null);
        }
        agents.remove(a);
    }

    //Make add mac and add bac methods so we don't have to cast?
    static void addAgent(int r, int c, Agent a)
    {
        System.out.println("Agent time: " + a.getNextETime());
        if(a.getType() == AgentInterface.AgentType.MACROPHAGE)
        {
            cells[r][c].setMac((Macrophage)a);
        }
        else
        {
            cells[r][c].setBac((Bacterium)a);
        }
        agents.add(a);
        System.out.println("Adding agent with time " + a.getNextETime());
    }

    static void moveAgent(int oldR, int oldC, int newR, int newC, Agent a)
    {
        Agent tmp = getAgent(oldR, oldC, a.getType());
        Landscape.deleteAgent(oldR, oldC, a);
        Landscape.addAgent(newR, newC, tmp);
        tmp.setRowCol(newR, newC);
    }

    static int getDimension()
    {
        return numRows;
    }

    static Agent getNextEvent()
    {
        Agent a = null;
        if(agents.size() > 0)
        {
            a = agents.get(0);
            for(int i = 0; i < agents.size(); i++)
            {
                //System.out.println("Comparing: " + agents.get(i).getNextETime() + " , " + a.getNextETime());
                if(agents.get(i).getNextETime() < a.getNextETime())
                    a = agents.get(i);
            }
        }
        return a;
    }
}
