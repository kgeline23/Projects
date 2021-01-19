package com.example.sportify.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sportify.Classes.Member;
import com.example.sportify.Classes.Preference;
import com.example.sportify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    //Initialization and Variable definitions
    private Button btnSignup;
    private EditText emailId, password, firstname, lastname, number;
    private TextView tvSignup;
    private FirebaseAuth mFirebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId= findViewById(R.id.RegEmail);
        password = findViewById(R.id.RegPassword);
        firstname = findViewById(R.id.RegFirstName);
        lastname = findViewById(R.id.RegLastName);
        number = findViewById(R.id.RegNumber);
        btnSignup = findViewById(R.id.RegButton);
        tvSignup = findViewById(R.id.RegTextView);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                String fname = firstname.getText().toString();
                String lname = lastname.getText().toString();
                String phone = number.getText().toString();

                databaseReference = FirebaseDatabase.getInstance().getReference("members");
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
                else if (fname.isEmpty())
                {
                    firstname.setError("Please enter a password");
                    firstname.requestFocus();
                }
                else if (lname.isEmpty())
                {
                    lastname.setError("Please enter a password");
                    lastname.requestFocus();
                }
                else if (phone.isEmpty())
                {
                    number.setError("Please enter a password");
                    number.requestFocus();
                }
                else if(email.isEmpty() && pwd.isEmpty() && fname.isEmpty() && lname.isEmpty() && phone.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Required fields are emplty!!!", Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && pwd.isEmpty() && fname.isEmpty() && lname.isEmpty() &&phone.isEmpty() ))
                {

                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {

                                Preference newpref = new Preference("new", 0);
                                /* MAYBE USE SHA256 ON PASSWORD?
                                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                                byte[] hash = digest.digest(pwd.getBytes(StandardCharsets.UTF_8));
                                Member newmem = new Member(fname, lname, email, hash, phone);
                                 */
                                Member newmem = new Member(fname,lname, email, pwd, phone);

                                FirebaseDatabase.getInstance().getReference("members").child(mFirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(newmem)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                Toast.makeText(RegisterActivity.this, "Registration Complete", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}