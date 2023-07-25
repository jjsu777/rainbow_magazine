package com.jju.rainbow_magazine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jju.rainbow_magazine.databinding.FragmentMyBinding

class Fragment_My : Fragment() {
    private var _binding: FragmentMyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.mypageLogout.setOnClickListener {
            SharedPreferencesUtil.logOut(requireContext())
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, Fragment_Login())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.mypageChange.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, Fragment_Change_Info())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
