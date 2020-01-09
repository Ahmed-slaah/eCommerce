package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ecommerce.model.Users;
import com.example.ecommerce.prevalent.prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {


    private ProgressDialog LoadinBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);
        Button login = findViewById(R.id.login_main);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        Button signup = findViewById(R.id.join_main);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

            String userphoneKey = Paper.book().read(prevalent.UserphoneKey);
            String userpasswordKey =  Paper.book().read(prevalent.UserpasswordKey);
            if (!userphoneKey.equals("") && !userpasswordKey.equals("")){
                if (TextUtils.isEmpty(userphoneKey) && TextUtils.isEmpty(userpasswordKey)){
                    AllowAcess(userphoneKey,userpasswordKey);


                    LoadinBar.setTitle("Alredy logged in");
                    LoadinBar.setMessage("Please wait..");
                    LoadinBar.setCanceledOnTouchOutside(false);
                    LoadinBar.show();
                }

            }

    }

    private void AllowAcess(final String phone, final String password) {
        final DatabaseReference Rootref ;
        Rootref = FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.child("Users").child(phone).exists()))
                {
                    Users datauser = dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    if (datauser.getPhone().equals(phone)){
                        if(datauser.getPassword().equals(password)){
                            Toast.makeText(MainActivity.this, "Please wait ,, You already logged in ..", Toast.LENGTH_SHORT).show();
                            LoadinBar.dismiss();

                            Intent i = new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(MainActivity.this, "The password is wrong ,, please try again", Toast.LENGTH_SHORT).show();
                            LoadinBar.dismiss();
                        }
                    }


                }else
                {
                    Toast.makeText(MainActivity.this, "This number"+phone+"Is not exist", Toast.LENGTH_SHORT).show();
                    LoadinBar.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
