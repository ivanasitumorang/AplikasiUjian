package com.azuka.aplikasiujian.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.azuka.aplikasiujian.R
import com.azuka.aplikasiujian.base.Result
import com.azuka.aplikasiujian.data.Constants.Collection
import com.azuka.aplikasiujian.data.Question
import com.azuka.aplikasiujian.data.RoleEnum
import com.azuka.aplikasiujian.data.User
import com.azuka.aplikasiujian.data.questions
import com.azuka.aplikasiujian.databinding.ActivityMainBinding
import com.azuka.aplikasiujian.external.removeAllSpaces
import com.azuka.aplikasiujian.presentation.adapter.QuestionAdapter
import com.azuka.aplikasiujian.presentation.viewmodel.QuizVM
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "Hasil"
    }

    private val db = Firebase.firestore

    private val viewModel by viewModels<QuizVM>()

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: QuestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUIListener()
        initObserver()
        adapter = QuestionAdapter()
        binding.rvQuestion.adapter = adapter

        viewModel.getUser()

//        db.collection("users").document(firebaseAuth.currentUser!!.uid)
//            .get().addOnSuccessListener { docSnapshot ->
//                user = docSnapshot.toObject<User>()!!
//                Log.i("Hasil", "user = $user")
//                initUIBasedOnRole()
//            }


    }

    private fun initObserver() {
        viewModel.activeUser.observe(this, {
            when (it) {
                is Result.Success -> {
                    initUIBasedOnRole(it.data)
                }

                is Result.Error -> {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        })

        viewModel.questions.observe(this, {
            when (it) {
                is Result.Success -> {
                    adapter.submitList(it.data)
                }

                is Result.Error -> {
                    Toast.makeText(this, "Data ga ada", Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.createQuestionStatus.observe(this, {
            when (it) {
                is Result.Success -> {
                    Toast.makeText(this, "Berhasil masukkan pertanyaan", Toast.LENGTH_SHORT).show()
                }

                is Result.Error -> {
                    Toast.makeText(this, "Data ga ada", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initUIBasedOnRole(user: User) {
        fun setupTeacherUI() {
            binding.clTeacher.visibility = View.VISIBLE
            binding.clStudent.visibility = View.GONE
            questions.forEach {
                viewModel.createQuestion("uts1", it.copy(createdBy = user))
            }
        }

        fun setupStudentUI() {
            binding.clTeacher.visibility = View.GONE
            binding.clStudent.visibility = View.VISIBLE
            viewModel.getQuestions("uts1")
        }
        if (user.role == RoleEnum.Teacher.code) setupTeacherUI()
        else setupStudentUI()
    }

    private fun initUIListener() {
        with(binding) {
//            btnCreate.setOnClickListener {
//                val question1 = etQuestion.text.toString()
//                val answer1 = etAnswer1.text.toString()
//                val answer2 = etAnswer2.text.toString()
//                val answer3 = etAnswer3.text.toString()
//                val answer4 = etAnswer4.text.toString()
//
//                if (question1.isEmpty()) return@setOnClickListener
//
//                val answerList = listOf(
//                    Answer(
//                        answer = answer1,
//                        isRightAnswer = true
//                    ),
//                    Answer(
//                        answer = answer2
//                    ),
//                    Answer(
//                        answer = answer3
//                    ),
//                    Answer(
//                        answer = answer4
//                    )
//                )
//
//                val question = Question(
//                    question = question1,
//                    createdBy = user,
//                    answer = answerList
//                )
//
//                createQuestion(question)
//            }

            fabAddQuizGroup.setOnClickListener {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Masukkan nama ujian")
                val customLayout = layoutInflater.inflate(R.layout.dialog_add_quiz_group, null)
                builder.setView(customLayout)
                builder.setPositiveButton("Simpan") { dialog, which ->
                    val quizName = customLayout.findViewById<AppCompatEditText>(R.id.et_group_name)
                        .text.toString()

                    sendQuizNameToActivity(quizName)
                }

                builder.setNegativeButton("Batal") { dialog, which ->
                    dialog.dismiss()
                }

                builder.create().show()
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

    private fun sendQuizNameToActivity(quizName: String) {
        Toast.makeText(this, quizName, Toast.LENGTH_SHORT).show()
        val quizRef = db.collection(Collection.QUIZZES)
            .document(quizName.removeAllSpaces())
        val questionCollection = quizRef.collection(Collection.QUESTIONS)
        val questionCollectionPath = questionCollection.path
        Log.i(TAG, "question path = $questionCollectionPath")

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