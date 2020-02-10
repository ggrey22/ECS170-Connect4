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

    private void numInARow(final GameStateModule state, int inARow[])
    {
        int x = state.getWidth();
        int y = state.getHeight();
        parseHorizontal(state, inARow, x, y);
        parseVertical(state, inARow, x, y);
        parseDiagonal(state, inARow, x, y);
    }

    private void parseHorizontal(final GameStateModule state, int inARow[], int width, int height)
    {
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                if(state.getAt(x, y) == player)
                {
                    int consec = 0;
                    while(x < width)
                    {
                        if(state.getAt(x, y) == player)
                        {
                            consec++;
                            x++;
                        }
                        else
                        {
                            break;
                        }
                    }
                    if(consec >= 2)
                    {
                        if(consec > 4)
                        {
                            consec = 4;
                        }
                        inARow[consec - 2]++;
                    }
                }
            }
        }
    }

    private void parseVertical(final GameStateModule state, int inARow[], int width, int height)
    {
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < width; y++)
            {
                if(state.getAt(x, y) == player)
                {
                    int consec = 0;
                    while(y < height)
                    {
                        if(state.getAt(x, y) == player)
                        {
                            consec++;
                            y++;
                        }
                        else
                        {
                            break;
                        }
                    }
                    if(consec >= 2)
                    {
                        if(consec > 4)
                        {
                            consec = 4;
                        }
                        inARow[consec - 2]++;
                    }
                }
            }
        }
    }

    private void parseDiagonal(final GameStateModule state, int inARow[], int width, int height)
    {
        //First half
        for(int y = 0; y < height; y++)
        {
            for(int diag = 0; y+diag < height && diag < width; diag++)
            {
                if(state.getAt(diag, y + diag) != player)
                {
                    continue;
                }
                int consec = 0;
                while(y+diag < height && diag < width)
                {
                    if(state.getAt(diag, y + diag) != player)
                    {
                        break;
                    }
                    consec++;
                    diag++;
                }
                if(consec >= 2)
                {
                    if(consec > 4)
                    {
                        consec = 4;
                    }
                    inARow[consec - 2]++;
                }
            }
        }

        //Second half
        for(int x = 1; x < width; x++)
        {
            for(int diag = 0; x+diag < width && diag < height; diag++)
            {
                if(state.getAt(x + diag, diag) != player)
                {
                    continue;
                }
                int consec = 0;
                while(x+diag < width && diag < height)
                {
                    if(state.getAt(x + diag, diag) != player)
                    {
                        break;
                    }
                    consec++;
                    diag++;
                }
                if(consec >= 2)
                {
                    if(consec > 4)
                    {
                        consec = 4;
                    }
                    inARow[consec - 2]++;
                }
            }
        }
    }

    // randomly assigns a value to a state
	private int eval(final GameStateModule state){
        int inARow[] = {0, 0, 0};
        numInARow(state, inARow);
        return(10 * inARow[0] + 100 * inARow[1] + 1000 * inARow[2]);
	}


}