package com.azuka.aplikasiujian.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.azuka.aplikasiujian.R
import com.azuka.aplikasiujian.base.Result
import com.azuka.aplikasiujian.data.RoleEnum
import com.azuka.aplikasiujian.data.User1
import com.azuka.aplikasiujian.databinding.ActivityLoginBinding
import com.azuka.aplikasiujian.presentation.viewmodel.QuizVM
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "Hasil"
    }

    private lateinit var binding: ActivityLoginBinding

    private lateinit var googleSignInClient: GoogleSignInClient

    private val viewModel by viewModels<QuizVM>()

    private val db = Firebase.firestore

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObserver()

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnSignIn.setOnClickListener {
            signIn()
        }
    }

    private fun initObserver() {
        viewModel.createUserStatus.observe(this, Observer {
            when (it) {
                is Result.Success -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    resetLoginUI()
                }

                is Result.Error -> {
                    Toast.makeText(this, "Gagal simpan data ${it.exception}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val user = firebaseAuth.currentUser
        if (user != null) {
            checkIfUserAlreadyRegistered(user)
        }
    }

    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
            }
        }

    private fun signIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        loginLauncher.launch(signInIntent)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = firebaseAuth.currentUser
                    user?.let {
                        checkIfUserAlreadyRegistered(user)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun checkIfUserAlreadyRegistered(account: FirebaseUser) {
        val userCollectionRef = db.collection("users")
        val userId = account.uid
        userCollectionRef.document(userId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.exists()) {
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        setupUserRole(account)
                    }
                } else {
                    setupUserRole(account)
                }
            }
    }

    private fun resetLoginUI() = with(binding) {
        btnSignIn.visibility = View.VISIBLE
        tvRoleTitle.visibility = View.GONE
        btnTeacher.visibility = View.GONE
        btnStudent.visibility = View.GONE
    }

    private fun showUserRoleUI() = with(binding) {
        btnSignIn.visibility = View.GONE
        tvRoleTitle.visibility = View.VISIBLE
        btnTeacher.visibility = View.VISIBLE
        btnStudent.visibility = View.VISIBLE
    }

    private fun setupUserRole(account: FirebaseUser) {
        val user = User1(
            id = account.uid,
            name = account.displayName ?: "Nama belum diset"
        )

        showUserRoleUI()
        binding.btnTeacher.setOnClickListener {
            val dataToSave = user.copy(role = RoleEnum.Teacher.code)
            viewModel.createUser(dataToSave)
        }
        binding.btnStudent.setOnClickListener {
            val dataToSave = user.copy(role = RoleEnum.Student.code)
            viewModel.createUser(dataToSave)
        }
    }
}