import java.util.ArrayList;
import java.util.*;

/**
 * This class implements the next-event simulation engine for your agent-based
 * simulation.  You may choose to define other helper classes (e.g., Event,
 * EventList, etc.), but the main while loop of your next-event engine should
 * appear here, in the run(...) method.
 */

public class Simulation extends SimulationManager
{
    // you may choose to have two separate lists, or only one list of all
    private ArrayList<Agent> macrophageList;
    private ArrayList<Agent> bacteriaList;
    private Random rng;


    /**************************************************************************
     * Constructs a Simulation object.  This should just perform initialization
     * and setup.  Later use the object to .run() the simulation.
     *
     * @param numCells       number of rows and columns in the environment
     * @param guiCellWidth   width of each cell drawn in the gui
     * @param numMacrophages number of initial macrophages in the environment
     * @param numBacteria    number of initial bacteria in the environment
     **************************************************************************/
    public Simulation(int numCells,       int guiCellWidth,
                      int numMacrophages, int numBacteria, double maxTime)
    {
        // call the SimulationManager constructor, which itself makes sure to 
        // construct and store an AgentGUI object for drawing
        super(numCells, guiCellWidth, maxTime);
        
        Landscape.setDim( numCells );

        time = 0;
        macrophageList = new ArrayList<Agent>();
        bacteriaList   = new ArrayList<Agent>();
        rng = new Random();

        //Drop the baceria and macrophages at random on the landscape, don't drop them on top of each other at first    
        int row = 0, col = 0;
        for (int i = 0; i < numCells; i++)
        {
            for (int j = 0; j < numCells; j++)
            {
                Cell c = new Cell(i,j);
                Landscape.addCell( c, i, j );
            }
        }
        for (int i = 0; i < numMacrophages; i++)
        {
            Macrophage a = new Macrophage();//just make new macrophages?
            row = rng.nextInt(numCells);
            col = rng.nextInt(numCells);
            while(Landscape.getMac(row, col) != null || Landscape.getBac(row, col) != null)
            {
                row = rng.nextInt(numCells);
                col = rng.nextInt(numCells);
            }
            a.setRowCol(row, col);
            Landscape.addAgent(row, col, a);
            macrophageList.add((Agent)a);//Cast to agent here?
            System.out.println("Macrophage number: " + i + " Next Move Time: " + a.getMoveTime() + " Next Eat Time: " + a.getEatTime() + " Next Divide Time: " + a.getDivideTime() );
        }

        for (int i = 0; i < numBacteria; i++)
        {
            Bacterium a = new Bacterium();//just make new bacteria?
            row = rng.nextInt(numCells);
            col = rng.nextInt(numCells);
            while(Landscape.getMac(row, col) != null || Landscape.getBac(row, col) != null)
            {
                row = rng.nextInt(numCells);
                col = rng.nextInt(numCells);
            }
            a.setRowCol(row, col);
            Landscape.addAgent(row, col, a);
            bacteriaList.add((Agent)a);//Cast to agent here?
            System.out.println("Bacterium number: " + i + " Next Move Time: " + a.getMoveTime() + " Next Divide Time: " + a.getDivideTime() );
        }

    }

    public double getMaxTime()
    {
        return this.maxTime;
    }

    /**************************************************************************
     * Method used to run the simulation.  This method should contain the
     * implementation of your next-event simulation engine (while loop handling
     * various event types).
     *
     * @param guiDelay  delay in seconds between redraws of the gui
     **************************************************************************/
    public void run(double guiDelay) throws InterruptedException 
    {  
        this.gui = new AgentGUI(this, numCells, guiCellWidth);

        while(time < getMaxTime())
        {
            Agent nextAgent = Landscape.getNextEvent();
            time = nextAgent.getNextETime();
            System.out.println("Next time is: " + time);
            String etype = null;
            Macrophage nextMac = null;
            Bacterium nextBac = null;

            AgentInterface.AgentType atype = nextAgent.getType();
            if(atype == AgentInterface.AgentType.MACROPHAGE)
            {
                nextMac = (Macrophage)nextAgent;
                etype = nextMac.getNextEType();
            }
            else
            {
                nextBac = (Bacterium)nextAgent;
                etype = nextBac.getNextEType();
            }


            if(etype == "EAT")
            {
                System.out.println();
                System.out.println("Processing EAT Event");
                System.out.println();
                Bacterium b = nextMac.eat();
                bacteriaList.remove(b);
            }
            else if(etype == "MOVE")
            {
                if(atype == AgentInterface.AgentType.MACROPHAGE)
                {   
                    System.out.println();
                    System.out.println("Processing MACROPHAGE MOVE Event");
                    System.out.println();
                    nextMac.move();
                }
                else
                {
                    System.out.println();
                    System.out.println("Processing BACTERIUM MOVE Event");
                    System.out.println();
                    nextBac.move(); 
                } 
            }
            else if(etype == "DIVIDE")
            {
                if(atype == AgentInterface.AgentType.BACTERIUM)
                {
                    System.out.println();
                    System.out.println("Processing BACTERIUM DIVIDE Event");
                    System.out.println();
                    Bacterium b = nextBac.divide();
                    bacteriaList.add(b);
                }
                else
                {
                    System.out.println();
                    System.out.println("Processing MACROPHAGE DIVIDE Event");
                    System.out.println();
                    Macrophage m = nextMac.divide();
                    if(m != null) {
                        macrophageList.add(m);
                    }
                }
            }
            gui.update(guiDelay); 
        }
    }

    /**************************************************************************
     * Accessor method that returns the number of macrophages still present.
     * @return an integer representing the number of macrophages present
     **************************************************************************/
    public int getNumMacrophages() { return(macrophageList.size()); }

    /**************************************************************************
     * Accessor method that returns the number of bacteria still present.
     * @return an integer representing the number of bacteria present
     **************************************************************************/
    public int getNumBacteria()    { return(bacteriaList.size()); }

    /**************************************************************************
     * Accessor method that returns the current time of the simulation clocl.
     * @return a double representing the current time in simulated time
     **************************************************************************/
    public double getTime()        { return(time); }

    /**************************************************************************
     * Method that constructs and returns a single list of all agents present.
     * This method is used by the gui drawing routines to update the gui based
     * on the number and positions of agents present.
     *
     * @return an ArrayList<AgentInterface> containing references to all macrophages and bacteria
     **************************************************************************/
    public ArrayList<AgentInterface> getListOfAgents()
    {
        // your implementation may differ depending on one or two lists...
        ArrayList<AgentInterface> returnList = new ArrayList<AgentInterface>();
        for (int i = 0; i < macrophageList.size(); i++) returnList.add( macrophageList.get(i) );
        for (int i = 0; i < bacteriaList.size(); i++)   returnList.add( bacteriaList.get(i) );
        return(returnList);
    }
}
