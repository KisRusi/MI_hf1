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

        return minimax(board,3,-1000000000, 100000000,2);
    }

    public int minimax(Board board, int depth,int alpha, int beta, int player)
    {
        if(depth == 0 || board.gameEnded())
        {
            return evalScore(board,2);
        }
        ArrayList<Integer> validsteps;
        validsteps = board.getValidSteps();
        if(player==2)
        {
            int maxEval = -100000000;
            for(Integer i : validsteps)
            {
                int row = nextRow(board,i);
                Board temp =new  Board(board);
                tryMove(temp,row,i,2);
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
                int row = nextRow(board, i);
                Board temp = new Board(board);
                tryMove(temp, row, i,1);
                int eval = minimax(board,depth - 1, alpha, beta, 2);
                minEval = Math.min(minEval,eval);
                beta = Math.min(beta, eval);
                if(beta <= alpha)
                    break;
            }
            return minEval;
        }
    }

    int evalWindow(int[] window,int playerState)
    {
        int score = 0;
        if(Arrays.stream(window).filter(c->c ==2).count()==4)
            score += 100;
        else if(Arrays.stream(window).filter(c-> c ==2).count()==3 && Arrays.stream(window).filter(c -> c==0).count()==1)
            score += 5;
        else if(Arrays.stream(window).filter(c-> c ==2).count()==2 && Arrays.stream(window).filter(c -> c==0).count()==2)
            score +=2;
        if(Arrays.stream(window).filter(c-> c ==1).count()==3 && Arrays.stream(window).filter(c -> c==0).count()==1)
            score -= 80;
        return score;
    }
    int evalScore(Board board, int playerState)
    {
        int[][] boardstate = board.getState();
        int[] row = new int[7];
        int[] col = new int[6];
        int[] center = new int[6];
        int[] window= new int[4];
        double floordiv = 7/2;
        int score = 0;

        for (int i = 0; i<6; i++)
        {
            center[i] = boardstate[i][Math.floorDiv(7,2)];
            score += (Arrays.stream(center).filter(c -> c == 2).count()) * 3;
        }


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
               score += evalWindow(window,playerState);

           }
       }
       for(int k = 0; k < 7; k++)
       {
            for(int i = 0; i<6; i++)
            {
                col[i] = boardstate[i][k];
            }
            for(int b = 0; b<6-3; b++)
            {
                for(int a = 0; a<4; a++)
                {
                    window[a] = col[a+b];
                }
                score += evalWindow(window,playerState);
            }
       }

        for(int k = 0; k<7-3; k++)
        {
            for(int i = 0; i<6-3; i++)
            {
                for(int a = 0; a<4; a++)
                {
                    window[a] = boardstate[i+a][k+a];
                }
                score += evalWindow(window,playerState);
            }
        }

        for(int k = 0; k<7-3; k++)
        {
            for(int i = 0; i<6-3; i++)
            {
                for(int a = 0; a<4; a++)
                {
                    window[a] = boardstate[i+a][k+3-a];
                }
                score += evalWindow(window,playerState);
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
            int row = nextRow(board,col);
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

    int nextRow(Board board, int col)
    {
        for(int i = 5; i>-1;i--)
        {
            if(board.getState()[i][col]==0)
                return i;
        }
            return 0;
    }
    void tryMove(Board board, int row, int col, int playerState)
    {
        board.getState()[row][col] = playerState;
    }
}


