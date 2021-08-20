package com.azuka.aplikasiujian.data

import com.azuka.aplikasiujian.external.removeAllSpaces


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
    val selectedAnswer: Answer? = null
)

data class Answer(
    val docType: String = "answer",
    val id: String = (System.currentTimeMillis() / 1000).toString(),
    val answer: String = "",
    val imageUrl: String = "",
    @field:JvmField val isRightAnswer: Boolean = false
)

data class StudentAnswer(
    val docType: String = "student-answer",
    val questions: Question = Question()
)

data class Quiz(
    val name: String = "",
    val id: String = name.removeAllSpaces().plus("-").plus(System.currentTimeMillis()),
    val startTime: String = "",
    val endTime: String = "",
    val createdBy: User = User(),
)

data class QuizStudent(
    val name: String = "",
    val id: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val score: String = "",
    val isTaken: Boolean = false,
    val answeredBy: User = User(),
)

val questions = listOf(
    Question(
        question = "Mana kah angka satu..?",
        answer = listOf(
            Answer(id = "1234", answer = "1", isRightAnswer = true),
            Answer(id = "1235", answer = "2", isRightAnswer = false),
            Answer(id = "1236", answer = "3", isRightAnswer = false)
        )
    ),
    Question(
        question = "Mana kah angka empat..?",
        answer = listOf(
            Answer(id = "2234", answer = "4", isRightAnswer = true),
            Answer(id = "3234", answer = "7", isRightAnswer = false),
            Answer(id = "4234", answer = "3", isRightAnswer = false)
        )
    ),
    Question(
        question = "Mana kah angka lima..?",
        answer = listOf(
            Answer(id = "12341", answer = "5", isRightAnswer = true),
            Answer(id = "12342", answer = "3", isRightAnswer = false),
            Answer(id = "12343", answer = "1", isRightAnswer = false)
        )
    )
)