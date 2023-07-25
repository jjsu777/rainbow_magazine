package com.jju.rainbow_magazine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jju.rainbow_magazine.databinding.FragmentChangeInfoBinding

class Fragment_Change_Info: Fragment() {

    private lateinit var binding: FragmentChangeInfoBinding
    private lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangeInfoBinding.inflate(inflater, container, false)
        dbHelper = DBHelper(context)

        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        binding.changeInfoButton.setOnClickListener {
            val username = binding.username.text.toString()
            val nowPassword = binding.nowPassword.text.toString()
            val tobePassword = binding.tobePassword.text.toString()

            val isPasswordChanged = dbHelper.changePassword(username, nowPassword, tobePassword)

            if (isPasswordChanged) {
                Toast.makeText(context, "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show()
                SharedPreferencesUtil.logOut(requireContext())
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, Fragment_Login())
                transaction.addToBackStack(null)
                transaction.commit()
            } else {
                Toast.makeText(context, "비밀번호 변경에 실패했습니다. 사용자 이름과 현재 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
