package com.stardust.scriptdroid.external.open;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.stardust.pio.PFiles;
import com.stardust.scriptdroid.ui.BaseActivity;
import com.stardust.scriptdroid.ui.common.ScriptOperations;
import com.stardust.scriptdroid.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Stardust on 2017/2/2.
 */

public class ImportIntentActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        try {
            handleIntent(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.edit_and_run_handle_intent_error, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void handleIntent(Intent intent) throws FileNotFoundException {
        Uri uri = intent.getData();
        if (uri != null && "content".equals(uri.getScheme())) {
            String ext = PFiles.getExtension(uri.getScheme());
            if (TextUtils.isEmpty(ext)) {
                ext = "js";
            }
            InputStream stream = getContentResolver().openInputStream(uri);
            new ScriptOperations(this, null)
                    .importFile("", stream, ext)
                    .subscribe(s -> finish());
        } else {
            final String path = intent.getData().getPath();
            if (!TextUtils.isEmpty(path)) {
                new ScriptOperations(this, null)
                        .importFile(path)
                        .subscribe(s -> finish());
            }
        }
    }

}