package com.example.logi_sign

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    @SuppressLint("MissingInflatedId")
    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity2::class.java))
            finish();
        } else {
            startActivity(Intent(this, LoginScreen::class.java))
            finish();
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()


        val finallogin = findViewById<Button>(R.id.finallogin)
        val email = findViewById<EditText>(R.id.logemail)
        val password = findViewById<EditText>(R.id.logpass)
        val forgetPass = findViewById<TextView>(R.id.forgotpass)



        finallogin.setOnClickListener {
            val eil = email.text.toString()
            val passwd = password.text.toString()

            // validations for input email and password
            if (eil.isEmpty() || passwd.isEmpty()) {
                Toast.makeText(this, "Please Fill All The Details", Toast.LENGTH_SHORT).show()

            } else {

                auth.signInWithEmailAndPassword(eil, passwd)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {


                            val verification = auth.currentUser?.isEmailVerified
                            if (verification == true) {
                                val user = auth.currentUser
                                Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT)
                                    .show()
                                startActivity(Intent(this, MainActivity2::class.java))
                                finish()
                            } else if (verification != true) {
                                Toast.makeText(
                                    this,
                                    "Please Verify Your Email First",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            Toast.makeText(
                                this,
                                "LogIn Failed : ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    }
            }
        }

        forgetPass.setOnClickListener {
            startActivity(Intent(this, ResetPassword::class.java))
        }


    }


}