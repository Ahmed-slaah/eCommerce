package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText input_name , input_phone , input_password;
     Button Register;
     private ProgressDialog laodingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        input_name=findViewById(R.id.name_register);
        input_phone=findViewById(R.id.phone_number_register);
        input_password=findViewById(R.id.password_register);
         Register = findViewById(R.id.register);
         laodingBar = new ProgressDialog(this);
    }

    public void register(View view) {
        String name = input_name.getText().toString();
        String phone = input_phone.getText().toString();
        String password = input_password.getText().toString();
        if
        (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Enter your name", Toast.LENGTH_SHORT).show();
        }
        else if
        (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please Enter your Phone", Toast.LENGTH_SHORT).show();
        }else if
        (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter your password", Toast.LENGTH_SHORT).show();

        }else {
            laodingBar.setTitle("Create account ...");
            laodingBar.setMessage("Loading ,, please wait ...");
            laodingBar.setCanceledOnTouchOutside(false);

            ValidatePhoneNumber (name,phone,password);
        }
        }

    private void ValidatePhoneNumber(final String name, final String phone, final String password)
    {
        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists())){
                    HashMap<String,Object> UserDataMap = new HashMap<>();
                    UserDataMap.put("phone",phone);
                    UserDataMap.put("name",name);
                    UserDataMap.put("password",password);
                    Rootref.child("Users").child(phone).updateChildren(UserDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Congertulation your account has been created", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(RegisterActivity.this ,LoginActivity.class);
                                        startActivity(i);
                                    }else {
                                        Toast.makeText(RegisterActivity.this, "Network error ,, please try again later ...", Toast.LENGTH_SHORT).show();
                                        laodingBar.dismiss();
                                        Intent a = new Intent(RegisterActivity.this,MainActivity.class);
                                        startActivity(a);

                                    }

                                }
                            });
                }
                else
                    {
                    Toast.makeText(RegisterActivity.this, "This number"+phone+"is already exist ", Toast.LENGTH_SHORT).show();
                    laodingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again ...", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

