import java.util.*;

public class minimax2 extends AIModule
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
        System.out.println(eval(game, true));
	}

	private int minimax(final GameStateModule state, int depth, int playerID) {
        if (terminate)
            return 0;
        if (depth == maxDepth) {
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
                boolean unblocked = true;
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
                        int firstx = x - i;
                        int remaining = 4 - consec;
                        for(int j = 1; j <= remaining; j++)
                        {
                            if(firstx - j < 0)
                            {
                                unblocked = false;
                                break;
                            }
                            coinID = state.getAt(firstx - j, y);
                            if(coinID != playerID)
                            {
                                unblocked = false;
                                break;
                            }
                        }
                        break;
                    }
                    if(coinID == 0)
                    {
                        stillGoing = false;
                    }
                    x++;
                    continue;
                }
                if((!blocked || (blocked && unblocked)) && consec >= 1)
                {
                    if(consec > 4)
                    {
                        consec = 4;
                    }
                    if(playerID == player)
                    {
                        inARowMe[consec - 1]++;
                    }
                    else
                    {
                        inARowOpponent[consec - 1]++;
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

    private void parseHorizontalR2L(final GameStateModule state, int inARowMe[], int inARowOpponent[], int width, int height)
    {
        for(int y = 0; y < height; y++)
        {
            for(int x = width - 1; x >= 3; x--)
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
                    if(x < 0)
                    {
                        break;
                    }
                    int coinID = state.getAt(x, y);
                    if(stillGoing && playerID == coinID)
                    {
                        consec++;
                        x--;
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
                    x--;
                }
                if(!blocked && consec >= 1)
                {
                    if(consec > 4)
                    {
                        consec = 4;
                    }
                    if(playerID == player)
                    {
                        inARowMe[consec - 1]++;
                    }
                    else
                    {
                        inARowOpponent[consec - 1]++;
                    }
                    continue;
                }
                if(blocked)
                {
                    x++;
                    continue;
                }
            }
        }
    }

    private void parseVertical(final GameStateModule state, int inARowMe[], int inARowOpponent[], int width, int height)
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
                    if(playerID == player)
                    {
                        inARowMe[consec - 1]++;
                    }
                    else
                    {
                        inARowOpponent[consec - 1]++;
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
    
    private void parseVerticalT2B(final GameStateModule state, int inARowMe[], int inARowOpponent[], int width, int height)
    {
        for(int x = 0; x < width; x++)
        {
            for(int y = height - 1; y >= 3; y--)
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
                    if(y < 0)
                    {
                        break;
                    }
                    int coinID = state.getAt(x, y);
                    if(stillGoing && playerID == coinID)
                    {
                        consec++;
                        y--;
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
                    y--;
                    continue;
                }
                if(!blocked && consec >= 1)
                {
                    if(consec > 4)
                    {
                        consec = 4;
                    }
                    if(playerID == player)
                    {
                        inARowMe[consec - 1]++;
                    }
                    else
                    {
                        inARowOpponent[consec - 1]++;
                    }
                    continue;
                }
                if(blocked)
                {
                    y++;
                    continue;
                }
            }
        }
    }

    private void parseDiagonalBotL2TopRFromXY(final GameStateModule state, int inARowMe[], int inARowOpponent[], int width, int height, int x, int y)
    {
        for(int diag = 0; y + diag < height - 3 && x + diag < width - 3; diag++)
        {
            int playerID = state.getAt(x + diag, y + diag);
            if(playerID == 0)
            {
                continue;
            }
            int consec = 0;
            boolean stillGoing = true;
            boolean blocked = false;
            boolean unblocked = true;
            for(int i = 0; i < 4; i++)
            {
                if((y + diag >= height) || (x + diag >= width))
                {
                    break;
                }
                int coinID = state.getAt(x + diag, y + diag);
                if(stillGoing && playerID == coinID)
                {
                    consec++;
                    diag++;
                    continue;
                }
                if(coinID != 0 && coinID != playerID)
                {
                    blocked = true;
                    int firstdiag = diag - i;
                    int remaining = 4 - consec;
                    for(int j = 1; j <= remaining; j++)
                    {
                        if(x - firstdiag - j < 0 || y - firstdiag - j < 0)
                        {
                            unblocked = false;
                            break;
                        }
                        coinID = state.getAt(x - firstdiag - j, y - firstdiag - j);
                        if(coinID != playerID)
                        {
                            unblocked = false;
                            break;
                        }
                    }
                    break;
                }
                if(coinID == 0)
                {
                    stillGoing = false;
                }
                diag++;
                continue;
            }
            if((!blocked || (blocked && unblocked)) && consec >= 1)
            {
                if(consec > 4)
                {
                    consec = 4;
                }
                if(playerID == player)
                {
                    inARowMe[consec - 1]++;
                }
                else
                {
                    inARowOpponent[consec - 1]++;
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

    private void parseDiagonalBotL2TopR(final GameStateModule state, int inARowMe[], int inARowOpponent[], int width, int height)
    {
        //First half
        for(int y = 0; y < height; y++)
        {
            parseDiagonalBotL2TopRFromXY(state, inARowMe, inARowOpponent, width, height, 0, y);
        }

        //Second half
        for(int x = 1; x < width; x++)
        {
            parseDiagonalBotL2TopRFromXY(state, inARowMe, inARowOpponent, width, height, x, 0);
        }

    }

    private void parseDiagonalTopR2BotL(final GameStateModule state, int inARowMe[], int inARowOpponent[], int width, int height)
    {
        //First half
        for(int y = height - 1; y >= 0; y--)
        {
            for(int diag = 0; (y - diag >= 3 && diag < width - 3); diag++)
            {
                int playerID = state.getAt(width - 1 - diag, y - diag);
                if(playerID == 0)
                {
                    continue;
                }
                int consec = 0;
                boolean stillGoing = true;
                boolean blocked = false;
                for(int i = 0; i < 4; i++)
                {
                    if(y - diag < 0 || diag >= width)
                    {
                        break;
                    }
                    int coinID = state.getAt(width - 1 - diag, y - diag);
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
                    if(playerID == player)
                    {
                        inARowMe[consec - 1]++;
                    }
                    else
                    {
                        inARowOpponent[consec - 1]++;
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
        for(int x = width - 2; x >= 0; x--)
        {
            for(int diag = 0; x - diag >= 3 && diag < height; diag++)
            {
                int playerID = state.getAt(x - diag, height - 1 - diag);
                if(playerID == 0)
                {
                    continue;
                }
                int consec = 0;
                boolean stillGoing = true;
                boolean blocked = false;
                for(int i = 0; i < 4; i++)
                {
                    if((diag >= height) || (x - diag < 0))
                    {
                        break;
                    }
                    int coinID = state.getAt(x - diag, height - 1 - diag);
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
                    if(playerID == player)
                    {
                        inARowMe[consec - 1]++;
                    }
                    else
                    {
                        inARowOpponent[consec - 1]++;
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

    private void parseDiagonalBotR2TopLFromXY(final GameStateModule state, int inARowMe[], int inARowOpponent[], int width, int height, int x, int y)
    {
        for(int diag = 0; y + diag < height - 3 && x - diag >= 3; diag++)
        {
            int playerID = state.getAt(x - diag, y + diag);
            if(playerID == 0)
            {
                continue;
            }
            int consec = 0;
            boolean stillGoing = true;
            boolean blocked = false;
            boolean unblocked = true;
            for(int i = 0; i < 4; i++)
            {
                if((y + diag >= height) || (x - diag < 0))
                {
                    break;
                }
                int coinID = state.getAt(x - diag, y + diag);
                if(stillGoing && playerID == coinID)
                {
                    consec++;
                    diag++;
                    continue;
                }
                if(coinID != 0 && coinID != playerID)
                {
                    blocked = true;
                    int firstdiag = diag - i;
                    int remaining = 4 - consec;
                    for(int j = 1; j <= remaining; j++)
                    {
                        if(x - firstdiag + j >= width || y + firstdiag - j < 0)
                        {
                            unblocked = false;
                            break;
                        }
                        coinID = state.getAt(x - firstdiag + j, y + firstdiag - j);
                        if(coinID != playerID)
                        {
                            unblocked = false;
                            break;
                        }
                    }
                    break;
                }
                if(coinID == 0)
                {
                    stillGoing = false;
                }
                diag++;
                continue;
            }
            if((!blocked || (blocked && unblocked)) && consec >= 1)
            {
                if(consec > 4)
                {
                    consec = 4;
                }
                if(playerID == player)
                {
                    inARowMe[consec - 1]++;
                }
                else
                {
                    inARowOpponent[consec - 1]++;
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

    private void parseDiagonalTopL2BotR(final GameStateModule state, int inARowMe[], int inARowOpponent[], int width, int height)
    {
        //First half
        for(int y = height - 1; y >= 0; y--)
        {
            for(int diag = 0; (y - diag >= 3) && (diag < width - 3); diag++)
            {
                int playerID = state.getAt(diag, y - diag);
                if(playerID == 0)
                {
                    continue;
                }
                int consec = 0;
                boolean stillGoing = true;
                boolean blocked = false;
                for(int i = 0; i < 4; i++)
                {
                    if((y - diag < 0) || (diag >= width))
                    {
                        break;
                    }
                    int coinID = state.getAt(diag, y - diag);
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
                    if(playerID == player)
                    {
                        inARowMe[consec - 1]++;
                    }
                    else
                    {
                        inARowOpponent[consec - 1]++;
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
        for(int x = 1; x < width - 1; x++)
        {
            for(int diag = 0; ((height - 1) - diag > 3) && (x + diag < width - 3); diag++)
            {
                int playerID = state.getAt(x + diag, height - 1 - diag);
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
                    int coinID = state.getAt(x + diag, height - 1 -diag);
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
                    if(playerID == player)
                    {
                        inARowMe[consec - 1]++;
                    }
                    else
                    {
                        inARowOpponent[consec - 1]++;
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

    private void parseDiagonalBotR2TopL(final GameStateModule state, int inARowMe[], int inARowOpponent[], int width, int height)
    {
        //First half
        for(int y = 0; y < height - 3; y++)
        {
            parseDiagonalBotR2TopLFromXY(state, inARowMe, inARowOpponent, width, height, width - 1, y);
        }

        //Second half
        for(int x = width - 2; x >= 0; x--)
        {
            parseDiagonalBotR2TopLFromXY(state, inARowMe, inARowOpponent, width, height, x, 0);
        }
    }


    // randomly assigns a value to a state
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
        return(100 * inARowMe[1] + 10000 * inARowMe[2] + 100000000 * inARowMe[3] - (100 * inARowOpponent[1] + 10000 * inARowOpponent[2] + 100000000 * inARowOpponent[3]));
    }
}