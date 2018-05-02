package edu.auburn.eng.csse.comp3710.spring2018.TeamBlakeDonJordan;



/**
 * Created by donaldtran on 4/29/18.
 * A simple model for a User
 * Since it's being used in multiple locations, We're creating a root model
 */

// Helper class to encapsulate User information programmatically
public class User {
    private String mUsername;
    private long mDateAdded;
    private long mId;
    private int mScore;

    public User(String username, long dateAdded, int score) {
        mUsername = username;
        mDateAdded = dateAdded;
        mScore = score;
        mId = -1;
    }

    // GETTERS

    public String getUserName() {
        return this.mUsername;
    }

    public long getDateUserAdded() {
        return this.mDateAdded;
    }

    public long getID() {
        return this.mId;
    }

    public int getScore() {
        return this.mScore;
    }

    // SETTERS

    public void setScore(int score) {
        this.mScore = score;
    }

    public void setUserName(String name) {
        this.mUsername = name;
    }

    public void setDateUserAdded(long date) {
        this.mDateAdded = date;
    }

    public void setID(long id) {
        this.mId = id;
    }


}