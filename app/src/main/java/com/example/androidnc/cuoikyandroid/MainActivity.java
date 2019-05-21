package com.example.androidnc.cuoikyandroid;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    public static  List<PhoneModel> Phonelist;

    public  static RecyclerView recyclerView;
    private String TAG = "FireBase-Log";
    int RC_SIGN_IN = 1;
    public static DatabaseReference reference;
    public  static MyViewAdapter adapter;
    private String userID ="";
    private String urlfirebase = "https://ckfirebase-1df28.firebaseio.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView =(RecyclerView)findViewById(R.id.Recycler_v);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("539705680155-51lmril474uokpujgh4l83e1dp3st5fb.apps.googleusercontent.com")
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signIn();
    }
    private void onstart(final GoogleSignInAccount account){
        Phonelist = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("AdvancedAndroidFinalTest");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        PhoneModel p = dataSnapshot1.getValue(PhoneModel.class);
                        Phonelist.add(p);
                    }
                    adapter = new MyViewAdapter(MainActivity.this, Phonelist);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else{
                    filebaseAuthGG(account);
                    onstart(account);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void LoadData(DataSnapshot dataSnapshot){


    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        signIn();
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                onstart(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                signIn();
            }
        }
    }
    private void filebaseAuthGG(GoogleSignInAccount account){
        userID =  account.getId();
        if(userID != ""){
            final Map<String,String> map = new HashMap<>();
            map.put("google_id",userID);
            map.put("firebase_url",urlfirebase);

            new firebaseAsyntask(new firebaseAsyntaskView() {


                @Override
                public void onSuccess(String m) {
                    Toast.makeText(MainActivity.this,m,Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFail(String m) {
                    Toast.makeText(MainActivity.this,m,Toast.LENGTH_SHORT).show();
                }
            },map).execute("http://vidoandroid.vidophp.tk/api/FireBase/PushData");
        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuOut){
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onOptionsItemSelected(RecyclerView item) {
        if (item.getId() == R.id.remove_btn) {

        }

        return false;
    }
}
