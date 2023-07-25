package com.jju.rainbow_magazine

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.jju.rainbow_magazine.databinding.FragmentLoginBinding

class Fragment_Login : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    var DB: DBHelper? = null
    private lateinit var kakaoLoginHelper: KakaoLoginHelper  // Add a property for the KakaoLoginHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        DB = DBHelper(requireContext())
        kakaoLoginHelper = KakaoLoginHelper(requireContext())  // Initialize the KakaoLoginHelper

        binding.loginButton.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            if (username == "" || password == "") {
                Toast.makeText(
                    requireContext(),
                    "회원정보를 전부 입력해주세요",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val checkUserpass = DB!!.checkUserpass(username, password)
                if (checkUserpass == true) {
                    Toast.makeText(
                        requireContext(),
                        "로그인 성공",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(requireActivity(), Activity_Home::class.java)
                    startActivity(intent)
                    SharedPreferencesUtil.setLoggedIn(requireContext(), true, username)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "로그인 실패",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.kakaoLoginButton.setOnClickListener {
            kakaoLoginHelper.loginWithKakao()  // Call the KakaoLoginHelper's loginWithKakao method
        }

        binding.joinText.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, Fragment_Join())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
