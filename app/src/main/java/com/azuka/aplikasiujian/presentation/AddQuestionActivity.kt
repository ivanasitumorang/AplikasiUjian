package com.azuka.aplikasiujian.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azuka.aplikasiujian.R
import com.azuka.aplikasiujian.databinding.ActivityAddQuestionBinding

class AddQuestionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}