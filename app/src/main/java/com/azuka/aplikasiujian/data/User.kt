package com.azuka.aplikasiujian.data


/**
 * Created by ivanaazuka on 8/14/21.
 * Android Engineer
 */

data class User(
    val docType: String = "user",
    val name: String = "",
    val id: String = "",
    val role: Int = RoleEnum.Teacher.code
)

data class Question(
    val docType: String = "question",
    val id: String = (System.currentTimeMillis() / 1000).toString(),
    val question: String = "",
    val imageUrl: String = "",
    val answer: List<Answer> = emptyList(),
    val createdBy: User = User(),
    val selectedAnswer: Answer? = null
)

data class Answer(
    val docType: String = "answer",
    val id: String = (System.currentTimeMillis() / 1000).toString(),
    val answer: String = "",
    val imageUrl: String = "",
    @field:JvmField val isRightAnswer: Boolean = false
)

data class StudentAnswerA(
    val answeredBy: User,
    val questions: List<Question>
)

data class StudentAnswer(
    val docType: String = "student-answer",
    val answeredBy: User,
    val questions: Question
)

val questions = listOf(
    Question(
        question = "Mana kah angka satu..?",
        answer = listOf(
            Answer(id = "1234", answer = "1", isRightAnswer = true),
            Answer(id = "1235", answer = "2", isRightAnswer = false),
            Answer(id = "1236", answer = "3", isRightAnswer = false)
        ),
        createdBy = User()
    ),
    Question(
        question = "Mana kah angka empat..?",
        answer = listOf(
            Answer(id = "2234", answer = "4", isRightAnswer = true),
            Answer(id = "3234", answer = "7", isRightAnswer = false),
            Answer(id = "4234", answer = "3", isRightAnswer = false)
        ),
        createdBy = User()
    ),
    Question(
        question = "Mana kah angka lima..?",
        answer = listOf(
            Answer(id = "12341", answer = "5", isRightAnswer = true),
            Answer(id = "12342", answer = "3", isRightAnswer = false),
            Answer(id = "12343", answer = "1", isRightAnswer = false)
        ),
        createdBy = User()
    )
)