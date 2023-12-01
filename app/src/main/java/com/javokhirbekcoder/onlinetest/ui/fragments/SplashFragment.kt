package com.javokhirbekcoder.onlinetest.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.javokhirbekcoder.onlinetest.R
import com.javokhirbekcoder.onlinetest.databinding.FragmentSplashBinding
import com.javokhirbekcoder.onlinetest.ui.viewModels.SplashFragmentViewModel
import com.javokhirbekcoder.onlinetest.utils.NetworkStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: SplashFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSplashBinding.bind(view)

        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)

      /* Buni ochib qoyish kk
      viewmodel.getSubjects().observe(viewLifecycleOwner){
            when(it.status){
                NetworkStatus.LOADING ->{

                }
                NetworkStatus.SUCCESS ->{
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }
                NetworkStatus.ERROR ->{
                    showNetworkStateDialog()
                }
            }

        }*/


    }

    private fun showNetworkStateDialog() {
        val noNetworkDialog = AlertDialog.Builder(requireContext())
        noNetworkDialog.setTitle("Serverga ulanishda xato")
            .setMessage("Internet mavjud emas!")
            .setCancelable(false)
            .setPositiveButton("Qaytadan urinish") { dialog, _ ->
                dialog.dismiss()
                viewmodel.getSubjects()
            }
            .setNegativeButton("Chiqish") { dialog, _ ->
                dialog.dismiss()
                requireActivity().finish()
            }
        noNetworkDialog.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}