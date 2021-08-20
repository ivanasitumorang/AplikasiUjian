package com.azuka.aplikasiujian.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azuka.aplikasiujian.base.Result
import com.azuka.aplikasiujian.data.*
import com.azuka.aplikasiujian.data.Constants.Collection
import com.azuka.aplikasiujian.external.removeAllSpaces
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by ivanaazuka on 8/15/21.
 * Android Engineer
 */

@HiltViewModel
class QuizVM @Inject constructor(
    private val database: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) : ViewModel() {

    var quizId = ""

    private val _createQuizStatus = MutableLiveData<Boolean>()
    val createQuizStatus: LiveData<Boolean> get() = _createQuizStatus
    fun createQuiz(quiz: Quiz) {
        quizId = quiz.id
        viewModelScope.launch(Dispatchers.IO) {
            database.collection(Collection.QUIZZES)
                .document(quiz.id)
                .set(quiz, SetOptions.merge())
                .addOnSuccessListener {
                    _createQuizStatus.postValue(true)
                }.addOnFailureListener {
                    _createQuizStatus.postValue(false)
                }
        }
    }

    private val _createQuestionStatus = MutableLiveData<Result<Boolean>>()
    val createQuestionStatus: LiveData<Result<Boolean>> get() = _createQuestionStatus
    fun createQuestion(quizId: String, question: Question) {
        viewModelScope.launch(Dispatchers.IO) {
            val questionRef = database.collection(Collection.QUIZZES)
                .document(quizId)
                .collection(Collection.QUESTIONS)
                .document()
            val questionId = questionRef.id

            val url =
                "gs://aplikasi-ujian-1d605.appspot.com/questions/Screen Shot 2021-04-19 at 10.30.34.png"
            storage.getReferenceFromUrl(url).downloadUrl
                .addOnSuccessListener { imageUri ->
                    val imageUrl = imageUri.toString()
                    Log.i("Hasil", "image url = $imageUri")
                    val quest = question.copy(id = questionId, imageUrl = imageUrl)

                    questionRef.set(quest, SetOptions.merge()).addOnCompleteListener {
                        if (it.isSuccessful) {
                            _createQuestionStatus.postValue(Result.Success(true))
                        } else {
                            _createQuestionStatus.postValue(Result.Error(Exception("Gagal")))
                        }
                    }
                }.addOnFailureListener {
                    _createQuestionStatus.postValue(Result.Error(Exception("Gagal upload gambar")))
                }
        }
    }

    private val _createUserStatus = MutableLiveData<Result<Boolean>>()
    val createUserStatus: LiveData<Result<Boolean>> get() = _createUserStatus
    fun createUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            database.collection(Collection.USERS).document(user.id)
                .set(user, SetOptions.merge())
                .addOnSuccessListener {
                    _createUserStatus.postValue(Result.Success(true))
                }.addOnFailureListener { e ->
                    _createUserStatus.postValue(Result.Error(e))
                }
        }
    }

    private val _activeUser = MutableLiveData<User>()
    val activeUser: LiveData<User> get() = _activeUser
    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            database.collection(Collection.USERS).document(auth.currentUser!!.uid)
                .get().addOnSuccessListener { docSnapshot ->
                    val user = docSnapshot.toObject<User>()
                    if (user != null) {
                        _activeUser.postValue(user)
                    } else {
                        _activeUser.postValue(null)
                    }
                }.addOnFailureListener { e ->
                    _activeUser.postValue(null)
                }
        }
    }

    private val _questions = MutableLiveData<Result<List<Question>>>()
    val questions: LiveData<Result<List<Question>>> get() = _questions
    fun getQuestions(quizId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            database.collection(Collection.QUIZZES).document(quizId)
                .collection(Collection.QUESTIONS)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val questionList = it.result.documents.map { doc ->
                            val question = doc.toObject<Question>()!!
                            question
                        }
                        _questions.postValue(Result.Success(questionList))
                    } else {
                        _questions.postValue(Result.Error(Exception("Gagal fetch data")))
                    }

                }
        }
    }

    private val _saveAnswerStatus = MutableLiveData<String>()
    val saveAnswerStatus: LiveData<String> get() = _saveAnswerStatus
    fun saveAnswer(quizId: String, question: Question) {
        viewModelScope.launch(Dispatchers.IO) {
//            val studentAnswerId = answeredBy.name.removeAllSpaces() + "-" + answeredBy.id
//            val studentAnswer = StudentAnswer(
//                questions = question
//            )
//            val questionId = question.id
//            Log.i("Hasil", "questionId = $questionId")
//            database.collection(Collection.STUDENT_ANSWER)
//                .document(studentAnswerId)
//                .collection(quizId)
//                .document(questionId)
//                .set(studentAnswer, SetOptions.merge())
//                .addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        Log.i("Hasil", "Jawaban pertanyaan id ${question.id} tersimpan")
////                        _saveAnswerStatus.postValue("Jawaban pertanyaan id ${question.id} tersimpan")
//                    } else {
//                        // error
//                        Log.i("Hasil", "Jawaban pertanyaan id ${question.id} tidak tersimpan")
////                        _saveAnswerStatus.postValue("Jawaban pertanyaan id ${question.id} tidak tersimpan")
//                    }
//                }

            val studentAnswer = StudentAnswer(questions = question)
            val studentAnswerId = getStudentAnswerNodeId(activeUser.value!!)
            database.collection(Collection.STUDENT_ANSWER)
                .document(studentAnswerId)
                .collection(Collection.QUIZZES)
                .document(quizId)
                .collection(Collection.QUESTIONS)
                .document(question.id)
                .set(studentAnswer, SetOptions.merge())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.i("Hasil", "Jawaban pertanyaan id ${question.id} tersimpan")
//                        _saveAnswerStatus.postValue("Jawaban pertanyaan id ${question.id} tersimpan")
                    } else {
                        // error
                        Log.i("Hasil", "Jawaban pertanyaan id ${question.id} tidak tersimpan")
//                        _saveAnswerStatus.postValue("Jawaban pertanyaan id ${question.id} tidak tersimpan")
                    }
                }
        }
    }

    private fun getStudentAnswerNodeId(user: User): String = with(user) {
        name.removeAllSpaces() + "-" + id
    }

    private val _startQuizStatus = MutableLiveData<Boolean>()
    val startQuizStatus: LiveData<Boolean> get() = _startQuizStatus
    fun startQuizByStudent(quizStudent: QuizStudent) {
        viewModelScope.launch(Dispatchers.IO) {
            database.collection(Collection.STUDENT_ANSWER)
                .document(getStudentAnswerNodeId(quizStudent.answeredBy))
                .set(quizStudent)
                .addOnSuccessListener {
                    _startQuizStatus.postValue(true)
                }.addOnFailureListener {
                    _startQuizStatus.postValue(false)
                }
        }
    }


    private val _availableQuizzes = MutableLiveData<List<Quiz>>()
    val availableQuizzes: LiveData<List<Quiz>> get() = _availableQuizzes
    fun getAvailableQuiz() {
        viewModelScope.launch(Dispatchers.IO) {
            database.collection(Collection.QUIZZES)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val quizList = it.result.documents.map { doc ->
                            val quiz = doc.toObject<Quiz>()!!
                            quiz
                        }
                        _availableQuizzes.postValue(quizList)
                    } else {
                        _availableQuizzes.postValue(emptyList())
                    }

                }
        }
    }

    private val _takenQuizzes = MutableLiveData<List<QuizStudent>>()
    val takenQuizzes: LiveData<List<QuizStudent>> get() = _takenQuizzes
    fun getTakenQuiz() {
        viewModelScope.launch(Dispatchers.IO) {
            val studentAnswerId = getStudentAnswerNodeId(activeUser.value!!)
            Log.i("Hasil", "quizId = $quizId")
            database.collection(Collection.STUDENT_ANSWER)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val quizList = it.result.documents.map { doc ->
                            val quiz = doc.toObject<QuizStudent>()!!
                            quiz
                        }
                        _takenQuizzes.postValue(quizList)
                    } else {
                        _takenQuizzes.postValue(emptyList())
                    }

                }
        }
    }
}