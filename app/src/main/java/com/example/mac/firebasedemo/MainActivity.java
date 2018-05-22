package com.example.mac.firebasedemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.Menu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        MessageUtil.MessageLoadListener{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final String TAG = "MainActivity";
    private FloatingActionButton mSendButton;
    private Button nextSong;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private EditText mMessageEditText, songOneEdit, songTwoEdit, songThreeEdit;
    private TextView votecount1, votecount2, votecount3;
    private FirebaseUser mUser;
    private GoogleApiClient mGoogleApiClient;
    public static final String MESSAGES_CHILD = "messages";
    public static final String SONGS_CHILD = "songs";
    private static DatabaseReference sFirebaseDatabaseReference =
            FirebaseDatabase.getInstance().getReference();
    final DatabaseReference songRef = sFirebaseDatabaseReference.child(SONGS_CHILD);

    public static final int MSG_LENGTH_LIMIT = 64;
    public static final int SONG_LENGTH_LIMIT = 25;

    private FirebaseRecyclerAdapter<ChatMessage, MessageUtil.MessageViewHolder>
            mFirebaseAdapter;

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView messengerTextView;
        public MessageViewHolder(View v) {
            super(v);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            messengerTextView = itemView.findViewById(R.id.messengerTextView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    // User is signed in
                    getSupportActionBar().setTitle(mUser.getEmail());
                    //removeDataFromDatabase();
                } else {
                    // User is signed out
                    Log.d(TAG, "not logged in");
                    Intent signInIntent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(signInIntent);
                    finish();
                }
            }
        };

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        //Initializing our Buttons
        songOneEdit = (EditText) findViewById(R.id.songOneText);
        songTwoEdit = (EditText) findViewById(R.id.songTwoText);
        songThreeEdit = (EditText) findViewById(R.id.songThreeText);
        songOneEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(SONG_LENGTH_LIMIT)});
        songTwoEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(SONG_LENGTH_LIMIT)});
        songThreeEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(SONG_LENGTH_LIMIT)});
        votecount1 = (TextView) findViewById(R.id.votecount1);
        votecount2 = (TextView) findViewById(R.id.votecount2);
        votecount3 = (TextView) findViewById(R.id.votecount3);
        nextSong = (Button) findViewById(R.id.nextSong);


        //Setting up onclick listener event
        nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        songRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = "";
                Integer count1,count2,count3;
                String songname1,songname2,songname3;
                for (DataSnapshot songSnap : dataSnapshot.getChildren()) {
                    if(songSnap != null)
                    {
                        songOneEdit.setEnabled(false);
                        songTwoEdit.setEnabled(false);
                        songThreeEdit.setEnabled(false);
                        mSendButton.setEnabled(false);
                    }
                    key = songSnap.getKey();
                    count1 = songSnap.child("song1count").getValue(Integer.class);
                    count2 = songSnap.child("song2count").getValue(Integer.class);
                    count3 = songSnap.child("song3count").getValue(Integer.class);
                    votecount1.setText(count1.toString());
                    votecount2.setText(count2.toString());
                    votecount3.setText(count3.toString());
                    songname1 = songSnap.child("song1").getValue(String.class);
                    songname2 = songSnap.child("song2").getValue(String.class);
                    songname3 = songSnap.child("song3").getValue(String.class);
                    songOneEdit.setText(songname1);
                    songTwoEdit.setText(songname2);
                    songThreeEdit.setText(songname3);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        //Initializing the floating action button
        mSendButton = (FloatingActionButton) findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initializing the Vote object and adding our songs
                Vote songs = new Vote (songOneEdit.getText().toString(), songTwoEdit.getText().toString(),songThreeEdit.getText().toString());
                addSong(songs);


                songOneEdit.setText(songOneEdit.getText().toString());
                songTwoEdit.setText(songTwoEdit.getText().toString());
                songThreeEdit.setText(songThreeEdit.getText().toString());
                songOneEdit.setEnabled(false);
                songTwoEdit.setEnabled(false);
                songThreeEdit.setEnabled(false);
                mSendButton.setEnabled(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                mAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }



    public void addSong(Vote song) {
        //Pushing songs onto the firebase database
        sFirebaseDatabaseReference.child(SONGS_CHILD).push().setValue(song);

        //Creating a reference to the children nodes
        DatabaseReference songRef = sFirebaseDatabaseReference.child(SONGS_CHILD);
        //Creating an event listener for grabbing updates
        ValueEventListener eventSongListener = new ValueEventListener() {
            @Override
            //Creating our data snapshot to get current database when it changes
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<>();
                String key;
                Integer count1,count2,count3;
                for(DataSnapshot songSnap : dataSnapshot.getChildren()) {
                    key = songSnap.getKey();
                    //Grabbing our current updated song counts
                    count1 = songSnap.child("song1count").getValue(Integer.class);
                    count2 = songSnap.child("song2count").getValue(Integer.class);
                    count3 = songSnap.child("song3count").getValue(Integer.class);
                    //Setting the current updated vote counts
                    votecount1.setText(count1.toString());
                    votecount2.setText(count2.toString());
                    votecount3.setText(count3.toString());
                    //Grabbing the user ID and adding it as a node
                    String userId = songSnap.getKey();
                    list.add(userId);
                }
                //Logcat debugging our list of songs
                Log.d("SONGS", list.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        songRef.addListenerForSingleValueEvent(eventSongListener);
    }

    public void next(){
        //Clearing the database so that we refresh all of the
        //properties of the program
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        root.setValue(null);
        VoteActivity.refreshVote();
        //Refreshing the song edit options
        songOneEdit.setEnabled(true);
        songTwoEdit.setEnabled(true);
        songThreeEdit.setEnabled(true);
        mSendButton.setEnabled(true);

    }



}