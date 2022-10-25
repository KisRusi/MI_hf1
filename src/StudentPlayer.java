import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class StudentPlayer extends Player{
    public StudentPlayer(int playerIndex, int[] boardSize, int nToConnect) {
        super(playerIndex, boardSize, nToConnect);
    }

    @Override
    public int step(Board board)
    {

        return pickMove(board,this.playerIndex);
    }

    public int minimax(Board board, int depth,int alpha, int beta, int player)
    {
        if(depth == 0 || board.gameEnded())
        {
            return heuristic(board.getState());
        }
        ArrayList<Integer> validsteps;
        validsteps = board.getValidSteps();
        if(player==2)
        {
            int maxEval = -100000000;
            for(Integer i : validsteps)
            {
                int eval = minimax(board, depth-1, alpha, beta,1);
                maxEval = Math.max(maxEval,eval);
                alpha = Math.max(alpha,eval);
                if(beta <= alpha)
                    break;
            }
            return maxEval;

        }
        else
        {
            int minEval = 100000000;
            for(int i : validsteps)
            {
                int eval = minimax(board,depth - 1, alpha, beta, 2);
                minEval = Math.min(minEval,eval);
                beta = Math.min(beta, eval);
                if(beta <= alpha)
                    break;
            }
            return minEval;
        }
    }


    int heuristic(int[][] s) {
        int result = 0;
        int i, j;

        //check horizontals
        for(i=0; i<6; i++)
            for(j=0; j<=7-4; j++){
                if(s[i][j]!= 2 && s[i][j+1]!= 2 && s[i][j+2]!= 2 && s[i][j+3]!= 2)
                    result++;
                if(s[i][j]!= 1 && s[i][j+1]!= 1 && s[i][j+2]!= 1 && s[i][j+3]!= 1)
                    result--;
            }

        //check verticals
        for(i=0; i<=6-4; i++)
            for(j=0; j<7; j++){
                if(s[i][j]!= 2 && s[i+1][j]!= 2 && s[i+2][j]!= 2 && s[i+3][j]!= 2 )
                    result++;
                if(s[i][j]!= 1 && s[i+1][j]!= 1 && s[i+2][j]!= 1 && s[i+3][j]!= 1 )
                    result--;
            }

        return result;
    }



    int evalScore(Board board, int playerState)
    {
        int[][] boardstate = board.getState();
        int[] row = new int[7];
        int[] window= new int[4];
        int score = 0;

       for(int k = 0; k <6; k++)
       {
           for(int i =0; i<7; i++)
           {
               row[i] = boardstate[k][i];
           }
           for(int b = 0; b<7-3; b++)
           {
               for (int a = 0; a<4; a++)
               {
                   window[a] = row[b+a];
               }
               if(Arrays.stream(window).filter(c-> c==1 || c ==2).count()==4)
                   score += 100;
               else if (Arrays.stream(window).filter(c-> c==1 || c ==2).count()==3 && Arrays.stream(window).filter(c-> c==0).count()==1)
                   score+= 10;

           }
       }



        return score;
    }

    int pickMove(Board board, int playerState)
    {
        int bestScore = -10000;
        int score = 0;
        ArrayList<Integer> validsteps= board.getValidSteps();
        Random rand = new Random();
        int bestCol = validsteps.get(rand.nextInt(validsteps.size()));
        for(int col = 0; col<validsteps.size();col++)
        {
            int row = nextOpenRow(board,col);
            Board temp = new Board(board);
            tryMove(temp,row,col,playerState);
            score =evalScore(temp,playerState);
            if(score > bestScore)
            {
                bestScore = score;
                bestCol = col;
            }
        }
        return bestCol;
    }

    int nextOpenRow(Board board, int col)
    {
        for(int i = 5; i>-1;i--)
        {
            if(board.getState()[i][col]==0)
                return i;
        }
            throw new RuntimeException();
    }
    void tryMove(Board board, int row, int col, int playerState)
    {
        board.getState()[row][col] = playerState;
    }
}


