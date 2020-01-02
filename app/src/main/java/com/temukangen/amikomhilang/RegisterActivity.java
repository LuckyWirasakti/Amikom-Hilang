package com.temukangen.amikomhilang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.temukangen.amikomhilang.model.Profile;

public class RegisterActivity extends AppCompatActivity {

    private Button btnLogin, btnRegister;
    private EditText edtNim, edtName, edtEmail, edtPassword;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        edtNim = findViewById(R.id.edtNim);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String nim = edtNim.getText().toString();
                final String name = edtName.getText().toString();
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if (nim.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Field nim are empty", Toast.LENGTH_SHORT).show();
                } else if (name.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Field name are empty", Toast.LENGTH_SHORT).show();
                } else if(email.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Field email are empty", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Field password are empty", Toast.LENGTH_SHORT).show();
                } else{
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Profile profile = new Profile();
                                profile.setNim(nim);
                                profile.setName(name);
                                FirebaseDatabase.getInstance().getReference("Profile")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
