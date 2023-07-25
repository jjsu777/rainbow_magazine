package com.jju.rainbow_magazine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.jju.rainbow_magazine.databinding.FragmentJoinBinding

class Fragment_Join : Fragment() {
    private var _binding: FragmentJoinBinding? = null
    private val binding get() = _binding!!
    var DB: DBHelper? = null

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJoinBinding.inflate(inflater, container, false)
        DB = DBHelper(requireContext())

        binding.signupButton!!.setOnClickListener {
            val username = binding.username!!.text.toString()
            val email = binding.email!!.text.toString()
            val password = binding.password!!.text.toString()
            if (username == "" || email == "" || password == "") Toast.makeText(
                requireContext(),
                "회원정보를 전부 입력해주세요",
                Toast.LENGTH_SHORT
            ).show() else {
                val checkUsername = DB!!.checkUsername(username, email, password)
                if (checkUsername == false) {
                    val insert = DB!!.insertUser(username, email, password, false)

                    if (insert == true) {
                        Toast.makeText(requireContext(), "회원가입을 축하합니다.", Toast.LENGTH_SHORT).show()
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.fragment_container, Fragment_Login())
                        transaction.addToBackStack(null)
                        transaction.commit()
                    } else {
                        Toast.makeText(requireActivity(), "회원 가입에 실패했습니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(requireActivity(), "이미 가입된 회원입니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.loginText.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, Fragment_Login())
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