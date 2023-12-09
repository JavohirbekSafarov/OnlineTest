package com.javokhirbekcoder.onlinetest.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.javokhirbekcoder.onlinetest.R
import com.javokhirbekcoder.onlinetest.databinding.FragmentLoginBinding
import com.javokhirbekcoder.onlinetest.ui.models.EnterTestModel
import com.javokhirbekcoder.onlinetest.ui.models.Subjects
import com.javokhirbekcoder.onlinetest.ui.viewModels.LoginFragmentViewModel
import com.javokhirbekcoder.onlinetest.utils.NetworkResult
import com.javokhirbekcoder.onlinetest.utils.NetworkStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: LoginFragmentViewModel by viewModels()
    private val subjects = Subjects()
    private var selectedSub = -1
    private var guid = ""
    private val coroutineScope = CoroutineScope(Dispatchers.IO)


    private val enterTestObserver = Observer<NetworkResult<EnterTestModel>> { result ->
        when (result.status) {
            NetworkStatus.LOADING -> {

            }

            NetworkStatus.SUCCESS -> {
                viewmodel.setDataToShared(guid, selectedSub)
                coroutineScope.launch {
                    viewmodel.saveEnterTestModel(result.data!!)
                    viewmodel.deleteTestLocal()
                }
                Thread.sleep(1000)
                binding.loading.visibility = View.INVISIBLE
                binding.enterTestBtn.visibility = View.VISIBLE
                findNavController().navigate(R.id.action_loginFragment_to_testingFragment)
                Toast.makeText(requireContext(), result.data.toString(), Toast.LENGTH_SHORT).show()
                viewmodel.enterTest(guid, selectedSub).removeObservers(viewLifecycleOwner)
                binding.subjectText.setText("")
                selectedSub = -1
                guid = ""
                binding.guidText.setText("")
            }

            NetworkStatus.ERROR -> {
                binding.loading.visibility = View.INVISIBLE
                binding.enterTestBtn.visibility = View.VISIBLE
                viewmodel.enterTest(guid, selectedSub).removeObservers(viewLifecycleOwner)
                Toast.makeText(
                    requireContext(),
                    result.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        viewmodel.getSubjects().observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    binding.textInputLayout1.isEnabled = false
                    binding.subjectText.setText("")
                    selectedSub = -1
                    guid = ""
                    binding.guidText.setText("")
                }

                NetworkStatus.SUCCESS -> {
                    binding.textInputLayout1.isEnabled = true
                    subjects.clear()
                    subjects.addAll(it.data ?: emptyList())
                    val items = ArrayList<String>()
                    for (a in subjects) {
                        items.add(a.name)
                    }
                    val adapter = ArrayAdapter(requireContext(), R.layout.main_spinner_item, items)
                    (binding.subjectText as? AutoCompleteTextView)?.setAdapter(adapter)
                }

                NetworkStatus.ERROR -> {
                    showNetworkStateDialog()
                }
            }
        }

        binding.subjectText.setOnItemClickListener { _, _, position, _ ->
            //val selectedItem: String = parent.getItemAtPosition(position) as String
            //Toast.makeText(this, "Selected Item: $selectedItem", Toast.LENGTH_SHORT).show()
            selectedSub = subjects[position].id
        }

        binding.guidText.doAfterTextChanged {
            guid = it.toString()
        }

        binding.enterTestBtn.setOnClickListener {
            //viewmodel.setDataToShared("69adbe0a-30a3-4ec6-af02-4216c6469f58", 2)
            //findNavController().navigate(R.id.action_loginFragment_to_testingFragment)
            if (guid != "" && guid.isNotEmpty() && selectedSub != -1) {
                viewmodel.enterTest(guid, selectedSub)
                    .observe(viewLifecycleOwner, enterTestObserver)
                binding.loading.visibility = View.VISIBLE
                binding.enterTestBtn.visibility = View.INVISIBLE
            } else
                showEmptyDataDialog()
        }

        //binding.enterTestBtn.callOnClick()

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

    private fun showEmptyDataDialog() {
        val noNetworkDialog = AlertDialog.Builder(requireContext())
        noNetworkDialog.setTitle("Kiritishda xatolik")
            .setMessage("Ma`lumotlarni to`liq kiriting!")
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }

        noNetworkDialog.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}