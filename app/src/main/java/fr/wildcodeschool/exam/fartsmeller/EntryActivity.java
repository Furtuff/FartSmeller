package fr.wildcodeschool.exam.fartsmeller;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EntryActivity extends AppCompatActivity {
    private EditText latitudeText, longitudeText;
    private SeekBar odorBar;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Button submitButton, resultsButton;
    private TextView odorText, confirmation;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
                }
            }
        };

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (!task.isSuccessful()) {
                            Toast.makeText(EntryActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

        this.latitudeText = (EditText) findViewById(R.id.latitude);
        this.longitudeText = (EditText) findViewById(R.id.longitude);
        this.odorBar = (SeekBar) findViewById(R.id.odor);
        this.submitButton = (Button) findViewById(R.id.submitButton);
        this.confirmation = (TextView) findViewById(R.id.confirmation);
        this.odorText = (TextView) findViewById(R.id.odorText);
        this.resultsButton = (Button)findViewById(R.id.showResult);
        this.odorText.setText("0");


        this.odorBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                odorText.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        this.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!latitudeText.getText().toString().isEmpty() || !longitudeText.getText().toString().isEmpty()) {
                    Calendar today = Calendar.getInstance();
                    FartData currentFart = new FartData(Double.valueOf(latitudeText.getText().toString()),
                            Double.valueOf(longitudeText.getText().toString()),
                            Integer.valueOf(odorText.getText().toString()),
                            sdf.format(today.getTime())
                    );
                    sendFart(currentFart);
                } else {
                    Toast.makeText(EntryActivity.this, "Set lat & long", Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.resultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EntryActivity.this, ResultActivity.class));
            }
        });

    }

    private void sendFart(FartData fart) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(Constants.FART).child(Constants.FART).child(Constants.LAT).push().setValue(fart.getLat());
        mDatabase.child(Constants.FART).child(Constants.FART).child(Constants.LONG).push().setValue(fart.getLongi());
        mDatabase.child(Constants.FART).child(Constants.FART).child(Constants.DATE).push().setValue(fart.getDate());
        mDatabase.child(Constants.FART).child(Constants.FART).child(Constants.ODOR).push().setValue(fart.getOdor());

        confirmation.setText(R.string.confirmation_message);
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

    @IgnoreExtraProperties
    public class FartData {
        public double lat;
        public double longi;
        public int odor;
        public String date;

        public FartData() {
        }

        public FartData(double lat, double longi, int odor, String date) {
            this.lat = lat;
            this.longi = longi;
            this.odor = odor;
            this.date = date;
        }

        public double getLat() {
            return lat;
        }

        public double getLongi() {
            return longi;
        }

        public int getOdor() {
            return odor;
        }

        public String getDate() {
            return date;
        }
    }
}
