package com.azuka.aplikasiujian.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.azuka.aplikasiujian.data.Answer
import com.azuka.aplikasiujian.data.Question
import com.azuka.aplikasiujian.data.RoleEnum
import com.azuka.aplikasiujian.data.User
import com.azuka.aplikasiujian.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "Hasil"
    }

    private val db = Firebase.firestore

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private lateinit var user: User

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUIListener()

        db.collection("users").document(firebaseAuth.currentUser!!.uid)
            .get().addOnSuccessListener { docSnapshot ->
                user = docSnapshot.toObject<User>()!!
                Log.i("Hasil", "user = $user")
                initUIBasedOnRole()
            }


    }

    private fun initUIBasedOnRole() {
        fun setupTeacherUI() {
            binding.clTeacher.visibility = View.VISIBLE
            binding.clStudent.visibility = View.GONE
        }

        fun setupStudentUI() {
            binding.clTeacher.visibility = View.GONE
            binding.clStudent.visibility = View.VISIBLE
        }
        if (user.role == RoleEnum.Teacher.code) setupTeacherUI()
        else setupStudentUI()
    }

    private fun initUIListener() {
        with(binding) {
            btnCreate.setOnClickListener {
                val question1 = etQuestion.text.toString()
                val answer1 = etAnswer1.text.toString()
                val answer2 = etAnswer2.text.toString()
                val answer3 = etAnswer3.text.toString()
                val answer4 = etAnswer4.text.toString()

                if (question1.isEmpty()) return@setOnClickListener

                val answerList = listOf(
                    Answer(
                        answer = answer1,
                        isRightAnswer = true
                    ),
                    Answer(
                        answer = answer2
                    ),
                    Answer(
                        answer = answer3
                    ),
                    Answer(
                        answer = answer4
                    )
                )

                val question = Question(
                    question = question1,
                    createdBy = user,
                    answer = answerList
                )

                createQuestion(question)
            }

            btnLogout.setOnClickListener {
                Firebase.auth.signOut()
                GoogleSignIn.getClient(
                    this@MainActivity,
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                ).signOut().addOnSuccessListener {
                    finish()
                }
            }
        }
    }

    private fun createQuestion(question: Question) {
        val questionRef = db.collection("questions")
        questionRef.add(question)
            .addOnSuccessListener { documentRef ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentRef.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}