package fr.wildcodeschool.exam.fartsmeller;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EntryActivity extends AppCompatActivity {
    private EditText latitudeText, longitudeText;
    private SeekBar odorBar;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Button submitButton;
    private TextView odorText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        this.latitudeText = (EditText) findViewById(R.id.latitude);
        this.longitudeText = (EditText) findViewById(R.id.longitude);
        this.odorBar = (SeekBar) findViewById(R.id.odor);
        this.submitButton = (Button) findViewById(R.id.submitButton);
        this.odorText = (TextView)findViewById(R.id.odorText);

        odorBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                    DatabaseReference lat = database.getReference(Constants.LAT);
                    DatabaseReference longi = database.getReference(Constants.LAT);
                    DatabaseReference date = database.getReference(Constants.LAT);
                    DatabaseReference odor = database.getReference(Constants.LAT);
                    lat.setValue(Float.valueOf(latitudeText.getText().toString()));
                    longi.setValue(Float.valueOf(longitudeText.getText().toString()));
                    odor.setValue(Integer.valueOf(odorText.getText().toString()));


                }
            }
        });

    }
}
