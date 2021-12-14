package com.example.futurefarm

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.futurefarm.SqLiteHandler.SqLiteDatabaseHAndler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth= FirebaseAuth.getInstance()

        login_btn.isEnabled = true

        btn_register.setOnClickListener{
            val intent = Intent(this,UserRegistration::class.java)
            startActivity(intent)
            finish()
        }

        login_btn.setOnClickListener{

            val intent = Intent(applicationContext,Home::class.java)
            startActivity(intent)
            finish()

            if(TextUtils.isEmpty(editText.text.toString())){
                editText.error = "Please enter emailId"
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(editText2.text.toString())){
                editText2.error = "Please enter password"
                return@setOnClickListener
            }

            val helper = SqLiteDatabaseHAndler(applicationContext)
            val db:SQLiteDatabase = helper.readableDatabase

            val cv = ContentValues()
            cv.put("User_Id",editText.text.toString())
            cv.put("Login_Status", editText2.text.toString())
            db.insert("USER_STATE",null,cv)

//            auth.signInWithEmailAndPassword(editText.text.toString(),editText2.text.toString()).addOnCompleteListener {
//                 if(it.isSuccessful){
//                     val intent = Intent(this,Home::class.java)
//                     startActivity(intent)
//                     finish()
//                }else{
//                 Toast.makeText(applicationContext,"SignIn Failed"+it.exception.toString(), Toast.LENGTH_SHORT).show()
//                 }
//            }

        }
/*
        var msg: String
        var msg2: String
        var flag_login = 0
        var flag_password = 0

        editText.addTextChangedListener{
            msg = editText.text.toString();

            if(msg.trim().length>0){
                login_btn.isEnabled = true
                flag_login = 1;

            }
            if(msg.trim().length<0){
                flag_login = 0;
                login_btn.isEnabled = false

            }


        }

        editText2.addTextChangedListener{
            msg2 = editText2.text.toString();

            if(msg2.trim().length>0){
                login_btn.isEnabled = true
                flag_password = 1;
            }
            if(msg2.trim().length<0){
                login_btn.isEnabled = false
                flag_password = 0;
            }



        }
*/



    }
}