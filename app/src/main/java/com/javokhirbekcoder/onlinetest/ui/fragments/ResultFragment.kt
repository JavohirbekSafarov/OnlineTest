package com.javokhirbekcoder.onlinetest.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.javokhirbekcoder.onlinetest.R
import com.javokhirbekcoder.onlinetest.databinding.FragmentResultBinding
import com.javokhirbekcoder.onlinetest.ui.models.AnswerModel
import com.javokhirbekcoder.onlinetest.ui.models.EnterTestModel
import com.javokhirbekcoder.onlinetest.ui.models.Subjects
import com.javokhirbekcoder.onlinetest.ui.viewModels.TestingFragmentViewModel
import com.javokhirbekcoder.onlinetest.utils.NetworkStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResultFragment : Fragment(R.layout.fragment_result) {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: TestingFragmentViewModel by viewModels()
    private lateinit var enterTestModel: EnterTestModel
    private lateinit var subjects: Subjects

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentResultBinding.bind(view)

        CoroutineScope(Dispatchers.IO).launch {
            enterTestModel = EnterTestModel(
                viewmodel.getEnterTestModel().contest!!,
                viewmodel.getEnterTestModel().contesters!!
            )
            viewmodel.loadAnswersDatabase()

            CoroutineScope(Dispatchers.Main).launch {
                addDatasToViews()
                calculateAndShowAnswers()
            }
        }

        binding.loginBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                viewmodel.deleteAnswersDatabase()
            }
            findNavController().navigate(R.id.action_resultFragment_to_loginFragment)
        }
    }


    private fun addDatasToViews() {
        binding.contestersIdTv.text = enterTestModel.contesters.id.toString()
        binding.nameTv.text = enterTestModel.contesters.name
        binding.specialityTv.text = enterTestModel.contesters.speciality
        binding.facultyTv.text = enterTestModel.contesters.faculty
        binding.groupTv.text = enterTestModel.contesters.group
        binding.courseTv.text = enterTestModel.contesters.course.toString()
        binding.guidTv.text = enterTestModel.contesters.guid
        binding.contestIdTv.text = enterTestModel.contest.id.toString()
        binding.contestTypeTv.text = "Yuklanmoqda..."
        binding.subjectTv.text = "Yuklanmoqda..."
        binding.testsCountTv.text = enterTestModel.contest.test_count.toString()
        binding.startDateTv.text = enterTestModel.contest.start_date
        binding.endDateTv.text = enterTestModel.contest.end_date
        binding.testsDurationTv.text = enterTestModel.contest.duration.toString()
        binding.testsIdsTv.text = enterTestModel.contest.tests
        binding.maxBallTv.text = enterTestModel.contest.max_ball.toString()

        if (viewmodel.getSubjects().isNullOrEmpty()) {
            viewmodel.reloadSubjects().observe(viewLifecycleOwner) { networkResult ->
                when (networkResult.status) {
                    NetworkStatus.LOADING -> {
                    }

                    NetworkStatus.SUCCESS -> {
                        if (!networkResult.data.isNullOrEmpty()) {
                            networkResult.data.forEach {
                                if (it.id == enterTestModel.contest.subject_id) {
                                    binding.subjectTv.text = it.name
                                }
                            }
                        }
                    }

                    NetworkStatus.ERROR -> {
                        binding.subjectTv.text = "Yuklashda xatolik..."
                    }
                }
            }
        } else {
            subjects = viewmodel.getSubjects()!!
            subjects.forEach {
                if (it.id == enterTestModel.contest.subject_id) {
                    binding.subjectTv.text = it.name
                }
            }
        }

        viewmodel.getContestType(enterTestModel.contest.contest_type_id)
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    NetworkStatus.LOADING -> {
                    }

                    NetworkStatus.SUCCESS -> {
                        binding.contestTypeTv.text = it.data
                    }

                    NetworkStatus.ERROR -> {
                        binding.subjectTv.text = it.message
                    }
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun calculateAndShowAnswers() {

        val answersList: ArrayList<AnswerModel> = viewmodel.getAnswersLocal()
        var correctAnswersCount = 0
        val maxBall = enterTestModel.contest.max_ball.toFloat()
        val oneAnswerBall: Float = maxBall / answersList.size

        //Toast.makeText(requireContext(), answersList.size.toString(), Toast.LENGTH_SHORT).show()

        answersList.forEach {
            //Toast.makeText(requireContext(), it.correctAnswer + " <- " + it.selectedAnswer, Toast.LENGTH_SHORT).show()
            if (it.correctAnswer == it.selectedAnswer)
                correctAnswersCount++
        }

        binding.correctAnswerCountTv.text = correctAnswersCount.toString()
        binding.collectedBallTv.text = String.format("%.2f", (oneAnswerBall * correctAnswersCount))
        binding.collectedPercentageTv.text = String.format("%.2f",((100.0 / answersList.size ) * correctAnswersCount)) + " %"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}