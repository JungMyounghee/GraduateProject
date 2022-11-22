package com.example.myhomefood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class JoinActivity extends AppCompatActivity {

    private EditText joinEmail;
    private EditText joinPasswd;

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        joinEmail = findViewById(R.id.join_email);
        joinPasswd = findViewById(R.id.join_passwd);

        Button join_button = findViewById(R.id.join_button);
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((joinPasswd.getText().toString().length() >= 6) && (joinEmail.getText().toString().length() >= 0)) {
                    String email_temp = joinEmail.getText().toString();
                    String passwd_temp = joinPasswd.getText().toString();

                    ((LoginActivity)LoginActivity.createContext).createUser(email_temp, passwd_temp);
                    Toast.makeText(JoinActivity.this, "잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();

                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 1000);

                }
                else if((joinPasswd.getText().toString().length() < 6) && (joinEmail.getText().toString().length() > 0))
                    Toast.makeText(JoinActivity.this, "비밀번호를 6자 이상으로 입력하세요.", Toast.LENGTH_SHORT).show();
                else if((joinEmail.getText().toString().length() == 0) && (joinPasswd.getText().toString().length() >= 6))
                    Toast.makeText(JoinActivity.this, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                else if((joinEmail.getText().toString().length() == 0) && (joinEmail.getText().toString().length() == 0))
                    Toast.makeText(JoinActivity.this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}