import java.util.*;

public class alphabeta extends AIModule
{
	int player;
	int opponent;
    int maxDepth = 10;
    int numAtDepth = 0;
    int bestMoveSeen;
    HashMap<GameStateModule, Integer> evalMap = new HashMap<GameStateModule, Integer>();

	public void getNextMove(final GameStateModule game)
	{
        player = game.getActivePlayer();
        opponent = (game.getActivePlayer() == 1?2:1);
		//begin recursion
        alphabeta(game, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, player);
		chosenMove = bestMoveSeen;
		if(game.canMakeMove(chosenMove))
            game.makeMove(chosenMove);
	}

    private int alphabeta(final GameStateModule state, int depth, int alpha, int beta, int playerID)
    {
        if(depth == maxDepth)
        {
            return eval(state);
        }
        int bestVal = Integer.MIN_VALUE;
        depth++;
        //Max's turn
        if(player == playerID)
        {
            int v = Integer.MIN_VALUE + 1;
            Map<Integer, Integer> unsortedEvals = new HashMap<Integer, Integer>();
            for(int i = 0; i < state.getWidth(); i++)
            {
                if(state.canMakeMove(i))
                {
                    int currentEval;
                    state.makeMove(i);
                    currentEval = eval(state);
                    unsortedEvals.put(i, currentEval);
                    state.unMakeMove();
                }
            }
                //Sort the possible moves by descending order
            LinkedHashMap<Integer, Integer> sortedEvals = new LinkedHashMap<>();
            unsortedEvals.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedEvals.put(x.getKey(), x.getValue()));

            for(int move : sortedEvals.keySet())
            {
                state.makeMove(move);
                v = Math.max(v, alphabeta(state, depth, alpha, beta, opponent));
                state.unMakeMove();
                alpha = Math.max(v, alpha);
                if (v > bestVal)
                {
                    bestVal = v;
                    if(depth == 1)
                    {
                        bestMoveSeen = move;
                    }
                }
                if(alpha >= beta)
                {
                    break;
                }
            }
            return v;
        }
        //Min's turn
        else
        {
            int v = Integer.MAX_VALUE;
            Map<Integer, Integer> unsortedEvals = new HashMap<Integer, Integer>();
            for(int i = 0; i < state.getWidth(); i++)
            {
                if(state.canMakeMove(i))
                {
                    int currentEval;
                    state.makeMove(i);
                    currentEval = eval(state);
                    unsortedEvals.put(i, currentEval);
                    state.unMakeMove();
                }
            }
                //Sort the possible moves by descending order
            LinkedHashMap<Integer, Integer> sortedEvals = new LinkedHashMap<>();
            unsortedEvals.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sortedEvals.put(x.getKey(), x.getValue()));

            for(int move : sortedEvals.keySet())
            {
                if(state.canMakeMove(move))
                {
                    state.makeMove(move);
                    v = Math.min(v, alphabeta(state, depth, alpha, beta, player));
                    state.unMakeMove();
                    beta = Math.min(v, beta);
                    if(alpha >= beta)
                    {
                        break;
                    }
                }
            }
            return v;          
        }
    }

    private void numInARow(final GameStateModule state, int inARowMe[], int inARowOpponent[])
    {
        int x = state.getWidth();
        int y = state.getHeight();
        parseHorizontal(state, inARowMe, inARowOpponent, x, y);
        parseVertical(state, inARowMe, inARowOpponent, x, y);
        parseDiagonalBotL2TopR(state, inARowMe, inARowOpponent, x, y);
        parseDiagonalBotR2TopL(state, inARowMe, inARowOpponent, x, y);
    }

    private void parseHorizontal(final GameStateModule state, int inARowMe[], int inARowOpponent[], int width, int height)
    {
        for(int x = 0; x < width - 3; x++)
        {
            for(int y = 0; y < height; y++)
            {
                parseHorizontalFromXY(state, inARowMe, inARowOpponent, x, y);
            }
        }
    }

    private void parseHorizontalFromXY(final GameStateModule state, int inARowMe[], int inARowOpponent[], int x, int y)
    {
        int coinID = 0;
        int potentialPlayer = 0;
        int total = 0;
        for(int i = 0; i < 4; i++)
        {
            coinID = state.getAt(x + i, y);
            if(coinID == 0)
            {
                continue;
            }
            if(potentialPlayer == 0)
            {
                potentialPlayer = coinID;
            }
            if(coinID != potentialPlayer)
            {
                return;
            }
            total++;
        }
        if(total == 0)
        {
            return;
        }
        if(potentialPlayer == player)
        {
            inARowMe[total - 1]++;
        }
        else
        {
            inARowOpponent[total - 1]++;
        }
    }

    private void parseVertical(final GameStateModule state, int inARowMe[], int inARowOpponent[], int width, int height)
    {
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height - 3; y++)
            {
                parseVerticalFromXY(state, inARowMe, inARowOpponent, x, y);
            }
        }
    }

    private void parseVerticalFromXY(final GameStateModule state, int inARowMe[], int inARowOpponent[], int x, int y)
    {
        int coinID = 0;
        int potentialPlayer = 0;
        int total = 0;
        for(int i = 0; i < 4; i++)
        {
            coinID = state.getAt(x, y + i);
            if(coinID == 0)
            {
                continue;
            }
            if(potentialPlayer == 0)
            {
                potentialPlayer = coinID;
            }
            if(coinID != potentialPlayer)
            {
                return;
            }
            total++;
        }
        if(total == 0)
        {
            return;
        }
        if(potentialPlayer == player)
        {
            inARowMe[total - 1]++;
        }
        else
        {
            inARowOpponent[total - 1]++;
        }
    }

    private void parseDiagonalBotL2TopR(final GameStateModule state, int inARowMe[], int inARowOpponent[], int width, int height)
    {
        for(int x = 0; x < width - 3; x++)
        {
            for(int y = 0; y < height - 3; y++)
            {
                parseDiagonalBotL2TopRFromXY(state, inARowMe, inARowOpponent, x, y);
            }
        }
    }

    private void parseDiagonalBotL2TopRFromXY(final GameStateModule state, int inARowMe[], int inARowOpponent[], int x, int y)
    {
        int coinID = 0;
        int potentialPlayer = 0;
        int total = 0;
        for(int i = 0; i < 4; i++)
        {
            coinID = state.getAt(x + i, y + i);
            if(coinID == 0)
            {
                continue;
            }
            if(potentialPlayer == 0)
            {
                potentialPlayer = coinID;
            }
            if(coinID != potentialPlayer)
            {
                return;
            }
            total++;
        }
        if(total == 0)
        {
            return;
        }
        if(potentialPlayer == player)
        {
            inARowMe[total - 1]++;
        }
        else
        {
            inARowOpponent[total - 1]++;
        }
    }

    private void parseDiagonalBotR2TopL(final GameStateModule state, int inARowMe[], int inARowOpponent[], int width, int height)
    {
        for(int x = width - 1; x >= 3; x--)
        {
            for(int y = 0; y < height - 3; y++)
            {
                parseDiagonalBotR2TopLFromXY(state, inARowMe, inARowOpponent, x, y);
            }
        }
    }

    private void parseDiagonalBotR2TopLFromXY(final GameStateModule state, int inARowMe[], int inARowOpponent[], int x, int y)
    {
        int coinID = 0;
        int potentialPlayer = 0;
        int total = 0;
        for(int i = 0; i < 4; i++)
        {
            coinID = state.getAt(x - i, y + i);
            if(coinID == 0)
            {
                continue;
            }
            if(potentialPlayer == 0)
            {
                potentialPlayer = coinID;
            }
            if(coinID != potentialPlayer)
            {
                return;
            }
            total++;
        }
        if(total == 0)
        {
            return;
        }
        if(potentialPlayer == player)
        {
            inARowMe[total - 1]++;
        }
        else
        {
            inARowOpponent[total - 1]++;
        }
    }

	private int eval(final GameStateModule state){
        int inARowMe[] = {0, 0, 0, 0};
        int inARowOpponent[] = {0, 0, 0, 0};
        numInARow(state, inARowMe, inARowOpponent);
        if(state.isGameOver())
        {
            if(state.getWinner() == opponent)
            {
                return -10000000;
            }
            else
            {
                return 10000000;
            }
        }
        else{
            return(inARowMe[0] + 100 * inARowMe[1] + 10000 * inARowMe[2] - (inARowOpponent[0] + 100 * inARowOpponent[1] + 10000 * inARowOpponent[2]));
        }
    }
}
