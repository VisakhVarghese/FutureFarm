package com.example.futurefarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)



        person_home.setOnClickListener{
            val intent = Intent(this,Profile::class.java)
            startActivity(intent)
        }
    }
}