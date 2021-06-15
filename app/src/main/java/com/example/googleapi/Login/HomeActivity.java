package com.example.googleapi.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.googleapi.Adapter.LoginAdapter;
import com.example.googleapi.ChuQuan.MainChuQuan;
import com.example.googleapi.Models.User;
import com.example.googleapi.NguoiDung.MainActivity;
import com.example.googleapi.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;


public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 1000;
    ProgressDialog progressDialog;
    private Button f,g;
    private static final String TAGF = "FacebookLogin";
    private CallbackManager mCallbackManager;
    ConstraintLayout layout;
    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton floatingActionButton;
    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;





    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);
        getSupportActionBar().hide();

        layout = findViewById(R.id.layoutLogin);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_page);
        getSupportActionBar().hide();
        g =  (Button) findViewById(R.id.login_button_google);
        f = (Button) findViewById(R.id.login_button_facebook);
//        floatingActionButton = findViewById(R.id.login_google);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        LoginAdapter loginAdapter = new LoginAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(loginAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // Write a message to the database


//        // [START initialize_auth]

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        createRequest();
        // [END initialize_auth]

        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInG();
            }
        });

        signInF();
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
//        sendDataToFragment();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
    }
    // [END on_start_check_user]

    private  void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
        else
            mCallbackManager.onActivityResult(requestCode,resultCode,data);
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure");
                        updateUI(null);
                    }
                });
    }
    // [END auth_with_google]

    private void signInF(){
        // [START config_signin] Configure Google Sign In
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

       // loginButton.setReadPermissions("email", "public_profile");


        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance()
                        .logInWithReadPermissions(HomeActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                    }
                });
            }
        });

        // [END config_signin]
    }
    // [START signin]
    private void signInG() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mGoogleSignInClient.signOut();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(HomeActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(HomeActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }


    public void createAccount(String email, String password, String role) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(HomeActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            createUser(email,role,user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(HomeActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    public void createUser(String email, String role, FirebaseUser userC)
    {
        User user = new User(userC.getUid(),email,role);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference ref = firebaseFirestore.collection("Users").document(user.getUserID());
        ref.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Snackbar.make(layout, "Đăng ký thành công", BaseTransientBottomBar.LENGTH_SHORT).show();
                    updateUI(userC);
                }
            }
        });
    }
    private void signInN(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(HomeActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

//private void sendDataToFragment(){
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.view_page, new SignupTabFragment());
//        fragmentTransaction.commit();
//}
    public void updateUI(FirebaseUser user) {
        if(user!=null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            progressDialog = new ProgressDialog(HomeActivity.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.activity_loading);
            progressDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );

//            new CountDownTimer(2000,500){
//                @Override
//                public void onTick(long millisUntilFinished) {
//
//                }
//
//                @Override
//                public void onFinish() {
//                    progressDialog.cancel();
//                }
//            }.start();

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = firebaseFirestore.collection("Users").document(user.getUid());
            //kiếm tra xem có phải lần đầu đăng nhập = fb/gg không
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //không phải lần đầu
                            Log.d(TAG, "User exists!");
                            User user1 = document.toObject(User.class);
                            if(user1.getRole().equals("chuquan"))
                            {
                                //kiểm tra xem đã tạo quán ăn chưa
                                DocumentReference documentReference1 = firebaseFirestore.collection("QuanAn").document(user.getUid());
                                documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            if(documentSnapshot.exists())
                                            {
                                                //tồn tại quán ăn r
                                                Intent intent = new Intent(getApplicationContext(), MainChuQuan.class);
                                                progressDialog.dismiss();
                                                startActivity(intent);
                                                finish();
                                            }
                                            else
                                            {
                                                //chưa tồn tại quán ăn
                                                progressDialog.dismiss();
                                                showDialogCreateQuanAn();
                                            }
                                        }
                                    }
                                });
                            }
                            else
                            {
                                startActivity(intent);
                                progressDialog.dismiss();
                                finish();
                            }
                        } else {
                            //Là lần đầu
                            Log.d(TAG, "User does not exist!");
                            progressDialog.dismiss();
                            showDialogCFirstTimeLogin();
                        }
                    } else {
                        progressDialog.dismiss();
                        Log.d(TAG, "Failed with: ", task.getException());
                    }
                }
            });


        }

    }

    private void showDialogCFirstTimeLogin() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_first_time_login);
        //
        Button btnchuquan = (Button) dialog.findViewById(R.id.btndialogChuQuan);
        Button btnnguoidung = (Button) dialog.findViewById(R.id.btndialogNguoiDung);
        //

        btnchuquan.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String email = user.getEmail();
            createUser(email,"chuquan",user);
            dialog.dismiss();
        });

        btnnguoidung.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String email = user.getEmail();
            createUser(email,"nguoidung",user);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void showDialogCreateQuanAn()
    {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ask_create_quanan);
        //
        Button btnCo = (Button) dialog.findViewById(R.id.btndialogCo);
        Button btnKhong = (Button) dialog.findViewById(R.id.btndialogKhong);
        //

        btnCo.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CreateQuanAnActivity.class);
            startActivity(intent);
            dialog.dismiss();
        });

        btnKhong.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }
}