package com.example.audiocall.PhoneDialer;

import static com.example.audiocall.PhoneDialer.Constants.asString;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.audiocall.R;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Predicate;
//import kotlin.collections.CollectionsKt;
@RequiresApi(api = Build.VERSION_CODES.M)
public class CallActivity extends AppCompatActivity {

    Button answer;
    Button hangup;
    TextView callInfo;


    private CompositeDisposable disposables;
    private String number;
    private OngoingCall ongoingCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        answer= findViewById(R.id.answer);
        hangup =findViewById(R.id.hangup);
        callInfo=findViewById(R.id.callInfo);

        ongoingCall = new OngoingCall();
        disposables = new CompositeDisposable();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        number = Objects.requireNonNull(getIntent().getData()).getSchemeSpecificPart();
    }

    public void onAnswerClicked() {
        ongoingCall.answer();
    }

    public void onHangupClicked() {
        ongoingCall.hangup();
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStart() {
        super.onStart();

        assert updateUi(-1) != null;
        disposables.add(
                OngoingCall.state
                        .subscribe(new io.reactivex.functions.Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                updateUi(integer);
                            }
                        }));

        disposables.add(
                OngoingCall.state
                        .filter(new Predicate<Integer>() {
                            @Override
                            public boolean test(Integer integer) throws Exception {
                                return integer == Call.STATE_DISCONNECTED;
                            }
                        })
                        .delay(1, TimeUnit.SECONDS)
                        .firstElement()
                        .subscribe(new io.reactivex.functions.Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                finish();
                            }
                        })
        );

    }

    @SuppressLint("SetTextI18n")
    private Consumer<? super Integer> updateUi(Integer state) {

        callInfo.setText(asString(state) + "\n" + number);

        if (state != Call.STATE_RINGING) {
            answer.setVisibility(View.GONE);
        } else answer.setVisibility(View.VISIBLE);

        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposables.clear();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void start(Context context, Call call) {
        Intent intent = new Intent(context, CallActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(call.getDetails().getHandle());
        context.startActivity(intent);
    }
}