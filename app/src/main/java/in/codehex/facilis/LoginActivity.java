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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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

public class LoginActivity extends AppCompatActivity {

    EditText editEmail, editPass;
    TextView textForgotPass;
    Button btnLogin;
    SharedPreferences userPreferences;
    Intent mIntent;
    ProgressDialog mProgressDialog;
    WifiManager mWifiManager;
    WifiInfo mWifiInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initObjects();
        prepareObjects();
    }

    /**
     * Initialize the objects.
     */
    private void initObjects() {
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPass = (EditText) findViewById(R.id.edit_pass);
        textForgotPass = (TextView) findViewById(R.id.text_forgot_pass);
        btnLogin = (Button) findViewById(R.id.btn_login);

        userPreferences = getSharedPreferences(Config.PREF_USER, MODE_PRIVATE);
        mProgressDialog = new ProgressDialog(this);
        mWifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    /**
     * Implement and manipulate the objects.
     */
    private void prepareObjects() {
        mProgressDialog.setMessage("Logging in..");
        mProgressDialog.setCancelable(false);

        textForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(mIntent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();
                String pass = editPass.getText().toString().trim();

                if (TextUtils.isEmpty(email))
                    editEmail.setError(getString(R.string.error_email_empty));
                else if (!isValidEmail(email))
                    editEmail.setError(getString(R.string.error_email_invalid));

                if (TextUtils.isEmpty(pass))
                    editPass.setError(getString(R.string.error_pass_empty));
                else if (pass.length() < 6)
                    editPass.setError(getString(R.string.error_pass_invalid));


                if (!TextUtils.isEmpty(email) && isValidEmail(email)
                        && !TextUtils.isEmpty(pass) && pass.length() >= 6)
                    processLogin(email, pass, mWifiInfo.getMacAddress());
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
     * Send request to the server to process login and fetch user token.
     *
     * @param email  email address of the user.
     * @param pass   password submitted by the user.
     * @param client wifi mac address of the user's device
     */
    private void processLogin(final String email, final String pass, final String client) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Config.KEY_API_USERNAME, email);
            jsonObject.put(Config.KEY_API_PASSWORD, pass);
            jsonObject.put(Config.KEY_API_CLIENT, client);
        } catch (JSONException e) {
            // TODO: remove toast
            Toast.makeText(LoginActivity.this,
                    "Error occurred while generating data - "
                            + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        showProgressDialog();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Config.API_LOGIN, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgressDialog();
                try {
                    String firstName = response.getString(Config.KEY_API_FIRST_NAME);
                    String lastName = response.getString(Config.KEY_API_LAST_NAME);
                    int userId = response.getInt(Config.KEY_API_USER_ID);
                    int companyId = response.getInt(Config.KEY_PREF_COMPANY_ID);
                    boolean creditStatus = response.getBoolean(Config.KEY_PREF_CREDIT_STATUS);
                    int role = response.getInt(Config.KEY_API_ROLE);
                    String token = response.getString(Config.KEY_API_TOKEN);
                    String userImage = response.getString(Config.KEY_API_USER_IMAGE);

                    SharedPreferences.Editor editor = userPreferences.edit();
                    editor.putString(Config.KEY_PREF_FIRST_NAME, firstName);
                    editor.putString(Config.KEY_PREF_LAST_NAME, lastName);
                    editor.putInt(Config.KEY_PREF_USER_ID, userId);
                    editor.putInt(Config.KEY_PREF_COMPANY_ID, companyId);
                    editor.putBoolean(Config.KEY_PREF_CREDIT_STATUS, creditStatus);
                    editor.putInt(Config.KEY_PREF_ROLE, role);
                    editor.putString(Config.KEY_PREF_TOKEN, token);
                    editor.putString(Config.KEY_USER_IMAGE, userImage);
                    editor.apply();

                    mIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mIntent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mIntent);
                } catch (JSONException e) {
                    // TODO: remove toast
                    Toast.makeText(LoginActivity.this,
                            "Error occurred while parsing data - "
                                    + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                NetworkResponse response = error.networkResponse;
                try {
                    byte[] data = response.data;
                    String mError = new String(data);
                    JSONObject errorObject = new JSONObject(mError);
                    JSONArray nonFieldErrors = errorObject
                            .getJSONArray(Config.KEY_API_NON_FIELD_ERRORS);
                    String errorData = nonFieldErrors.getString(0);
                    Toast.makeText(LoginActivity.this, errorData, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    // TODO: remove toast
                    Toast.makeText(LoginActivity.this,
                            "Error occurred while parsing data - "
                                    + e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    // TODO: remove toast
                    Toast.makeText(LoginActivity.this, "Network error - "
                            + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "login");
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
