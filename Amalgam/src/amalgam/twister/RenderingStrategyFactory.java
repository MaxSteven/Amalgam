package amalgam.twister;

public class RenderingStrategyFactory {

	public static RenderingStrategy getRandomStrategy(Spinner spinner) {
		RenderingStrategy strat;

		int N = 3;
		int R = (int) (N * (float) Math.random());

		switch (R) {
			case 0 :
				strat = new ConcreteRenderStrategy_Basic(spinner);
				break;
			case 1 :
				strat = new ConcreteRenderStrategy_Checkerboard(spinner);
				break;
			case 2 :
				strat = new ConcreteRenderStrategy_Faded(spinner);
				break;
			default :
				strat = new ConcreteRenderStrategy_Faded(spinner);
				break;
		}
		return strat;
	}

}
