package com.example.bubbletea;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bubbletea.Classes.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    //Initialization and Variable definitions
    private Button btnSignup;
    private EditText emailId, password, etName;
    private TextView tvSignup;
    private FirebaseAuth mFirebaseAuth;
    DatabaseReference databaseReference;
    String email, pwd,name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId= findViewById(R.id.RegEmail);
        password = findViewById(R.id.RegPassword);
        etName = findViewById(R.id.RegName);
        btnSignup = findViewById(R.id.RegButton);
        tvSignup = findViewById(R.id.RegTextView);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailId.getText().toString();
                pwd   = password.getText().toString();
                name = etName.getText().toString();

                databaseReference = FirebaseDatabase.getInstance().getReference("users");
                mFirebaseAuth = FirebaseAuth.getInstance();

                if(email.isEmpty())
                {
                    emailId.setError("Please enter email");
                    emailId.requestFocus();
                }
                else if (pwd.isEmpty())
                {
                    password.setError("Please enter a password");
                    password.requestFocus();
                }
                else if (name.isEmpty())
                {
                    etName.setError("Please enter a name");
                    etName.requestFocus();
                }
                else if(email.isEmpty() && pwd.isEmpty() && name.isEmpty() )
                {
                    Toast.makeText(getApplicationContext(),"Required fields are emplty!!!", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
/*
                                MessageDigest digest = null;
                                try {
                                    digest = MessageDigest.getInstance("SHA-256");
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                }
                                byte[] hash = digest.digest(pwd.getBytes(StandardCharsets.UTF_8));

 */
                                Users newUser = new Users(email, pwd, name);

                                FirebaseDatabase.getInstance().getReference("users").child(mFirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(newUser)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                Toast.makeText(RegisterActivity.this, "Registration Complete", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                            }
                                        });
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"server error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
    }
}