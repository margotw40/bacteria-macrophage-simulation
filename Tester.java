public class Tester
{
    public static void main(String[] args) throws InterruptedException
    {
        // construct a simulation object w/ appropriate parameters and then run
        int numCells       = 20;
        int guiCellWidth   = 10;
        int numMacrophages = 10;
        int numBacteria    = 30;
        double maxTime     = 30;

        Simulation s = new Simulation(numCells, guiCellWidth,
                                      numMacrophages, numBacteria, maxTime);
        double guiDelayInSecs = 0.1;
        s.run(guiDelayInSecs);
    }
}
