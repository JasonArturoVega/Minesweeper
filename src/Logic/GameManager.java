package Logic;

public class GameManager {

    public GameManager()
    {
        gameState = MainState.PLAYING;
    }

    public enum MainState{PLAYING, WIN, LOSE};
    private static MainState gameState;

    public void gameOver()
    {
        gameState = MainState.LOSE;
    }

    public static void win()
    {
        gameState = MainState.WIN;
    }

    public void restart(){ gameState = MainState.PLAYING;}

    public MainState getMainState()
    {
        return gameState;
    }

}
