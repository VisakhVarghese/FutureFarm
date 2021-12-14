package com.example.futurefarm

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.LocusId
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.JsonToken
import android.util.Log
import android.widget.Toast
import com.example.futurefarm.ModelClasses.UserReg_DataClass
import com.google.firebase.FirebaseError
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ActionCodeSettings.newBuilder
import com.google.firebase.auth.OAuthProvider.newBuilder

import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_user_registration.*
import java.nio.file.attribute.AclEntry.newBuilder
import java.util.concurrent.TimeUnit


class UserRegistration : AppCompatActivity() {


    val countryCode = +91
    private lateinit var auth: FirebaseAuth
    val loginStatus = 0

    // var databaseReference : DatabaseReference? = null
    var database: FirebaseDatabase? = null

    private lateinit var databaseReference: DatabaseReference



    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken ?=null;

    //private val auth = FirebaseAuth.getInstance()
    private val timeout = 60L

    private  var mVerificationId: String ?=null
    private  var resendToken: PhoneAuthProvider.ForceResendingToken ?=null

    private  var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks ? =null


    override fun onBackPressed() {
        val intent = Intent(applicationContext,Login::class.java)
        startActivity(intent)

        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("users")

        back_btn.setOnClickListener {


            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

//        val isUserSignedIn = FirebaseAuth.getInstance().currentUser != null

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {

                Toast.makeText(this@UserRegistration,"${e.message}",Toast.LENGTH_LONG).show()

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
//                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId
                forceResendingToken = token
                Toast.makeText(this@UserRegistration,"Verification Code Sent",Toast.LENGTH_SHORT).show()
            }
        }

        register_button2.setOnClickListener {
            // otpgen()
            saveUser()


        }

    }

    private fun startPhoneNumberVerification(phoneNumber: String){
        val options = callbacks?.let {
            PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(it)          // OnVerificationStateChangedCallbacks
                .build()
        }
        if (options != null) {
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

//    private fun resendVerificationCode(phoneNumber: String, token: PhoneAuthProvider.ForceResendingToken){
//        val options = PhoneAuthOptions.newBuilder(auth)
//            .setPhoneNumber(phoneNumber)
//            .setTimeout(timeout, TimeUnit.SECONDS)
//            .setActivity(this)
//            .setCallbacks(callbacks)
//            .setForceResendingToken(token)
//            .build()
//        PhoneAuthProvider.verifyPhoneNumber(options)
//    }

//    private fun verifyPhoneNumberWithCode(verificationId: String, code:String){
//            val credential = PhoneAuthProvider.getCredential(verificationId, code)
//        signInWithPhoneAuthCredential(credential)
//    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential).addOnSuccessListener {
            //login success
            saveUser()
        }.addOnFailureListener{e->
            //login failure
            Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()
    }
    }

//    private fun otpgen() {
//        registerPhoneNumber(reg_phone.text.toString())
//    }
//
//    private fun registerPhoneNumber(phoneNumber: String) {
//        val options = PhoneAuthOptions.newBuilder(auth)
//            .setPhoneNumber(phoneNumber)
//            .setTimeout(timeout, TimeUnit.SECONDS)
//            .setActivity(this)
//            .setCallbacks(callbacks)
//            .build()
//        PhoneAuthProvider.verifyPhoneNumber(options)
//    }




    private fun saveUser() {
        var name = reg_name.text.toString().trim()
        var phone_number = reg_phone.text.toString().trim()
        var email = reg_email.text.toString().trim()
        var password = reg_password.text.toString().trim()
        var re_password = reg_rePassword.text.toString().trim()


        if (name.isEmpty()) {
            reg_name.error = "Enter name"
            return
        }
        if (phone_number.isEmpty()) {
            reg_phone.error = "Enter phone number"
            return
        }
        if (password.isEmpty()) {
            reg_password.error = "Enter password"
            return
        }
        if (re_password.isEmpty()) {
            reg_rePassword.error = "Re enter password"
            return
        }
        var password_check = 0;
        if(password.length <8){
            Toast.makeText(applicationContext,"Password should be carry atleast 8 characters" , Toast.LENGTH_LONG)
            reg_password.error = "Password should be carry atleast 8 characters"
            return
        }else if(password != re_password){
            reg_rePassword.error = "Password not match"
            return
        }
        else{
            password_check = 1
        }



        var id = databaseReference.push().key
        var model = UserReg_DataClass(name, phone_number, email, password)
       // databaseReference.child(id!!).setValue(model)

        val ref = FirebaseDatabase.getInstance().getReference("users")

//        val userId = ref.push().key

        if(password_check == 1) {

            databaseReference?.orderByChild("phone_number")?.equalTo(phone_number)
                ?.addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onCancelled(error: DatabaseError) {
                        println(error.message)
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.getValue() != null) {
                            Toast.makeText(
                                applicationContext,
                                "Phone number already used!",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            reg_phone.setSelection(reg_phone.length())
                        } else {

                            startPhoneNumberVerification("+91"+phone_number)
//                            if (userId != null) {
//                                ref.child(phone_number).setValue(model).addOnCompleteListener {
//                                    Toast.makeText(applicationContext, "User is added", Toast.LENGTH_LONG).show()
//                                    val intent = Intent(applicationContext,Home::class.java)
//                                    startActivity(intent)
//
//                                    finish()
//                                }

//
//                            }

                        }


                        /*else{
                        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                            if(it.isSuccessful){
                                val user = FirebaseAuth.getInstance().currentUser
                                user?.sendEmailVerification()?.addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Log.d(TAG, "Email sent.")
                                        var currentUser =  auth.currentUser
                                        var currentUserDb = databaseReference?.child(currentUser?.uid!!)


                                        currentUserDb?.child("customerid")?.setValue(currentUser?.uid!!);
                                        currentUserDb?.child("name")?.setValue(name);
                                        currentUserDb?.child("email")?.setValue(email);
                                        currentUserDb?.child("phonenumber")?.setValue(phone_number);
                                        currentUserDb?.child("password")?.setValue(password);

                                        Toast.makeText(applicationContext, "Email sent.", Toast.LENGTH_LONG)
                                            .show()
                                        val intent = Intent(applicationContext,Login::class.java)
                                        startActivity(intent)
                                        finish()
                                    }else{
                                        Toast.makeText(applicationContext,"SignIn link sending failed!"+it.exception.toString(),Toast.LENGTH_SHORT).show()
                                    }
                                }

                            }else{
                                Toast.makeText(applicationContext,"SignUp Failed"+it.exception.toString(),Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                     */


//                    val children = snapshot.children
//
//                    // This returns the correct child count...
//                    println("count: "+snapshot.children.count().toString())
//                    children.forEach {
//                        println(it.toString())
//                    }
                    }
                })

        }

    }
}

