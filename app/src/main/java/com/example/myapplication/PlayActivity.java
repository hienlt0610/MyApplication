package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class PlayActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private static final String TAG = PlayActivity.class.getSimpleName();
    private DialogProperties properties;
    private TextView tvPublicKey;
    private TextView tvPrivateKey;
    private RadioGroup rgAction;
    private Button btnSubmit;
    private EditText edtData;
    private TextView tvResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_activity);
        initDialog();
        Button btnPickPublicKey = findViewById(R.id.btn_public_key);
        Button btnPickPrivateKey = findViewById(R.id.btn_private_key);
        tvPublicKey = findViewById(R.id.tv_public_name);
        tvPrivateKey = findViewById(R.id.tv_private_name);
        rgAction = findViewById(R.id.rg_action);
        btnSubmit = findViewById(R.id.btn_submit);
        edtData = findViewById(R.id.edt_data);
        tvResult = findViewById(R.id.tv_result);

        rgAction.check(R.id.rb_encrypt);
        btnPickPublicKey.setOnClickListener(this);
        btnPickPrivateKey.setOnClickListener(this);
        rgAction.setOnCheckedChangeListener(this);
        btnSubmit.setOnClickListener(this);
        tvResult.setOnClickListener(this);
    }

    private void initDialog() {
        properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_public_key:
                FilePickerDialog dialog = new FilePickerDialog(this, properties);
                dialog.setTitle("Select a File");
                dialog.setDialogSelectionListener(files -> {
                    tvPublicKey.setText(files[0]);
                    tvPublicKey.setTag(files[0]);
                });
                dialog.show();
                break;
            case R.id.btn_private_key:
                dialog = new FilePickerDialog(this, properties);
                dialog.setTitle("Select a File");
                dialog.setDialogSelectionListener(files -> {
                    tvPrivateKey.setText(files[0]);
                    tvPrivateKey.setTag(files[0]);
                });
                dialog.show();
                break;
            case R.id.btn_submit:
                switch (rgAction.getCheckedRadioButtonId()) {
                    case R.id.rb_encrypt:
                        encryptData();
                        break;
                    case R.id.rb_decrypt:
                        decryptData();
                        break;
                }
                break;
            case R.id.
                    tv_result:
                onClickCopy(v);
                break;
        }
    }

    private void decryptData() {
        String privatePath = (String) tvPrivateKey.getTag();
        PrivateKey privateKey = getPrivateKey(privatePath);
        if (privateKey == null) {
            Toast.makeText(this, "Private key chưa dc chọn hoặc public key không chính xác", Toast.LENGTH_SHORT).show();
            return;
        }
        String encryptData = edtData.getText().toString().trim();
        tvResult.setText(decrypt(encryptData, privateKey));
    }

    private void encryptData() {
        String publicKeyPath = (String) tvPublicKey.getTag();
        PublicKey publicKey = getPublicKey(publicKeyPath);
        if (publicKey == null) {
            Toast.makeText(this, "Public key chưa dc chọn hoặc public key không chính xác", Toast.LENGTH_SHORT).show();
            return;
        }
        String plainData = edtData.getText().toString().trim();
        tvResult.setText(encrypt(plainData, publicKey));
    }

    public static String encrypt(String data, PublicKey publicKey) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytes = cipher.doFinal(data.getBytes());
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String encryptData, PrivateKey privateKey) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] bytes = cipher.doFinal(Base64.decode(encryptData, Base64.DEFAULT));
            return new String(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PublicKey getPublicKey(String path) {
        try {
            byte[] bytes;
            bytes = IOUtil.readFile(path);
            if(bytes == null) return null;
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PrivateKey getPrivateKey(String path) {
        try {
            byte[] bytes = IOUtil.readFile(path);
            if(bytes == null) return null;
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_encrypt:
                edtData.setText(null);
                btnSubmit.setText("Encrypt");
                break;
            case R.id.rb_decrypt:
                edtData.setText(null);
                btnSubmit.setText("Decrypt");
                break;
        }
    }

    public void onClickCopy(View v) {   // User-defined onClick Listener
        int sdk_Version = android.os.Build.VERSION.SDK_INT;
        if(sdk_Version < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(tvResult.getText().toString());   // Assuming that you are copying the text from a TextView
            Toast.makeText(getApplicationContext(), "Copied to Clipboard!", Toast.LENGTH_SHORT).show();
        }
        else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Text Label", tvResult.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "Copied to Clipboard!", Toast.LENGTH_SHORT).show();
        }
    }
}
