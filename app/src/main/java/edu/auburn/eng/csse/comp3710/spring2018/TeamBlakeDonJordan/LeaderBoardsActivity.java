package edu.auburn.eng.csse.comp3710.spring2018.TeamBlakeDonJordan;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;



/** Leaderboards
 *
 * The Data Adapter for Storing Scores
 *
 * Created by Jordan, Don, and Blake on 4/23/2018.
 *
 */

public class LeaderBoardsActivity extends Activity {
    private static final String DEBUG_TAG = "SimpleDB Log";

    private ScoreboardDBHelper dbHelper;

    // Our database instance
    private SQLiteDatabase mDatabase;


    protected Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboards);
        mContext = (Context) getBaseContext();
        dbHelper = new ScoreboardDBHelper(mContext);
        if(savedInstanceState != null) { }
        Log.d(DEBUG_TAG, "onCreate()");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {      //saves mBoard on state destruction
        super.onSaveInstanceState(outState);
    }


}