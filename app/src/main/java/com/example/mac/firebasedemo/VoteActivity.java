package com.example.mac.firebasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class VoteActivity extends AppCompatActivity {
    private static final String voteTAG = "VoteActivity";
    private Button song1Button, song2Button, song3Button;
    private EditText song1name, song2name, song3name;
    private FirebaseAuth mAuth;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference sFirebaseDatabaseReference =
            FirebaseDatabase.getInstance().getReference();
    public static final String SONGS_CHILD = "songs";
    final DatabaseReference songRef = sFirebaseDatabaseReference.child(SONGS_CHILD);
    private static boolean voted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(voteTAG, "starting voting activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        //Initializing our vote buttons
        song1Button = (Button) findViewById(R.id.song1Vote);
        song2Button = (Button) findViewById(R.id.song2Vote);
        song3Button = (Button) findViewById(R.id.song3Vote);
        song1name = (EditText) findViewById(R.id.song1name);
        song2name = (EditText) findViewById(R.id.song2name);
        song3name = (EditText) findViewById(R.id.song3name);

        //Adding an event listener to our database reference
        songRef.addListenerForSingleValueEvent(new ValueEventListener() {
            //Allowing ourselves to get updated information via the database snapshot
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = "";
                String song1 = "",song2 = "",song3 = "";
                for (DataSnapshot songSnap : dataSnapshot.getChildren()) {
                    key = songSnap.getKey();
                    //Getting the song names
                    song1 = songSnap.child("song1").getValue(String.class);
                    song2 = songSnap.child("song2").getValue(String.class);
                    song3 = songSnap.child("song3").getValue(String.class);
                    //Setting the song names
                    song1name.setText(song1);
                    song2name.setText(song2);
                    song3name.setText(song3);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        // set click listeners
        song1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vote1(songRef);
            }
        });
        song2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vote2(songRef);
            }
        });
        song3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vote3(songRef);
            }
        });

    }

    //Check to see if voted
    public static void voted() {
        voted = true;
    }

    //Check to see if refreshed vote
    public static void refreshVote() {
        voted = false;
    }

    //Checking to see if we have voted
    //so that we can't vote more than once
    public void voteCheck()
    {
        if (voted = true) {
            song1Button.setEnabled(false);
            song2Button.setEnabled(false);
            song3Button.setEnabled(false);
        }
        else{
            song1Button.setEnabled(true);
            song2Button.setEnabled(true);
            song3Button.setEnabled(true);

        }

    }


    //For each song we need to be able to grab updated information
    //about the individual song's votes, so we did that here
    //for each song's vote count.


    //Update for votes on song 1
    private void vote1(final DatabaseReference songRef) {
        songRef.addListenerForSingleValueEvent(new ValueEventListener() {
            String key = "";
            Integer count;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot songSnap : dataSnapshot.getChildren()) {
                    key = songSnap.getKey();
                    count = songSnap.child("song1count").getValue(Integer.class);
                    //System.out.println("the count is :" + count);
                    Log.d("count", count.toString());
                }
                sFirebaseDatabaseReference.child(SONGS_CHILD).child(key.toString()).child("song1count").setValue(count + 1);
                voted();
                voteCheck();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    //Update for votes on song 2
    private void vote2(final DatabaseReference songRef) {
        songRef.addListenerForSingleValueEvent(new ValueEventListener() {
            String key = "";
            Integer count;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot songSnap : dataSnapshot.getChildren()) {
                    key = songSnap.getKey();
                    count = songSnap.child("song2count").getValue(Integer.class);
                    //System.out.println("the count is :" + count);
                    Log.d("count", count.toString());
                }
                sFirebaseDatabaseReference.child(SONGS_CHILD).child(key.toString()).child("song2count").setValue(count + 1);
                voted();
                voteCheck();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    //Update for votes on song 3
    private void vote3(final DatabaseReference songRef) {
        songRef.addListenerForSingleValueEvent(new ValueEventListener() {
            String key = "";
            Integer count;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot songSnap : dataSnapshot.getChildren()) {
                    key = songSnap.getKey();
                    count = songSnap.child("song3count").getValue(Integer.class);
                    //System.out.println("the count is :" + count);
                    Log.d("count", count.toString());
                }
                sFirebaseDatabaseReference.child(SONGS_CHILD).child(key.toString()).child("song3count").setValue(count + 1);
                voted();
                voteCheck();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }



}
