package com.example.android.hilt.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.android.hilt.data.TestData
import com.example.android.hilt.databinding.ActivityTestOneBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TestActivityOne:AppCompatActivity(){
    lateinit var binding:ActivityTestOneBinding

    @Inject
    lateinit var testData: TestData

    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityTestOneBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.activityTestOneB.setOnClickListener{
            testData.data+="*("
            Log.d("test", testData.data)
            val intent= Intent(applicationContext,TestActivityTwo::class.java)
            startActivity(intent)

        }

    }
}
