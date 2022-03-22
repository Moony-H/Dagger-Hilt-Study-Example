package com.example.android.hilt.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.android.hilt.data.TestData
import com.example.android.hilt.databinding.ActivityTestTwoBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TestActivityTwo:AppCompatActivity() {
    lateinit var binding: ActivityTestTwoBinding
    @Inject
    lateinit var testData:TestData
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityTestTwoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.activityTestTwoB.setOnClickListener{
            testData.data+="*("
            Log.d("test", testData.data)
            val intent= Intent(applicationContext,TestActivityOne::class.java)
            startActivity(intent)
        }

    }
}