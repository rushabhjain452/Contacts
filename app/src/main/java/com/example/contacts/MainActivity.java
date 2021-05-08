package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {
    public static final int REQUEST_READ_CONTACTS = 98;
    SearchView scv;
    ListView lvContacts;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scv = findViewById(R.id.searchView);
//        edt.setOnKeyListener(new View.OnKeyListener(){
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                // Toast.makeText(MainActivity.this, keyCode, Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this, "key pressed.", Toast.LENGTH_SHORT).show();
//                // If you don't want to display the typed character, then return true.
//                // display();
//                try{
//                    ContentResolver cr = getContentResolver();
//                    String name = edt.getText().toString();
//                    // cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME}, "DISPLAY_NAME LIKE ?" + "*", new String[]{name}, "DISPLAY_NAME");
//                    cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME}, "DISPLAY_NAME LIKE '" + name +  "%'", null, "DISPLAY_NAME");
//                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(getBaseContext(), R.layout.row, cursor, new String[]{ContactsContract.Contacts.DISPLAY_NAME}, new int[]{R.id.tvName}, 0);
//                    lvContacts.setAdapter(adapter);
//                    return false;
//                }
//                catch (Exception ex){
//                    Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
//                }
//                return false;
//            }
//        });
        // scv.isIconifiedByDefault(true);
        scv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try{
                    ContentResolver cr = getContentResolver();
                    // String name = edt.getText().toString();
                    // String name = scv.getQuery().toString();
                    String name = newText;
                    // cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME}, "DISPLAY_NAME LIKE ?" + "*", new String[]{name}, "DISPLAY_NAME");
                    // cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME}, "DISPLAY_NAME LIKE ?", new String[]{name + "%'}, "DISPLAY_NAME");
//                    cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME}, "DISPLAY_NAME LIKE '" + name +  "%'", null, "DISPLAY_NAME");
                    // Search in middle of String also
                    cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME}, "DISPLAY_NAME LIKE '%" + name +  "%'", null, "DISPLAY_NAME");
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(getBaseContext(), R.layout.row, cursor, new String[]{ContactsContract.Contacts.DISPLAY_NAME}, new int[]{R.id.tvName}, 0);
                    lvContacts.setAdapter(adapter);
                }
                catch (Exception ex){
                    Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

//        edt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                try{
//                    ContentResolver cr = getContentResolver();
//                    scv.get
//                    String name = edt.getText().toString();
//                    // cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME}, "DISPLAY_NAME LIKE ?" + "*", new String[]{name}, "DISPLAY_NAME");
//                    cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME}, "DISPLAY_NAME LIKE '" + name +  "%'", null, "DISPLAY_NAME");
//                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(getBaseContext(), R.layout.row, cursor, new String[]{ContactsContract.Contacts.DISPLAY_NAME}, new int[]{R.id.tvName}, 0);
//                    lvContacts.setAdapter(adapter);
//                }
//                catch (Exception ex){
//                    Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });

        lvContacts = findViewById(R.id.listView);
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    cursor.moveToPosition(position);
                    int columnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                    String contactId =cursor.getString(columnIndex);
                    String contactName = cursor.getString(cursor.getColumnIndex("DISPLAY_NAME"));

                    Intent intent = new Intent(getBaseContext(), CallActivity.class);
                    intent.putExtra("contactId", contactId);
                    intent.putExtra("contactName", contactName);
                    startActivity(intent);
                }
                catch(Exception ex){
                    Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        display();
    }
    private void display(){
        // Check if the READ_CONTACTS permission is already available
        if(checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
            // READ_CONTACTS permission is already available, show all contacts in ListView
            display_contacts();
        }
        else{
            // READ_CONTACTS permission has not been granted.
            // Provide an additional rationale to the user if the permission was not granted and
            // the user would benefit from additional context for the use of the permission.
            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)){
                Toast.makeText(this, "\"Contacts\" permission is needed to show Contacts.", Toast.LENGTH_LONG).show();
            }
            // Request READ_CONTACTS permission
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE}, REQUEST_READ_CONTACTS);
        }
    }
    private void display_contacts(){
        ContentResolver cr = getContentResolver();
        cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME}, null, null, "DISPLAY_NAME");
        // startManagingCursor(cursor);  // deprecated
        // String name = edt.getText().toString();
        // cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME}, "DISPLAY_NAME LIKE '?%'", new String[]{name}, "DISPLAY_NAME");
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, new String[]{ContactsContract.Contacts.DISPLAY_NAME}, new int[]{R.id.tvName}, 0);
        lvContacts.setAdapter(adapter);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS){
            // Received permission result for READ_CONTACTS permission

            // Check if the only required permission has been granted
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // READ_CONTACTS permission has been granted, Contacts can be displayed
                display_contacts();
            }
            else{
                // READ_CONTACTS permission has been denied, so we cannot use this feature (We cannot display contacts)
                Toast.makeText(this, "You did not given to access your contacts. So this app will not work properly.", Toast.LENGTH_LONG).show();
            }
        }
        else{
            super.onRequestPermissionsResult(requestCode ,permissions, grantResults);
        }
    }
}
