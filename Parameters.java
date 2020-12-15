public class Parameters
{
	// bacteria parameter values
	protected static double CONSUMPTION_RATE_MEAN   = 1.0;  // Normal(m,s)
	protected static double CONSUMPTION_RATE_SD     = 0.2;
	protected static double INIT_RESOURCE_MEAN      = 1.0;  // Normal(m,s)
	protected static double INIT_RESOURCE_SD        = 0.5;
	protected static double BACT_INTER_MOVE         = 1.0;  // Exp(m)
	protected static double BACT_INTER_DIVIDE       = 20.0; // Exp(m)
	// macrophage parameter values
	protected static double MACRO_INTER_MOVE        = 3.0;  // Exp(m)
	protected static double MACRO_INTER_DIVIDE      = 20.0; // Exp(m)
	protected static int    MIN_BACT_TO_DIVIDE      = 1;
	// cell parameter values
	protected static double REGROWTH_RATE_MEAN      = 1.0;  // Normal(m,s)
	protected static double REGROWTH_RATE_SD        = 0.1;
	protected static double MAX_RESOURCE_MEAN       = 1.0;  // Normal(m,s)
	protected static double MAX_RESOURCE_SD         = 0.25;
}