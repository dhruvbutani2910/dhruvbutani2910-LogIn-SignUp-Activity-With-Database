package com.example.logi_sign

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class signup_screen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        auth = FirebaseAuth.getInstance()

        val finalSingup = findViewById<Button>(R.id.finalregister)
        val username = findViewById<EditText>(R.id.signusername)
        val email = findViewById<EditText>(R.id.signemail)
        val password = findViewById<EditText>(R.id.signpass)
        val confpassword = findViewById<EditText>(R.id.signconfirmpass)

        finalSingup.setOnClickListener {

            val uname = username.text.toString()
            val emaill = email.text.toString()
            val ppasword = password.text.toString()
            val cofpass = confpassword.text.toString()

            if (uname.isEmpty() || emaill.isEmpty() || ppasword.isEmpty() || cofpass.isEmpty()) {
                Toast.makeText(this, "Please Fill All The Details", Toast.LENGTH_SHORT).show()
            } else if (ppasword != cofpass) {
                Toast.makeText(this, "Confirm Password Is Not Same As Password", Toast.LENGTH_SHORT)
                    .show()
            } else {
                auth.createUserWithEmailAndPassword(emaill, ppasword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            auth.currentUser?.sendEmailVerification()
                                ?.addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "Verification Main Sent, Please Verify",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                                ?.addOnFailureListener {
                                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                                }

                            startActivity(Intent(this, LoginScreen::class.java))
                            finish()

                        } else {
                            Toast.makeText(
                                this,
                                "Registration Failed : ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }


        }

    }
}