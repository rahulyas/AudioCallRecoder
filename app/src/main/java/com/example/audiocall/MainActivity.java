package com.example.audiocall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;
    private EditText mEditTextNumber;
    //private ToggleButton toggleButton;
    //private TextView textSubHeader;
    //private Button btn;
    ArrayList<String> permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  btn = (Button) findViewById(R.id.btn);

      //  toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        mEditTextNumber = findViewById(R.id.edit_text_number);
        ImageView imageCall = findViewById(R.id.image_call);
        //textSubHeader = (TextView) findViewById(R.id.textSubHeader);

        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    int accessStorage = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE );
                    int accessContact = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS);
                    int accessCall = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE );
                    int accessAudio = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO );

                    permissions = new ArrayList();

                    if (accessStorage == PackageManager.PERMISSION_DENIED) {
                        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                    if (accessContact == PackageManager.PERMISSION_DENIED) {
                        permissions.add(Manifest.permission.READ_CONTACTS);
                    }
                    if (accessCall == PackageManager.PERMISSION_DENIED) {
                        permissions.add(Manifest.permission.CALL_PHONE);
                    }
                    if (accessAudio == PackageManager.PERMISSION_DENIED) {
                        permissions.add(Manifest.permission.RECORD_AUDIO);
                    }

                    if(permissions.size() > 0) {
                        ActivityCompat.requestPermissions(MainActivity.this, permissions.toArray(new String[permissions.size()]), 1);
                    }else{

                        Toast.makeText(MainActivity.this,"Call Recorder Started",Toast.LENGTH_LONG).show();
                    }
                }

                Intent serviceIntent = new Intent(MainActivity.this, CallRecoder.class);
                startService(serviceIntent);

            }
        });


      /*  btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    int accessStorage = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE );
                    int accessContact = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS);
                    int accessCall = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE );
                    int accessAudio = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO );

                    permissions = new ArrayList();

                    if (accessStorage == PackageManager.PERMISSION_DENIED) {
                        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                    if (accessContact == PackageManager.PERMISSION_DENIED) {
                        permissions.add(Manifest.permission.READ_CONTACTS);
                    }
                    if (accessCall == PackageManager.PERMISSION_DENIED) {
                        permissions.add(Manifest.permission.CALL_PHONE);
                    }
                    if (accessAudio == PackageManager.PERMISSION_DENIED) {
                        permissions.add(Manifest.permission.RECORD_AUDIO);
                    }

                    if(permissions.size() > 0) {
                        ActivityCompat.requestPermissions(MainActivity.this, permissions.toArray(new String[permissions.size()]), 1);
                    }else{

                        Toast.makeText(MainActivity.this,"Call Recorder Started",Toast.LENGTH_LONG).show();
                    }
                }

                Intent serviceIntent = new Intent(MainActivity.this, CallRecoder.class);
                startService(serviceIntent);


            }

        }); */


    }

    private void makePhoneCall() {
        String number = mEditTextNumber.getText().toString();
        if (number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        } else {
            Toast.makeText(MainActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }
/*
    @Override
    protected void onResume() {
        super.onResume();

        // Runtime permission
        try {

            boolean permissionGranted_OutgoingCalls = ActivityCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_GRANTED;
            boolean permissionGranted_phoneState = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
            boolean permissionGranted_recordAudio = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
            boolean permissionGranted_WriteExternal = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            boolean permissionGranted_ReadExternal = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;


            if (permissionGranted_OutgoingCalls) {
                if (permissionGranted_phoneState) {
                    if (permissionGranted_recordAudio) {
                        if (permissionGranted_WriteExternal) {
                            if (permissionGranted_ReadExternal) {
                                try {
                                    toggleButton.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
                            }
                        } else {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
                        }
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 400);
                    }
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 500);
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 600);
            }

        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

    }

    @SuppressLint("ResourceAsColor")
    public void toggleButtonClick(View view) {
        try {
            boolean checked = ((ToggleButton) view).isChecked();
            if (checked) {
                Intent intent = new Intent(this, RecordingService.class);
                startService(intent);
                Toast.makeText(getApplicationContext(), "Call Recording is set ON", Toast.LENGTH_SHORT).show();
                textSubHeader.setText("Switch on Toggle to record your calls");
            } else {
                Intent intent = new Intent(this, RecordingService.class);
                stopService(intent);
                Toast.makeText(getApplicationContext(), "Call Recording is set OFF", Toast.LENGTH_SHORT).show();
                textSubHeader.setText("Switch Off Toggle to stop recording your calls");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200 || requestCode == 300 || requestCode == 400 || requestCode == 500 || requestCode == 600) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    toggleButton.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }*/
}