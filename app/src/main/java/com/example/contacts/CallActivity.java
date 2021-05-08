package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CallActivity extends AppCompatActivity {
    public static final int REQUEST_CALL_PHONE = 99;
    TextView tvName, tvNumber;
    Button btnCall;
    String contactId, contactName, contactNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        tvName = findViewById(R.id.tvDisplayName);
        tvNumber = findViewById(R.id.tvDisplayNumber);
        btnCall = findViewById(R.id.btnCall);
        Intent intent = getIntent();
        contactId = intent.getStringExtra("contactId");
        contactName = intent.getStringExtra("contactName");
        tvName.setText(contactName);
        // To display Contact Number
        ContentResolver cr = getContentResolver();
        Cursor cursor =cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{contactId}, null);
        cursor.moveToFirst();
        contactNumber =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        tvNumber.setText(contactNumber);
        // btnCall.setText("Call \"" + contactName + "\"");
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    // Toast.makeText(CallActivity.this, "Permission is granted.", Toast.LENGTH_SHORT).show();
                    call();
                }
                else{
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
                }
            }
        });
    }
    private void call(){
        // Toast.makeText(this, "call() method called.", Toast.LENGTH_SHORT).show();
        try{
            Intent iCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+contactNumber));
            // Toast.makeText(this, "Calling : " + contactNumber, Toast.LENGTH_SHORT).show();
            startActivity(iCall);
            // Toast.makeText(this, "Call finished.", Toast.LENGTH_SHORT).show();
        }
        catch(SecurityException ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL_PHONE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                call();
            }
            else{
                // READ_CONTACTS permission has been denied, so we cannot use this feature (We cannot display contacts)
                Toast.makeText(this, "You did not given to call your contacts. So this app cannot make a call.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
