import java.util.*;

public class minimax extends AIModule
{
	int player;
	int opponent;
	int maxDepth = 5;
	int bestMoveSeen;

	public void getNextMove(final GameStateModule game)
	{
        player = game.getActivePlayer();
        opponent = (game.getActivePlayer() == 1?2:1);
		//begin recursion
		while(!terminate){
			minimax(game, 0, player);
            if(!terminate)
				chosenMove = bestMoveSeen;
		}
		if(game.canMakeMove(chosenMove))
			game.makeMove(chosenMove);
	}

	private int minimax(final GameStateModule state, int depth, int playerID) {
        if (terminate)
            return 0;
        if (depth == maxDepth) {
            return eval(state);
        }
        depth++;
        int value = 0;
        //max's turn
        int bestVal = Integer.MIN_VALUE;
        if(playerID == player){
            value = Integer.MIN_VALUE + 1;
            for(int i = 0; i < state.getWidth(); i++){
                if(state.canMakeMove(i)) {
                    state.makeMove(i);
                    value = Math.max(value, minimax(state, depth, opponent));
                    state.unMakeMove();
                    if (value > bestVal){
                        bestVal = value;
                        if (depth == 1) { //top of recursion, make our move choice
                            bestMoveSeen = i;
                        }
                    }
                }
            }
            return value;
        }

        else { //min's turn
            value = Integer.MAX_VALUE;
            for(int i = 0; i < state.getWidth(); i++) {
                if (state.canMakeMove(i)) {
                    state.makeMove(i);
                    value = Math.min(value, minimax(state, depth, player));
                    state.unMakeMove();
                }
            }
            return value;
        }
    }

    private void numInARow(final GameStateModule state, int inARowP1[], int inARowP2[])
    {
        int x = state.getWidth();
        int y = state.getHeight();
        parseHorizontal(state, inARowP1, inARowP2, x, y);
        parseVertical(state, inARowP1, inARowP2, x, y);
        parseDiagonal(state, inARowP1, inARowP2, x, y);
    }

    private void parseHorizontal(final GameStateModule state, int inARowP1[], int inARowP2[], int width, int height)
    {
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width - 3; x++)
            {
                int playerID = state.getAt(x, y);
                if(playerID == 0)
                {
                    continue;
                }
                int consec = 0;
                boolean stillGoing = true;
                boolean blocked = false;
                for(int i = 0; i < 4; i++)
                {
                    if(x >= width)
                    {
                        break;
                    }
                    int coinID = state.getAt(x, y);
                    if(stillGoing && playerID == coinID)
                    {
                        consec++;
                        x++;
                        continue;
                    }
                    if(coinID != 0 && coinID != playerID)
                    {
                        blocked = true;
                        break;
                    }
                    if(coinID == 0)
                    {
                        stillGoing = false;
                    }
                    x++;
                    continue;
                }
                if(!blocked && consec >= 1)
                {
                    if(consec > 4)
                    {
                        consec = 4;
                    }
                    if(playerID == 1)
                    {
                        inARowP1[consec - 1]++;
                    }
                    else
                    {
                        inARowP2[consec - 1]++;
                    }
                    continue;
                }
                if(blocked)
                {
                    x--;
                    continue;
                }
            }
        }
    }

    private void parseVertical(final GameStateModule state, int inARowP1[], int inARowP2[], int width, int height)
    {
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height - 3; y++)
            {
                int playerID = state.getAt(x, y);
                if(playerID == 0)
                {
                    continue;
                }
                int consec = 0;
                boolean stillGoing = true;
                boolean blocked = false;
                for(int i = 0; i < 4; i++)
                {
                    if(y >= height)
                    {
                        break;
                    }
                    int coinID = state.getAt(x, y);
                    if(stillGoing && playerID == coinID)
                    {
                        consec++;
                        y++;
                        continue;
                    }
                    if(coinID != 0 && coinID != playerID)
                    {
                        blocked = true;
                        break;
                    }
                    if(coinID == 0)
                    {
                        stillGoing = false;
                    }
                    y++;
                    continue;
                }
                if(!blocked && consec >= 1)
                {
                    if(consec > 4)
                    {
                        consec = 4;
                    }
                    if(playerID == 1)
                    {
                        inARowP1[consec - 1]++;
                    }
                    else
                    {
                        inARowP2[consec - 1]++;
                    }
                    continue;
                }
                if(blocked)
                {
                    y--;
                    continue;
                }
            }
        }
    }

    private void parseDiagonal(final GameStateModule state, int inARowP1[], int inARowP2[], int width, int height)
    {
        //First half
        for(int y = 0; y < height; y++)
        {
            for(int diag = 0; (y + diag < height - 3) && (diag < width - 3); diag++)
            {
                int playerID = state.getAt(diag, y + diag);
                if(playerID == 0)
                {
                    continue;
                }
                int consec = 0;
                boolean stillGoing = true;
                boolean blocked = false;
                for(int i = 0; i < 4; i++)
                {
                    if((y + diag >= height) || (diag >= width))
                    {
                        break;
                    }
                    int coinID = state.getAt(diag, y + diag);
                    if(stillGoing && playerID == coinID)
                    {
                        consec++;
                        diag++;
                        continue;
                    }
                    if(coinID != 0 && coinID != playerID)
                    {
                        blocked = true;
                        break;
                    }
                    if(coinID == 0)
                    {
                        stillGoing = false;
                    }
                    diag++;
                    continue;
                }
                if(!blocked && consec >= 1)
                {
                    if(consec > 4)
                    {
                        consec = 4;
                    }
                    if(playerID == 1)
                    {
                        inARowP1[consec - 1]++;
                    }
                    else
                    {
                        inARowP2[consec - 1]++;
                    }
                    continue;
                }
                if(blocked)
                {
                    diag--;
                    continue;
                }
            }
        }

        //Second half
        for(int x = 1; x < width; x++)
        {
            for(int diag = 0; (diag < height - 3) && (x + diag < width - 3); diag++)
            {
                int playerID = state.getAt(x + diag, diag);
                if(playerID == 0)
                {
                    continue;
                }
                int consec = 0;
                boolean stillGoing = true;
                boolean blocked = false;
                for(int i = 0; i < 4; i++)
                {
                    if((diag >= height) || (x + diag >= width))
                    {
                        break;
                    }
                    int coinID = state.getAt(x + diag, diag);
                    if(stillGoing && playerID == coinID)
                    {
                        consec++;
                        diag++;
                        continue;
                    }
                    if(coinID != 0 && coinID != playerID)
                    {
                        blocked = true;
                        break;
                    }
                    if(coinID == 0)
                    {
                        stillGoing = false;
                    }
                    diag++;
                    continue;
                }
                if(!blocked && consec >= 1)
                {
                    if(consec > 4)
                    {
                        consec = 4;
                    }
                    if(playerID == 1)
                    {
                        inARowP1[consec - 1]++;
                    }
                    else
                    {
                        inARowP2[consec - 1]++;
                    }
                    continue;
                }
                if(blocked)
                {
                    diag--;
                    continue;
                }
            }
        }

    }

    // randomly assigns a value to a state
	private int eval(final GameStateModule state){
        int inARowP1[] = {0, 0, 0, 0};
        int inARowP2[] = {0, 0, 0, 0};
        numInARow(state, inARowP1, inARowP2);
        return(inARowP1[0] + 100 * inARowP1[1] + 10000 * inARowP1[2] + 1000000 * inARowP1[3] - (inARowP2[0] + 100 * inARowP2[1] + 10000 * inARowP2[2] + 1000000 * inARowP2[3]));
	}


}