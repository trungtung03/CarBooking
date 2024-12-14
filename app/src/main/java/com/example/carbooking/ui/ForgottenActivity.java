package com.example.carbooking.ui;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.carbooking.R;
import com.example.carbooking.models.UserObject;
import com.example.carbooking.network.GsonRequest;
import com.example.carbooking.utils.Constants;
import com.example.carbooking.utils.CustomApplication;
import com.example.carbooking.utils.Helper;

import java.util.HashMap;
import java.util.Map;

public class ForgottenActivity extends AppCompatActivity {

    private static final String TAG = ForgottenActivity.class.getSimpleName();

    private EditText passwordRecovery;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar){
            actionBar.hide();
        }


        passwordRecovery = findViewById(R.id.reset_password);

        Button passwordResetButton = findViewById(R.id.password_reset_button);
        passwordResetButton.setOnClickListener(v -> {
            String resetPassword = passwordRecovery.getText().toString();

            if (TextUtils.isEmpty(resetPassword)) {
                Helper.displayErrorMessage(ForgottenActivity.this, "Email field is empty");
            }
            else if (!isValidEmail(resetPassword)) {
                Helper.displayErrorMessage(ForgottenActivity.this, "You have entered invalid email");
            } else {
                Map params = getParams(resetPassword);
                GsonRequest<UserObject> serverRequest = new GsonRequest<UserObject>(
                        Request.Method.POST,
                        Constants.PATH_TO_SERVER_RESET_PASSWORD,
                        UserObject.class,
                        params,
                        createRequestSuccessListener(),
                        createRequestErrorListener());

                ((CustomApplication) getApplication()).getNetworkCall().callToRemoteServer(serverRequest);
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailPattern);
    }

    private Map getParams(String email){
        Map<String, String> params = new HashMap<String,String>();
        params.put(Constants.EMAIL, email);
        return params;
    }

    private Response.Listener<UserObject> createRequestSuccessListener() {
        return response -> {
            try {
                if(response != null){

                }else{
                    Helper.displayErrorMessage(ForgottenActivity.this, "You email was not found in database");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };
    }
}
