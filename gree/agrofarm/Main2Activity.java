package com.example.agrofarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Main2Activity extends AppCompatActivity {

    private TextView textView,password,email;
    private Button button1;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textView = findViewById(R.id.textView);
        email = findViewById(R.id.emailid);
        password = findViewById(R.id.password);
        button1 = findViewById(R.id.button);

        firebaseAuth = FirebaseAuth.getInstance();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                startActivity(intent);


            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email1 = email.getText().toString();
                String pwd = password.getText().toString();

                if (email1.isEmpty()){
                    Toast.makeText(Main2Activity.this,"enter valid Email",Toast.LENGTH_SHORT).show();
                }
                else if(pwd.isEmpty()){
                    Toast.makeText(Main2Activity.this,"enter password",Toast.LENGTH_SHORT);
                }
                else if(!(email1.isEmpty() && pwd.isEmpty() )){
                    firebaseAuth.signInWithEmailAndPassword(email1,pwd).addOnCompleteListener(Main2Activity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (! task.isSuccessful()){
                                Toast.makeText(Main2Activity.this,"Login Error,Please Login Again",Toast.LENGTH_SHORT);
                            }
                            else {
                                Intent intoHome = new Intent(Main2Activity.this,Home1Activity.class);
                                startActivity(intoHome);
                            }
                        }
                    });

                }
                else {
                    Toast.makeText(Main2Activity.this,"Error Occurred",Toast.LENGTH_SHORT);
                }

            }
        });


    }
}
