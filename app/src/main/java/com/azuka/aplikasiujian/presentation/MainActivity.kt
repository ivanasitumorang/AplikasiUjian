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
import com.azuka.aplikasiujian.data.*
import com.azuka.aplikasiujian.databinding.ActivityMainBinding
import com.azuka.aplikasiujian.external.hide
import com.azuka.aplikasiujian.external.show
import com.azuka.aplikasiujian.presentation.adapter.QuestionAdapter
import com.azuka.aplikasiujian.presentation.adapter.QuizAdapter
import com.azuka.aplikasiujian.presentation.adapter.QuizStudentAdapter
import com.azuka.aplikasiujian.presentation.viewmodel.QuizVM
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import org.joda.time.DateTime

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<QuizVM>()

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: QuestionAdapter

    private lateinit var quizStudentAdapter: QuizStudentAdapter

    private lateinit var quizAdapter: QuizAdapter

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUIListener()
        initObserver()
        adapter = QuestionAdapter { question, answer ->

            Log.i("Hasil", "selected answer = ${answer.id} dari pertanyaan ${question.id}")
            viewModel.saveAnswer(
                quizId = viewModel.quizId,
                question.copy(selectedAnswer = answer)
            )
        }

        quizStudentAdapter = QuizStudentAdapter { quizStudent ->
//            viewModel.startQuizByStudent(quizStudent)
            Log.i("Hasil", "quiz ${quizStudent.name} diklik")
            viewModel.quizId = quizStudent.id
        }

        quizAdapter = QuizAdapter { quiz ->
            viewModel.quizId = quiz.id
            val quizStudent = QuizStudent(
                name = quiz.name,
                id = quiz.id,
                startTime = DateTime.now().toString(),
                answeredBy = user!!
            )
            viewModel.startQuizByStudent(quizStudent)
        }

        binding.rvQuestion.adapter = adapter
        binding.rvAvailableQuiz.adapter = quizAdapter
        binding.rvQuizStudent.adapter = quizStudentAdapter

        viewModel.getUser()
    }

    private fun initObserver() {
        viewModel.activeUser.observe(this, {
            if (it != null) {
                user = it
                initUIBasedOnRole(it)
            } else {
                Toast.makeText(this, "user aktif gada", Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        viewModel.startQuizStatus.observe(this, { started ->
            if (started) {
                viewModel.getQuestions(viewModel.quizId)
                binding.rvQuizStudent.hide()
                binding.rvQuestion.show()
            } else {
                Toast.makeText(this, "Quiz gagal dimulai", Toast.LENGTH_SHORT).show()
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

        viewModel.createQuizStatus.observe(this, { success ->
            if (success) {
                Toast.makeText(this, "Berhasil menyimpan quiz", Toast.LENGTH_SHORT).show()
                questions.forEach { question ->
                    viewModel.createQuestion(viewModel.quizId, question)
                }
            } else Toast.makeText(this, "Gagal menyimpan quiz", Toast.LENGTH_SHORT).show()
        })

        viewModel.availableQuizzes.observe(this, { quizzes ->
            quizAdapter.submitList(quizzes)
        })

        viewModel.takenQuizzes.observe(this, { quizStudents ->
            quizStudentAdapter.submitList(quizStudents)
        })
    }

    private fun initUIBasedOnRole(user: User) {
        fun setupTeacherUI() {
            binding.clTeacher.visibility = View.VISIBLE
            binding.clStudent.visibility = View.GONE
        }

        fun setupStudentUI() {
            binding.clTeacher.visibility = View.GONE
            binding.clStudent.visibility = View.VISIBLE
            viewModel.getAvailableQuiz()
        }
        if (user.role == RoleEnum.Teacher.code) setupTeacherUI()
        else setupStudentUI()
    }

    private fun initUIListener() {
        with(binding) {

            btnAvailableQuiz.setOnClickListener {
                viewModel.getAvailableQuiz()
                rvQuestion.hide()
                rvQuizStudent.hide()
                rvAvailableQuiz.show()
            }

            btnTakenQuiz.setOnClickListener {
                viewModel.getTakenQuiz()
                rvQuestion.hide()
                rvQuizStudent.show()
                rvAvailableQuiz.hide()
            }

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
        val startTime = DateTime.now()
        val endTime = startTime.plusMinutes(10)
        val quiz = Quiz(
            name = quizName,
            startTime = startTime.toString(),
            endTime = endTime.toString(),
            createdBy = user!!
        )
        viewModel.createQuiz(quiz)
    }
}