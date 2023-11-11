package com.orders.aydivedics;

import static com.orders.aydivedics.Utility.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.orders.aydivedics.ui.login.LoginActivity;

public class userButtonActivity extends AppCompatActivity {
Button agent,labelCreator;
TextView logout, userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_button);

        agent = findViewById(R.id.agent);
        labelCreator = findViewById(R.id.label);
        logout = findViewById(R.id.logout);
        userName = findViewById(R.id.username);
        SharedPreferences sp1= getSharedPreferences ("Login", MODE_PRIVATE);

        String username = sp1.getString("username", null);
        String userType = sp1.getString("userType", null);

        userName.setText(username);

        agent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userType.equals("agent")){
                    Intent mainIntent = new Intent(userButtonActivity.this, MainActivity.class);
                    userButtonActivity.this.startActivity(mainIntent);
                }
                else {
                    Toast.makeText(userButtonActivity.this, "Sorry , you don't have access to this function", Toast.LENGTH_SHORT).show();
                }


            }
        });

        labelCreator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userType.equals("reporter")){
                    Intent mainIntent = new Intent(userButtonActivity.this, orderReportActivity.class);
                    userButtonActivity.this.startActivity(mainIntent);
                }
                else{
                    Toast.makeText(userButtonActivity.this, "Sorry , you don't have access to this function", Toast.LENGTH_SHORT).show();
                }

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                SharedPreferences sp= activity.getSharedPreferences("Login", MODE_PRIVATE);
                                SharedPreferences.Editor Ed=sp.edit();
                                Ed.putString("username",null );
                                Ed.putString("password",null);
                                Ed.putString("userId",null);
                                Ed.putString("userType",null);
                                Ed.commit();
                                Intent mainIntent = new Intent(userButtonActivity.this, LoginActivity.class);
                                finish();
                                userButtonActivity.this.startActivity(mainIntent);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(userButtonActivity.this);
                builder.setMessage("Are you sure to logout?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }
}