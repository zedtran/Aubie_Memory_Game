package edu.auburn.eng.csse.comp3710.spring2018.TeamBlakeDonJordan;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.app.AlertDialog;
import android.widget.EditText;
import android.text.InputType;
import android.content.DialogInterface;
import android.text.InputFilter;
import java.util.List;
import java.util.Formatter;
import java.util.Locale;

/**
 * AubieFragment
 * main fragment of the app. Runs most of the code here. Essentially causes the game to work
 *
 * Created by Jordan, Don, and Blake on 4/12/2018.
 */

public class AubieFragment extends Fragment {

    private Board mBoard;
    private boolean mGameOver = false;
    private TextView mScoreBoard;
    private static final String KEY_BOARD = "Board";
    private int replayCount = 0;
    private View v;
    private SoundPool sp = new SoundPool.Builder().build(); //(5, AudioManager.STREAM_MUSIC, 0);
    private ScoreboardDBHelper dbHelper;
    private Toast mToast;
    protected String mAlertInputTypeText; // To be used for AlertDialogs during GameOver
    private static final CharSequence GAME_OVER_MSG = "Try again. You didn't score high enough to place on the leader board.";
    private static final CharSequence SCOREBOARD_SUCCESS_MSG = "Congratulations! You made it to the leader board! Please enter your name.";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){                     //restores mBoard after state destroyed
            mBoard = savedInstanceState.getParcelable(KEY_BOARD);

        }

        // Our SQL Database Access Helper
        dbHelper = new ScoreboardDBHelper(getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {      //saves mBoard on state destruction
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_BOARD, mBoard);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_aubieboard, container, false);

        //creates all views
        mScoreBoard = v.findViewById(R.id.score);
        Bundle bundle = getArguments();
        Button mResetButton =  v.findViewById(R.id.reset);
        Button mReplayButton = v.findViewById(R.id.replay);
        TextView gameType = v.findViewById(R.id.gameType);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean mPlayOriginal = true;
        String difficulty = sharedPrefs.getString("pref_difficulty", "Easy");

        if (bundle != null) mPlayOriginal = bundle.getBoolean("PLAY_ORIGINAL"); //sets play original value

        if(mPlayOriginal)  gameType.setText(getString(R.string.simonTitle));   //decided to play simon or aubie game
        else  gameType.setText(getString(R.string.aubieTitle));

        if(savedInstanceState == null){
            mBoard = new Board(mPlayOriginal, v, difficulty, getContext()); //difficulty will be chosen through preferences Easy/Normal/Hard/Extreme
        }
        else  mBoard.updateBoard(v);


        /* soundId for Later handling of sound pool */
        final int redSound = sp.load(getContext(), R.raw.anote_red, 1); // in 2nd param u have to pass your desire ringtone
        final int blueSound = sp.load(getContext(), R.raw.enote_blue, 1); // in 2nd param u have to pass your desire ringtone
        final int yellowSound = sp.load(getContext(), R.raw.csharpnote_yellow, 1); // in 2nd param u have to pass your desire ringtone
        final int greenSound = sp.load(getContext(), R.raw.enote_green, 1); // in 2nd param u have to pass your desire ringtone
        final int orangeSound = sp.load(getContext(), R.raw.fnote_orange, 1); // in 2nd param u have to pass your desire ringtone

        //sets up listeners for buttons
        mBoard.getRedLight().getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {                               //Red
                //mRedLight.clearAnimation();
                click(getString(R.string.RED), redSound);
            }
        });
        mBoard.getBlueLight().getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {                              //Blue
                //mBlueLight.clearAnimation();
                click(getString(R.string.BLUE), blueSound);
            }
        });
        mBoard.getYellowLight().getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {                            //Yellow
                //mYellowLight.clearAnimation();
                click(getString(R.string.YELLOW), yellowSound);
            }
        });
        mBoard.getGreenLight().getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {                             //Green
                //mGreenLight.clearAnimation();
                click(getString(R.string.GREEN), greenSound);
            }
        });
        mBoard.getOrangeLight().getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {                            //Orange
                //mOrangeLight.clearAnimation();
                click(getString(R.string.ORANGE), orangeSound);
            }
        });
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                        //Reset
                v.findViewById(R.id.board).setVisibility(View.VISIBLE);
                mGameOver = mBoard.reset(); //sets gameOver to false
                v.findViewById(R.id.gameOverScreen).setVisibility(View.INVISIBLE);  //hides game over screen
                v.findViewById(R.id.replay).setVisibility(View.VISIBLE);        //sets replay to visible
            }
        });
        mReplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                        //Replay
                   mBoard.playAnimation();
                   mBoard.resetInputCount();
                   replayCount++;
            }
        });
        return v;
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /* click(color, mScoreBoard)
     * deals with player clicks. sets new sequence (will need to be removed eventually, sets game
     * over and sets score
     */
    public void click(String color, int sound) {
        if(!mBoard.isAnimatorRunning()) {
            if (!mGameOver) {
                mGameOver = mBoard.checkInput(color);
                if (!mGameOver) sp.play(sound, 1, 1, 0, 0, 1);
            }
            if (mGameOver) {
                v.findViewById(R.id.gameOverScreen).setVisibility(View.VISIBLE); //sets game over screen to visible
                v.findViewById(R.id.board).setVisibility(View.INVISIBLE);        //sets board to invisible
                v.findViewById(R.id.replay).setVisibility(View.INVISIBLE);       //sets replay to invisible
                mScoreBoard.setText(setScore());

                ///////////////////////////////////////////////////////////
                // Returning Current Top Ten High Scores as an ArrayList //
                ///////////////////////////////////////////////////////////
                List<User> highScoreUserList = dbHelper.getTopTenUsers();
                User userPrevious;
                User userNext;
                int tempHighScoreIndex = -1;
                final int gameOverScore = getFinalScore();

                /*
                 * Traverse the High Scores. If player scored high enough,
                 * find out where they placed.
                 */

                /////////////////////////////////////////////
                // If the leader board is currently empty, //
                // the player made first place             //
                /////////////////////////////////////////////
                if (highScoreUserList.size() == 0) {
                    tempHighScoreIndex = 0;
                }
                //////////////////////////////////////////////
                // If the leader board has just one entry   //
                // compare the game over score with that    //
                // single leader board entry                //
                //////////////////////////////////////////////
                else if (highScoreUserList.size() == 1) {
                    userNext = highScoreUserList.get(0);
                    if (gameOverScore > userNext.getScore()) {
                        tempHighScoreIndex = 1;
                    }
                    else {
                        tempHighScoreIndex = 0;
                    }
                }
                // If the leader board has at least two entries
                else {
                    for (int i = 1; i < highScoreUserList.size(); i++) {
                        userPrevious = highScoreUserList.get(i - 1);
                        userNext = highScoreUserList.get(i);
                        if (gameOverScore >= userPrevious.getScore()) {
                            if (gameOverScore < userNext.getScore()) {
                                tempHighScoreIndex = i - 1;
                                break;
                            }
                            else if (gameOverScore >= userNext.getScore()) {
                                tempHighScoreIndex = i;
                            }
                        }
                    }
                }
                showGameOverAlert(tempHighScoreIndex, highScoreUserList);
            }
        }
    }

    public String setScore(){
        int finalScore = getFinalScore();
        StringBuilder builder = new StringBuilder();
        Formatter formatter = new Formatter(builder, Locale.US);
        formatter.format("Final Score: %1$2d" +
                "\nLongest Sequence: %2$2d" +
                "\nNumber of Replays: %3$2d" +
                "\nDifficulty: %4$s",finalScore, mBoard.getScore(), replayCount, mBoard.getDifficulty());
        return formatter.toString();
    }

    public int getFinalScore() {
        int longestSequence = mBoard.getScore();
        return (longestSequence - replayCount) * mBoard.getDifficultyModifier();
    }

    /* Simple toast message function */
    private void makeToast(CharSequence message) {
        if(mToast != null) {
            mToast.cancel();
        }
        Context context = getContext();
        int duration = Toast.LENGTH_SHORT;
        mToast = Toast.makeText(context, message, duration);
        mToast.show();
    }

    // Returns the String superscript of an integer value with range 1 through 10
    private String getNumSuffix(int num) {
        String superScript = null;
        switch (num) {
            case 1 :
                superScript = "st";
                break;
            case 2 :
                superScript = "nd";
                break;
            case 3 :
                superScript = "rd";
                break;
            case 4 :
            case 5 :
            case 6 :
            case 7 :
            case 8 :
            case 9 :
            case 10 :
                superScript = "th";
                break;
            default :
                break;
        }
        return superScript;
    }

    private void showGameOverAlert(final int updateIndex, final List<User> highScoreUserList) {

        // Final values, variables & Inner class usage vars
        //final int positionNum = updateIndex;
        //final String positionSuffix = getNumSuffix(positionNum);
        final String alertMessageTitle;
        final CharSequence toastMsg;  // Sets up toast alert
        final EditText input;         // Sets up the alert input
        AlertDialog alertDialog;      // Initialize simple alert for Game Over alert
        AlertDialog.Builder builder;  // Initialize alert builder for Game Over + New Leaderboard entry
        final int MAX_LENGTH = 6;    // Limiting Name input field to 10 Characters
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(MAX_LENGTH);


        /////////////////////////////////////////////////////////////////////////
        // If the user didn't get a high enough score to make the scoreboard,  //
        // Show a simple alert message indicating game over                    //
        /////////////////////////////////////////////////////////////////////////
        if (updateIndex == -1 && highScoreUserList.size() > 9) {
            toastMsg = "Check Leader board from Main Menu.";
            alertMessageTitle = "GAME OVER";

            // Make Alert
            alertDialog = new AlertDialog.Builder(getActivity()).create(); // Game Over Alert
            alertDialog.setTitle(alertMessageTitle);
            alertDialog.setMessage(GAME_OVER_MSG);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            makeToast(toastMsg);
                        }
                    });
            alertDialog.show();
        }
        ////////////////////////////////////////////
        // Add the User Score to the Leader Board //
        ////////////////////////////////////////////
        else {
            toastMsg = "Updated! Check Leader board from Main Menu.";
            alertMessageTitle = "CONGRATULATIONS";

            // Alert Dialog for inputText name and congrats on placement on scoreboard
            builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(alertMessageTitle);
            builder.setMessage(SCOREBOARD_SUCCESS_MSG);

            // Specify the type of input expected
            input = new EditText(getActivity());
            input.setFilters(FilterArray);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAlertInputTypeText = input.getText().toString();
                    User newUser = new User(mAlertInputTypeText, getFinalScore(), System.currentTimeMillis());
                    dbHelper.addUserScore(newUser);
                    makeToast(toastMsg);
                }
            });
            builder.show();
        }
    }

}
