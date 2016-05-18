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

import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

import in.codehex.facilis.app.Config;
import in.codehex.facilis.helper.CircleTransform;

public class ProfileActivity extends AppCompatActivity {

    Toolbar mToolbar;
    ImageView imgDp;
    FloatingActionButton btnCamera;
    SharedPreferences userPreferences;
    Intent mIntent;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initObjects();
        prepareObjects();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Config.REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            Uri uri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = cursor.getString(columnIndex);
                Picasso.with(this).load(new File(imagePath)).transform(new CircleTransform())
                        .into(imgDp);
                cursor.close();
            }
            // TODO: call this function only when the image path got some value
            uploadDp();
        }
    }

    /**
     * Initialize the objects.
     */
    private void initObjects() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        imgDp = (ImageView) findViewById(R.id.img_dp);
        btnCamera = (FloatingActionButton) findViewById(R.id.btn_camera);

        userPreferences = getSharedPreferences(Config.PREF_USER, MODE_PRIVATE);
    }

    /**
     * Implement and manipulate the objects.
     */
    private void prepareObjects() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(Intent.ACTION_PICK);
                mIntent.setType("image/*");
                startActivityForResult(mIntent, Config.REQUEST_PICK_IMAGE);
            }
        });
    }

    /**
     * Upload the new profile image to the server.
     */
    private void uploadDp() {
        try {
            new MultipartUploadRequest(this, Config.API_SET_PROFILE_PIC +
                    userPreferences.getInt(Config.KEY_PREF_USER_ID, 0) + "/")
                    .addFileToUpload(imagePath, "user_image")
                    .addHeader("Authorization", "Token " +
                            userPreferences.getString(Config.KEY_PREF_TOKEN, null))
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setUtf8Charset()
                    .setMaxRetries(2)
                    .startUpload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
