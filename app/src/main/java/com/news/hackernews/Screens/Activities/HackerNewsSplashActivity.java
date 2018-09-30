package com.news.hackernews.Screens.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.news.hackernews.ApplicationShell.ShellActivity.BaseActivity;
import com.news.hackernews.ConstantHelper.ExtraUtils;
import com.news.hackernews.DataProcessor.RealmManager;
import com.news.hackernews.NetworkAPIExecutor.APIConfig;
import com.news.hackernews.NetworkAPIExecutor.CustomJSONArrayRequest;
import com.news.hackernews.NetworkAPIExecutor.CustomVolleyRequestQueue;
import com.news.hackernews.R;

import org.json.JSONArray;

public class HackerNewsSplashActivity extends BaseActivity implements Response.Listener, Response.ErrorListener{

    private static final String TAG = HackerNewsSplashActivity.class.getSimpleName();
    private static final String URL = APIConfig.HN_URL_BASE + APIConfig.HN_URL_TOP_STORIES;
    private static final int VALUE_SIGN_IN = 7;

    private FirebaseAuth mFirebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private TextView mAppName;

    private RequestQueue mQueue = null;

    @Override
    public void makeWindowFullScreen() {
        super.makeWindowFullScreen();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mFirebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions);

        activateViews();
    }

    private void activateViews(){
        mAppName = findViewById(R.id.tv_Logo);
        //mSignInButton = findViewById(R.id.sign_in_button);

        mAppName.setText(ExtraUtils.HN_TEXT);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mFirebaseAuth.getCurrentUser() != null){
            if (RealmManager.with(this).isEmpty()){
                getData();
                return;
            }
            finish();
            startActivity(new Intent(HackerNewsSplashActivity.this, HackerNewsList.class));
        }
    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, VALUE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VALUE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                addUserToFirebase(account);
            } catch (ApiException e) {
                Log.e(TAG, "Result" + e.getMessage());
            }
        }
    }

    protected void addUserToFirebase(GoogleSignInAccount account){
        AuthCredential mCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(mCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //mSignInButton.setVisibility(View.GONE);
                    Toast.makeText(HackerNewsSplashActivity.this, "Please wait. Precessing result!", Toast.LENGTH_LONG).show();
                    getData();
                }else {
                    Toast.makeText(HackerNewsSplashActivity.this, "Error!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getData(){
        mQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        CustomJSONArrayRequest mJsonRequest = new CustomJSONArrayRequest(Request.Method.GET, URL, new JSONArray(), this, this);
        mJsonRequest.setTag(TAG);
        mQueue.add(mJsonRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mQueue.cancelAll(TAG);
        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show();
        Log.e(TAG, error.toString());
    }

    @Override
    public void onResponse(Object response) {
        JSONArray mArray = ((JSONArray) response);
        RealmManager.with(this).writeNewsIDToRealm(mArray);
        finish();
        startActivity(new Intent(HackerNewsSplashActivity.this, HackerNewsList.class));
    }

    @Override
    protected void onDestroy() {
        if (mQueue != null) {
            mQueue.cancelAll(TAG);
        }
        super.onDestroy();
    }
}
