package com.azuka.aplikasiujian.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.azuka.aplikasiujian.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "Hasil"
    }

    private val db = Firebase.firestore

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnCreate.setOnClickListener {
                val question = etQuestion.text.toString()
                val answer1 = etAnswer1.text.toString()

                if (question.isEmpty() || answer1.isEmpty()) return@setOnClickListener
                else {
                    val dataToSave = hashMapOf(
                        "question" to question,
                        "answer1" to answer1
                    )

                    db.collection("questions")
                        .add(dataToSave)
                        .addOnSuccessListener { documentRef ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentRef.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                }
            }

            btnLogout.setOnClickListener {
                Firebase.auth.signOut()
                finish()
            }
        }
    }
}