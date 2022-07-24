package com.example.audiocall;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class RecordingService extends Service {
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private MediaRecorder recorder = null;
    private int currentFormat = 0;
    private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4,
            MediaRecorder.OutputFormat.THREE_GPP };
    private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4,
            AUDIO_RECORDER_FILE_EXT_3GP };
    //private MediaRecorder rec;
    private boolean recoderstarted;
    private File file;
    String path =Environment.getExternalStorageDirectory().getAbsolutePath();


    ////////////////////////
    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
    }

    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            Toast.makeText(RecordingService.this,
                    "Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            Toast.makeText(RecordingService.this,
                            "Warning: " + what + ", " + extra, Toast.LENGTH_SHORT)
                    .show();
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(output_formats[currentFormat]);
        //recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(getFilename());

        TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        manager.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                if (TelephonyManager.CALL_STATE_IDLE == state ){
                    try {
                        if (recorder == null) {
                            recorder.stop();
                            recorder.reset();
                            recorder.release();
                            recoderstarted = false;
                            recorder = null;
                            stopSelf();
                        //    System.out.println("RAHUL CALL_STATE_IDLE");
                        } else {
                            recorder.stop();
                            recorder.reset();
                            recorder.release();
                            recoderstarted = false;
                            recorder = null;
                            stopSelf();
                      //      System.out.println("RAHUL CALL_STATE_IDLE");
                        }
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                else if(TelephonyManager.CALL_STATE_OFFHOOK==state){
                    //System.out.println("Rahul CALL_STATE_OFFHOOK");
                    try {
                        recorder = new MediaRecorder();
                        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        recorder.setOutputFormat(output_formats[currentFormat]);
                        //recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        recorder.setOutputFile(getFilename());
                        TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
                        recorder.prepare();
                        recorder.start();
                        recoderstarted=true;
                    } catch (IOException e) {
                        System.out.println("Exception ="+ e.getMessage());
                        e.printStackTrace();
                    }
                }


            }
        }, PhoneStateListener.LISTEN_CALL_STATE);

        return START_STICKY;
    }

}
