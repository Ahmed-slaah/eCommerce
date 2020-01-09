package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.model.Users;
import com.example.ecommerce.prevalent.prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    EditText Inputpassword,Inputphone ;
    TextView forget,admin ;
    CheckBox remember ;
    private ProgressDialog LoadinBar;
    private String parentDBname = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Inputpassword = findViewById(R.id.password_login);
        Inputphone= findViewById(R.id.phone_number);
        forget=findViewById(R.id.forget_password);
        admin = findViewById(R.id.admin_login);
        Button login = findViewById(R.id.login_login);
        LoadinBar = new ProgressDialog(this);

        remember=findViewById(R.id.remember_me);
        Paper.init(this);
    }

    public void login(View view) {
        String password = Inputpassword.getText().toString();
        String phone = Inputphone.getText().toString();

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password..", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please enter your phone..", Toast.LENGTH_SHORT).show();
        }
        else
            LoadinBar.setTitle("Login account");
            LoadinBar.setMessage("Please wait");
            LoadinBar.setCanceledOnTouchOutside(false);
            LoadinBar.show();

            AllowaccessAccount(phone,password) ;
    }

    private void AllowaccessAccount(final String phone, final String password)
    {
        if (remember.isChecked()){
            Paper.book().write(prevalent.UserphoneKey,phone);
            Paper.book().write(prevalent.UserpasswordKey,password);
        }
        final DatabaseReference Rootref ;
        Rootref = FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.child(parentDBname).child(phone).exists()))
                {
                    Users datauser = dataSnapshot.child(parentDBname).child(phone).getValue(Users.class);
                    if (datauser.getPhone().equals(phone)){
                        if(datauser.getPassword().equals(password)){
                            Toast.makeText(LoginActivity.this, "Logged in successful", Toast.LENGTH_SHORT).show();
                            LoadinBar.dismiss();

                            Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(LoginActivity.this, "The password is wrong ,, please try again", Toast.LENGTH_SHORT).show();
                            LoadinBar.dismiss();
                        }
                    }


                }else
                    {
                        Toast.makeText(LoginActivity.this, "This number"+phone+"Is not exist", Toast.LENGTH_SHORT).show();
                        LoadinBar.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
