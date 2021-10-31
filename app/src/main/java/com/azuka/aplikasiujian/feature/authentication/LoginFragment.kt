package com.azuka.aplikasiujian.feature.authentication

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.azuka.aplikasiujian.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    companion object {
        private const val TAB_TEACHER = 0
        private const val TAB_STUDENT = 1
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUIListener()
    }

    private fun setupUIListener() = with(binding) {
        tvTeacher.setOnClickListener { toggleTabLogin(TAB_TEACHER) }
        tvStudent.setOnClickListener { toggleTabLogin(TAB_STUDENT) }
        btnLogin.setOnClickListener {
            Toast.makeText(requireContext(), "Login", Toast.LENGTH_SHORT).show()
        }

        btnRegister.setOnClickListener {
            Toast.makeText(requireContext(), "Register", Toast.LENGTH_SHORT).show()
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
    }

    private fun toggleTabLogin(selectedTab: Int) {
        if (selectedTab == TAB_TEACHER) {
            binding.tvStudent.typeface = Typeface.DEFAULT
            binding.tvTeacher.typeface = Typeface.DEFAULT_BOLD
        } else {
            binding.tvStudent.typeface = Typeface.DEFAULT_BOLD
            binding.tvTeacher.typeface = Typeface.DEFAULT
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}