/*
 * Copyright 2016 Bobby
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.codehex.facilis;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.codehex.facilis.app.AppController;
import in.codehex.facilis.app.Config;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText editEmail;
    Button btnResetPass;
    TextView textBackLogin;
    Intent mIntent;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initObjects();
        prepareObjects();
    }

    /**
     * Initialize the objects.
     */
    private void initObjects() {
        editEmail = (EditText) findViewById(R.id.edit_email);
        btnResetPass = (Button) findViewById(R.id.btn_reset_pass);
        textBackLogin = (TextView) findViewById(R.id.text_back_login);

        mProgressDialog = new ProgressDialog(this);
    }

    /**
     * Implement and manipulate the objects.
     */
    private void prepareObjects() {
        mProgressDialog.setMessage("Resetting password..");
        mProgressDialog.setCancelable(false);

        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = editEmail.getText().toString().trim();
                if (TextUtils.isEmpty(mail))
                    editEmail.setError(getString(R.string.error_email_empty));
                else if (!isValidEmail(mail))
                    editEmail.setError(getString(R.string.error_email_invalid));

                if (!TextUtils.isEmpty(mail) && isValidEmail(mail))
                    processForgotPass(mail);
            }
        });

        textBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                mIntent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
            }
        });
    }

    /**
     * Validate the email of the user.
     *
     * @param email email address entered by the user.
     * @return returns true if the email is valid else false.
     */
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Send request to the server to reset the password of the user.
     *
     * @param email the email address of the user.
     */
    private void processForgotPass(String email) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Config.KEY_API_EMAIL, email);
        } catch (JSONException e) {
            // TODO: remove toast
            Toast.makeText(ForgotPasswordActivity.this,
                    "Error occurred while generating data - "
                            + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        showProgressDialog();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Config.API_FORGOT_PASS, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgressDialog();
                mIntent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                mIntent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                Toast.makeText(ForgotPasswordActivity.this,
                        "Password reset link has been sent to your mail",
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                NetworkResponse response = error.networkResponse;
                // TODO: remove toast
                if (response.statusCode == 400)
                    Toast.makeText(ForgotPasswordActivity.this,
                            "Error processing your request! Try again later!",
                            Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "forgot_pass");
    }

    /**
     * Display the progress dialog if dialog is not being shown.
     */
    private void showProgressDialog() {
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    /**
     * Hide the progress dialog if dialog is being shown.
     */
    private void hideProgressDialog() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
