package edu.miracostacollege.cs134.theescape;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.miracostacollege.cs134.theescape.model.Direction;
import edu.miracostacollege.cs134.theescape.model.Player;
import edu.miracostacollege.cs134.theescape.model.Zombie;

import static edu.miracostacollege.cs134.theescape.model.BoardValues.FREE;
import static edu.miracostacollege.cs134.theescape.model.BoardValues.EXIT;
import static edu.miracostacollege.cs134.theescape.model.BoardValues.OBST;
import static edu.miracostacollege.cs134.theescape.model.Direction.DOWN;
import static edu.miracostacollege.cs134.theescape.model.Direction.LEFT;
import static edu.miracostacollege.cs134.theescape.model.Direction.RIGHT;
import static edu.miracostacollege.cs134.theescape.model.Direction.UP;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private int wins = 0;
    private int losses = 0;

    public static final int TOTAL_ROWS = 8;
    public static final int TOTAL_COLS = 8;

    public static final int PLAYER_ROW = 1;
    public static final int PLAYER_COL = 1;

    public static final int ZOMBIE_ROW = 2;
    public static final int ZOMBIE_COL = 4;

    public static final int EXIT_ROW = 5;
    public static final int EXIT_COL = 7;

    private static final float FLING_THRESHOLD = 500f;

    private LinearLayout boardLinearLayout;
    private TextView winsTextView;
    private TextView lossesTextView;
    private GestureDetector gestureDetector;

    private LinearLayout row;
    private ImageView imageSquare;

    private Player player;
    private Zombie zombie;

    final int gameBoard[][] = {
            {OBST, OBST, OBST, OBST, OBST, OBST, OBST, OBST},
            {OBST, FREE, FREE, FREE, FREE, FREE, OBST, OBST},
            {OBST, FREE, OBST, FREE, FREE, FREE, FREE, OBST},
            {OBST, FREE, OBST, FREE, FREE, FREE, FREE, OBST},
            {OBST, FREE, FREE, FREE, FREE, FREE, OBST, OBST},
            {OBST, FREE, FREE, FREE, FREE, FREE, FREE, EXIT},
            {OBST, FREE, OBST, FREE, FREE, FREE, FREE, OBST},
            {OBST, OBST, OBST, OBST, OBST, OBST, OBST, OBST}
    };

    ImageView viewBoard[][] = new ImageView[TOTAL_ROWS][TOTAL_COLS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boardLinearLayout = findViewById(R.id.boardLinearLayout);
        winsTextView = findViewById(R.id.winsTextView);
        lossesTextView = findViewById(R.id.lossesTextView);

        gestureDetector = new GestureDetector(this, this);


        startNewGame();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }

    private void startNewGame() {
        //done: Loop through the viewBoard and initialize each of the ImageViews
        //done: to the children of the LinearLayouts
        //done: Use the gameBoard to determine which image to assign:
        for (int i =0; i < TOTAL_ROWS; i++) {

            row = (LinearLayout) boardLinearLayout.getChildAt(i);

            for (int j=0; j < TOTAL_COLS; j++) {

                imageSquare = (ImageView) row.getChildAt(j);
                viewBoard[i][j] = imageSquare;

                // Determine what image to place in the row-imageSquare square
                switch (gameBoard[i][j]) {
                    case (OBST) :
                        imageSquare.setImageResource(R.drawable.obstacle);
                        break;
                    case (FREE):
                        imageSquare.setImageDrawable(null);
                        break;
                    case (EXIT):
                        imageSquare.setImageResource(R.drawable.exit);
                        break;
                }

            }

        }

        //done: OBST = R.drawable.obstacle
        //done: EXIT = R.drawable.exit
        //done: FREE = null (no image to load)

        //done: Instantiate a new Player object at PLAYER_ROW, PLAYER_COL
        //done: Set the imageSquare at that position to R.drawable.player
        player = new Player(PLAYER_ROW,PLAYER_COL);
        viewBoard[PLAYER_ROW][PLAYER_COL].setImageResource(R.drawable.male_player);


        //done: Instantiate a new Zombie object at ZOMBIE_ROW, ZOMBIE_COL
        //done: Set the imageSquare at that position to R.drawable.zombie
        zombie = new Zombie(ZOMBIE_ROW, ZOMBIE_COL);
        viewBoard[ZOMBIE_ROW][ZOMBIE_COL].setImageResource(R.drawable.zombie);
    }

    private void movePlayer(float velocityX, float velocityY) {
        //done: Set the player's current image view drawable to null
        viewBoard[player.getRow()][player.getCol()].setImageDrawable(null);

        //done: Determine the direction of the fling (based on velocityX and velocityY)
        Direction direction = RIGHT;
        float absX = Math.abs(velocityX);
        float absY = Math.abs(velocityY);

        if      (velocityX > 0 && absX > absY)
            direction = RIGHT;
        else if (velocityX < 0 && absX > absY)
            direction = LEFT;
        else if (velocityY > 0 && absX < absY)
            direction = DOWN;
        else if (velocityY < 0 && absX < absY)
            direction = UP;

        //done in onFling: The velocity must exceed FLING_THRESHOLD to count (otherwise, it's not really a move)
        //done: Move the player
        player.move(gameBoard, direction);
        //done: Set the player's current image view to R.drawable.player after the move
        viewBoard[player.getRow()][player.getCol()].setImageResource(R.drawable.male_player);
        moveZombie();
        determineOutcome();
    }

    private void moveZombie() {
        //done: Set the zombie's current image view drawable to null
        viewBoard[zombie.getRow()][zombie.getCol()].setImageDrawable(null);
        //done: Move the zombie
        zombie.move(gameBoard,player.getRow(), player.getCol());
        //done: Set the zombie's current image view to R.drawable.zombie after the move
        viewBoard[zombie.getRow()][zombie.getCol()].setImageResource(R.drawable.zombie);
    }

    private void determineOutcome() {
        int playerRow = player.getRow();
        int playerCol = player.getCol();
        int zombieRow = zombie.getRow();
        int zombieCol = zombie.getCol();



        //TODO: Determine the outcome of the game (win or loss)
        //TODO: It's a win if the player's row/imageSquare is the same as the exit row/imageSquare
        //TODO: Call the handleWin() method

        if (gameBoard[playerRow][playerCol] == EXIT)
            handleWin();


        //TODO: It's a loss if the player's row/imageSquare is the same as the zombie's row/imageSquare
        //TODO: Call the handleLoss() method

        else if (playerRow == zombieRow && playerCol == zombieCol)
            handleLoss();



        //TODO: Otherwise, do nothing, just return.

        else return;

    }

    private void handleWin()
    {
        //TODO: Implement the handleWin() method by accomplishing the following:
        //done: Increment the wins
        winsTextView.setText(++wins+"");
        //done: Set the imageSquare (at the zombie's row/imageSquare) to the R.drawable.bunny
        viewBoard[zombie.getRow()][zombie.getCol()].setImageResource(R.drawable.bunny);

        //TODO: Start an animation


        //TODO: Wait 2 seconds, then start a new game
    }

    private void handleLoss()
    {
        //TODO: Implement the handleLoss() method by accomplishing the following:
        //done: Increment the losses
        lossesTextView.setText(--losses+"");
        //done: Set the imageSquare (at the player's row/imageSquare) to the R.drawable.blood
        viewBoard[player.getRow()][player.getCol()].setImageResource(R.drawable.blood);

        //TODO: Start an animation
        //TODO: Wait 2 seconds, then start a new game
    }


    Runnable newGameRunnable = new Runnable() {
        @Override
        public void run() {
            startNewGame();
        }
    };

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(velocityX) < FLING_THRESHOLD && Math.abs(velocityY) < FLING_THRESHOLD)
        {
            return false;
        }

        movePlayer(velocityX, velocityY);
        return true;
    }
}
