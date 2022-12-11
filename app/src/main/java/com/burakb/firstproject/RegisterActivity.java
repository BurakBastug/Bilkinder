package com.burakb.firstproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText userName, password, passwordCheck, email; //schoolName may be with scrollbar
    private Button submit;
    private RadioButton studentRatio,teacherRatio;
    private RadioGroup group;
    private String type;
    private FirebaseAuth mAuth;
    private DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        userName = findViewById(R.id.fullname);
        password = findViewById(R.id.password);
        passwordCheck = findViewById(R.id.passwordagain);
        email = findViewById(R.id.email);
        submit = findViewById(R.id.submitbtn);
        studentRatio = findViewById(R.id.studentradiobtn);
        teacherRatio = findViewById(R.id.teacherradiobtn);
        group = findViewById(R.id.radioGroup);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance("https://bilkinderdata-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");

        studentRatio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    type = "child";
                }
            }
        });

        teacherRatio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                type = "teacher";
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String mail = email.getText().toString().trim();
        String name = userName.getText().toString();
        String psw = password.getText().toString();
        String pswAgain = passwordCheck.getText().toString();

        if(TextUtils.isEmpty(name)) {
            userName.setError("Full name is required");
        }
        else if(TextUtils.isEmpty(psw)) {
            password.setError("Password is required");
        }
        else if(TextUtils.isEmpty(pswAgain)) {
            passwordCheck.setError("Full name is required");
        }
        else if(TextUtils.isEmpty(mail)) {
            email.setError("E-mail is required");
        }
        else if(TextUtils.isEmpty(type)){
            studentRatio.setError("Please select user type");
        }
        else if(!password.getText().toString().equals(passwordCheck.getText().toString()) || TextUtils.isEmpty(psw) ) {
            passwordCheck.setError("Different password");
        }
        else {
            mAuth.createUserWithEmailAndPassword(mail, psw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        if(studentRatio.isChecked()) {
                            Child child = new Child(name, mail, psw);
                            mData.child("Students").child(mAuth.getInstance().getCurrentUser().getUid()).setValue(child);
                        }
                        else if(teacherRatio.isChecked()) {
                            Teacher teacher = new Teacher(name, mail, psw);
                            mData.child("Teachers").child(mAuth.getInstance().getCurrentUser().getUid()).setValue(teacher);
                        }
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        Toast.makeText(RegisterActivity.this, "User created successfully", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
