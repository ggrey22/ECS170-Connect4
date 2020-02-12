import java.util.*;

public class minimax2 extends AIModule
{
	int player;
	int opponent;
    int maxDepth = 6;
    int numAtDepth = 0;
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
        System.out.println(eval(game, true));
	}

	private int minimax(final GameStateModule state, int depth, int playerID) {
        if (terminate)
            return 0;
        if (depth == maxDepth) {
            numAtDepth++;
            return eval(state, false);
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
            for(int y = 0; y < height - 3; y++)
            {
                int initialOpponent = inARowOpponent[2];
                parseHorizontalFromXY(state, inARowMe, inARowOpponent, x, y);
                if(inARowOpponent[2] != initialOpponent && state.getAt(x, y) == 0)
                {
                    break;
                }
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
                int initialOpponent = inARowOpponent[2];
                parseVerticalFromXY(state, inARowMe, inARowOpponent, x, y);
                if(inARowOpponent[2] != initialOpponent && state.getAt(x, y) == 0)
                {
                    break;
                }
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
                int initialOpponent = inARowOpponent[2];
                parseDiagonalBotL2TopRFromXY(state, inARowMe, inARowOpponent, x, y);
                if(inARowOpponent[2] != initialOpponent && state.getAt(x, y) == 0)
                {
                    break;
                }
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
                int initialOpponent = inARowOpponent[2];
                parseDiagonalBotR2TopLFromXY(state, inARowMe, inARowOpponent, x, y);
                if(inARowOpponent[2] != initialOpponent && state.getAt(x, y) == 0)
                {
                    break;
                }
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

	private int eval(final GameStateModule state, boolean print){
        int inARowMe[] = {0, 0, 0, 0};
        int inARowOpponent[] = {0, 0, 0, 0};
        numInARow(state, inARowMe, inARowOpponent);
        if(print){
            for(int i = 0; i < 4; i++){
                System.out.print(inARowMe[i] + " ");
            }
            for(int i = 0; i < 4; i++){
                System.out.print(inARowOpponent[i] + " ");
            }
        }
        if(inARowOpponent[3] >= 1)
        {
            return -10000000;
        }
        else if(inARowMe[3] >= 1)
        {
            return 10000000;
        }
        else{
            return(inARowMe[0] + 100 * inARowMe[1] + 10000 * inARowMe[2] - (inARowOpponent[0] + 100 * inARowOpponent[1] + 10000 * inARowOpponent[2]));
        }
    }
}