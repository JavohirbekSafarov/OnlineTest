package com.javokhirbekcoder.onlinetest.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Button
import android.widget.LinearLayout.LayoutParams
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.javokhirbekcoder.onlinetest.R
import com.javokhirbekcoder.onlinetest.databinding.FragmentTestingBinding
import com.javokhirbekcoder.onlinetest.ui.adapters.OnItemClickListener
import com.javokhirbekcoder.onlinetest.ui.adapters.TestsDialogRvAdapter
import com.javokhirbekcoder.onlinetest.ui.models.AnswerModel
import com.javokhirbekcoder.onlinetest.ui.models.EnterTestModel
import com.javokhirbekcoder.onlinetest.ui.models.LoginDataModel
import com.javokhirbekcoder.onlinetest.ui.models.TestModelLocal
import com.javokhirbekcoder.onlinetest.ui.viewModels.TestingFragmentViewModel
import com.javokhirbekcoder.onlinetest.utils.NetworkStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("SetTextI18n")
class TestingFragment : Fragment(R.layout.fragment_testing), OnItemClickListener {

    private var _binding: FragmentTestingBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: TestingFragmentViewModel by viewModels()

    private var asciiVal = 65
    private var answersSize = 0

    private lateinit var loginDataModel: LoginDataModel
    private lateinit var enterTestModel: EnterTestModel
    //private lateinit var test: TestModel
    //private var isLoadingTest = false
    //private val answers = ArrayList<String>()
    // private val loadingTest = true

    private var reloadTestPos = -1
    private var questionPos = -1

    private var testModel = ArrayList<TestModelLocal>()

    private lateinit var alertDialog: AlertDialog

    private var openedtest = TestModelLocal("", 0, -1, "", -1, 0)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTestingBinding.bind(view)
// For testing
//        viewmodel.addAnswerLocal(AnswerModel(1, "A", "A"))
//        viewmodel.addAnswerLocal(AnswerModel(3, "B", "B"))
//        viewmodel.addAnswerLocal(AnswerModel(2, "B", "C"))

        firstLoadTest()



        binding.swiperefresh.setOnRefreshListener {
            if (reloadTestPos != -1) {
                showTest(reloadTestPos)
                addLog("Reload id = $reloadTestPos")
            } else {
                if (testModel.size > 0) {
                    showTest(testModel.lastIndex)
                    addLog("(reloadTestPos = -1) Reload id = ${testModel.lastIndex}")
                }
            }
            binding.swiperefresh.isRefreshing = false
        }

        binding.questionsBtn.setOnClickListener {
            showTestsListDialog()
        }

        binding.submitBtn.setOnClickListener {
            if (viewmodel.getAnswersLocal().size == enterTestModel.contest.test_count) {
                addLog("Hamma javob kiritilgan")
                val contesters = enterTestModel.contesters
                var answersString = ""
                val answersList = viewmodel.getAnswersLocal()
                //for (i in enterTestModel.contest.tests)
                for (i in 1..enterTestModel.contest.test_count) {
                    val testid = enterTestModel.contest.tests.split(",")[i - 1].toInt()
                    answersList.forEach{
                        if (it.id == testid){
                            answersString += it.selectedAnswer + ","
                        }
                    }
                }
                contesters.answers = answersString
                addLog(answersString)
                viewmodel.submitTest(contesters).observe(viewLifecycleOwner){
                    when (it.status) {
                        NetworkStatus.LOADING -> {
                            binding.progressCircular.visibility = View.VISIBLE
                        }
                        NetworkStatus.SUCCESS -> {
                            binding.progressCircular.visibility = View.INVISIBLE

                            addLog("succes add to api")

                            CoroutineScope(Dispatchers.IO).launch {
                                viewmodel.addAnswersDatabase()
                                CoroutineScope(Dispatchers.Main).launch {
                                    findNavController().navigate(R.id.action_testingFragment_to_resultFragment)
                                }
                            }
                        }
                        NetworkStatus.ERROR -> {
                            binding.progressCircular.visibility = View.INVISIBLE
                            addLog("error to add api")
                            addLog(it.message!!)
                        }
                    }
                }
            } else {
                addLog("To'liq javob berilmagan")
            }
        }

        binding.nextBtn.setOnClickListener {

            //showTest()
        }

        binding.previousBtn.setOnClickListener {
            //showTest()
        }
    }

    private fun firstLoadTest() {
        loginDataModel = viewmodel.getDataFromShared()

        if (loginDataModel.guid.isNullOrEmpty().not()) {

            CoroutineScope(Dispatchers.IO).launch {

                if (viewmodel.getEnterTestModel().contest == null) {

                    CoroutineScope(Dispatchers.Main).launch {

                        addLog("Null")

                        viewmodel.enterTest(loginDataModel.guid!!, loginDataModel.subId!!)
                            .observe(viewLifecycleOwner) {
                                when (it.status) {
                                    NetworkStatus.LOADING -> {
                                        binding.progressCircular.visibility = View.VISIBLE
                                    }

                                    NetworkStatus.SUCCESS -> {
                                        enterTestModel = it.data!!
                                        CoroutineScope(Dispatchers.IO).launch {
                                            viewmodel.saveEnterTestModel(enterTestModel)
                                        }
                                        addLog("Api")
                                        saveTestsLocal()
                                    }

                                    NetworkStatus.ERROR -> {
                                        binding.progressCircular.visibility = View.INVISIBLE
                                        binding.logText.text =
                                            binding.logText.text.toString() + it.message
                                    }
                                }
                            }
                    }
                } else {
                    enterTestModel = EnterTestModel(
                        viewmodel.getEnterTestModel().contest!!,
                        viewmodel.getEnterTestModel().contesters!!
                    )
                    CoroutineScope(Dispatchers.Main).launch {
                        addLog("Database")
                        saveTestsLocal()
                    }

                }
            }


        } else {
            Toast.makeText(requireContext(), "Iltimos qaytadan testga kiring!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showTestsListDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_tests_list, null)
        dialogView.setBackgroundColor(Color.TRANSPARENT)

        val rv = dialogView.findViewById<RecyclerView>(R.id.testsRv)

        val adapter = TestsDialogRvAdapter(testModel, this)
        rv.adapter = adapter

        //Toast.makeText(requireContext(), adapter.itemCount.toString(), Toast.LENGTH_SHORT).show()

        builder.setView(dialogView)
            .setTitle("Savolni tanlang:")

        alertDialog = builder.create()
        alertDialog.show()
    }


    private fun saveTestsLocal() {
        viewmodel.deleteTestLocal()
        for (i in 1..enterTestModel.contest.test_count) {
            //for (i in enterTestModel.contest.tests.split(",")) {
            //if (i.isNotEmpty()) {
            binding.logText.text =
                binding.logText.text.toString() + "\nTestni yuklash position = " + i
            //if (enterTestModel.contest.test_count)
            loadTest(enterTestModel.contest.tests.split(",")[i - 1].toInt())
            //}
        }
        testModel = viewmodel.getTestsLocal()

        binding.progressCircular.visibility = View.INVISIBLE
    }

    private fun loadTest(id: Int) {
        //reloadTestPos = id
        viewmodel.getTest(id).observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }

                NetworkStatus.SUCCESS -> {
                    val testModelLocal = TestModelLocal(
                        it.data!!.answer, it.data.answer_count,
                        it.data.id, it.data.question_path, it.data.subject_id, 0
                    )
                    viewmodel.addTestLocal(testModelLocal)
                    binding.logText.text =
                        binding.logText.text.toString() + "\nTestni saqlash id = " + it.data.id
                    if (enterTestModel.contest.test_count == viewmodel.getTestsLocal().size) {
                        showTest(0)
                    }
                }

                NetworkStatus.ERROR -> {
                    binding.progressCircular.visibility = View.INVISIBLE
                    binding.progressText.visibility = View.INVISIBLE
                    showErrorPage()
                }
            }
        }
    }

    private fun createButtons() {
        asciiVal = 65
        binding.answersLayout.removeAllViews()
        for (i in 1..answersSize) {
            val params = LayoutParams(
                LayoutParams.MATCH_PARENT,
                280
            )
            //marging
            params.topMargin = 15
            params.bottomMargin = 15

            val btn = MaterialButton(requireContext())
            btn.cornerRadius = 15
            btn.id = i
            val myId = btn.id
            btn.text = asciiVal.toChar().toString()
            btn.textSize = 13.0f
            btn.insetBottom = 0

            btn.insetTop = 0
            asciiVal++
            btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
            binding.answersLayout.addView(btn, params)
            val btn1 = requireActivity().findViewById(myId) as Button
            btn1.setOnClickListener {

                viewmodel.addAnswerLocal(
                    AnswerModel(
                        openedtest.id,
                        openedtest.answer,
                        btn.text.toString()
                    )
                )

//                Toast.makeText(
//                    view.context,
//                    "${openedtest.id}, ${openedtest.answer}, ${btn.text}",
//                    Toast.LENGTH_SHORT
//                ).show()

                addLog("${openedtest.id}, ${openedtest.answer}, ${btn.text}")
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun openPdfFromUrl(url: String) {

        binding.swiperefresh.isEnabled = true
        binding.progressCircular.visibility = View.VISIBLE
        binding.progressText.visibility = View.VISIBLE
        binding.pdfView.visibility = View.INVISIBLE

        with(binding) {
            pdfView.clearCache(true)
            pdfView.invalidate()
            pdfView.visibility = View.INVISIBLE
            /*
                        val settings: WebSettings = pdfView.settings
                        settings.javaScriptEnabled = true
                        settings.allowFileAccess = true

                        settings.builtInZoomControls = true
                        settings.setSupportZoom(true)
                        settings.displayZoomControls = false

                        pdfView.loadUrl("file:///android_asset/pdfviewer.html?${url}");*/

            val settings: WebSettings = pdfView.settings
            settings.javaScriptEnabled = true
            settings.builtInZoomControls = true
            settings.setSupportZoom(true)

            pdfView.loadUrl("https://docs.google.com/viewer?url=$url")
            //pdfView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=$url")

            pdfView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    if (newProgress == 100) {
                        binding.progressCircular.visibility = View.INVISIBLE
                        binding.progressText.visibility = View.INVISIBLE
                        binding.swiperefresh.isEnabled = false
                        binding.pdfView.visibility = View.VISIBLE
                    } else {
                        binding.swiperefresh.isEnabled = true
                        binding.progressCircular.visibility = View.VISIBLE
                        binding.progressText.visibility = View.VISIBLE
                        binding.pdfView.visibility = View.INVISIBLE
                        binding.progressText.text = "$newProgress %"
                    }
                }


                @SuppressLint("SuspiciousIndentation")
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    if (title.isNullOrEmpty())
                        pdfView.reload()
                    binding.progressCircular.visibility = View.VISIBLE
                    binding.swiperefresh.isEnabled = true
                }

            }
            /* pdfView.webViewClient = object : WebViewClient() {

                 override fun onReceivedError(
                     view: WebView?,
                     request: WebResourceRequest?,
                     error: WebResourceError?
                 ) {
                     super.onReceivedError(view, request, error)
                     pdfView.reload()
                     //openPdfFromUrl(url)
                     binding.logText.text = binding.logText.text.toString() + "\nXatolik! ->" + error.toString()
                     //Toast.makeText(requireContext(), "Qandaydir xatolik!", Toast.LENGTH_SHORT).show()
                     // Handle loading error here
                     // For example, display an error message to the user
                     //showErrorPage()
                 }

                 override fun onReceivedHttpError(
                     view: WebView?,
                     request: WebResourceRequest?,
                     errorResponse: WebResourceResponse?
                 ) {
                     pdfView.visibility = View.INVISIBLE
                     //pdfView.reload()
                     //openPdfFromUrl(url)
                     //Toast.makeText(requireContext(), "HTTP xatolik!", Toast.LENGTH_SHORT).show()
                     binding.logText.text = binding.logText.text.toString() + "\n HTTP xatolik! ->" + errorResponse.toString()
                     super.onReceivedHttpError(view, request, errorResponse)
                 }

                 override fun onReceivedSslError(
                     view: WebView?,
                     handler: SslErrorHandler?,
                     error: SslError?
                 ) {
                     super.onReceivedSslError(view, handler, error)
                     openPdfFromUrl(url)
                     Toast.makeText(requireContext(), "SSL xatolik!", Toast.LENGTH_SHORT).show()
                     binding.logText.text = binding.logText.text.toString() + "\n" + error.toString()
                 }

                 override fun onPageFinished(view: WebView?, url: String?) {
                     super.onPageFinished(view, url)
                     if (view != null) {
                         if (view.title.equals(""))
                             view.reload()
                     }else{
                         Toast.makeText(requireContext(), "view = null", Toast.LENGTH_SHORT).show()
                     }
 //                    binding.swiperefresh.isEnabled = false
 //                    binding.pdfView.visibility = View.VISIBLE
                 }
             }
 */

        }

    }

    private fun showErrorPage() {
        firstLoadTest()
        binding.pdfView.loadDataWithBaseURL(
            null,
            "<html><body><h2>Testni yuklashda xatolik, Qaytadan urinib ko`ring!</h2></body></html>",
            "text/html",
            "UTF-8",
            null
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.pdfView.destroy()
        _binding = null
    }

    override fun onItemClick(position: Int) {
        alertDialog.dismiss()
        showTest(position)
    }

    private fun showTest(position: Int) {
        addLog("Show test $position")
        questionPos = position
        openedtest = testModel[position]
        answersSize = openedtest.answer_count
        createButtons()
        reloadTestPos = position
        openPdfFromUrl(openedtest.question_path)
    }

    private fun addLog(messsage: String) {
        binding.logText.text = binding.logText.text.toString() + "\n" + messsage
    }
}