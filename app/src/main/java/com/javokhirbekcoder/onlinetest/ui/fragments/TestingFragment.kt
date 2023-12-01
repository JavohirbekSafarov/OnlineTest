package com.javokhirbekcoder.onlinetest.ui.fragments

import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.javokhirbekcoder.onlinetest.R
import com.javokhirbekcoder.onlinetest.databinding.FragmentTestingBinding
import com.javokhirbekcoder.onlinetest.ui.models.EnterTestModel
import com.javokhirbekcoder.onlinetest.ui.models.LoginDataModel
import com.javokhirbekcoder.onlinetest.ui.models.TestModel
import com.javokhirbekcoder.onlinetest.ui.viewModels.TestingFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestingFragment : Fragment(com.javokhirbekcoder.onlinetest.R.layout.fragment_testing) {

    private var _binding: FragmentTestingBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: TestingFragmentViewModel by viewModels()

    private var asciiVal = 65
    private val answersSize = 4

    private lateinit var loginDataModel: LoginDataModel
    private lateinit var enterTestModel: EnterTestModel
    private lateinit var test: TestModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTestingBinding.bind(view)

        loginDataModel = viewmodel.getDataFromShared()

        /*   if (loginDataModel.guid.isNullOrEmpty().not()){
               viewmodel.enterTest(loginDataModel.guid!!, loginDataModel.subId!!).observe(viewLifecycleOwner){ it ->
                   when (it.status) {
                       NetworkStatus.LOADING -> {
                           binding.progressCircular.visibility = View.VISIBLE
                       }
                       NetworkStatus.SUCCESS -> {
                           //binding.progressCircular.visibility = View.INVISIBLE
                           enterTestModel = it.data!!
                           viewmodel.getTest(1).observe(viewLifecycleOwner){
                               when (it.status) {
                                   NetworkStatus.LOADING -> {
                                       binding.progressCircular.visibility = View.VISIBLE
                                   }
                                   NetworkStatus.SUCCESS -> {

                                       test = it.data!!
                                       //openPdfFromUrl(test.question_path)
                                       //Toast.makeText(requireContext(), test.question_path, Toast.LENGTH_SHORT).show()
                                   }
                                   NetworkStatus.ERROR -> {
                                       binding.progressCircular.visibility = View.INVISIBLE
                                   }
                               }
                           }
                       }
                       NetworkStatus.ERROR -> {
                           binding.progressCircular.visibility = View.INVISIBLE
                       }
                   }
               }
           }
   */

        openPdfFromUrl("https://quiz.onlinegroup.uz/Utils/Uploads/Questions/10:10_21.11.2023_android1.pdf")

        binding.swiperefresh.setOnRefreshListener {
            binding.pdfView.reload()
            binding.swiperefresh.isRefreshing = false
        }

        // region Create Button

        for (i in 1..answersSize) {
            val params = LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                280
            )
            //marging
            params.topMargin = 15
            params.bottomMargin = 15

            val btn = MaterialButton(requireContext())
            btn.cornerRadius = 15
            btn.id = i
            val id_ = btn.id
            btn.text = asciiVal.toChar().toString()
            btn.textSize = 13.0f
            btn.insetBottom = 0
            btn.insetTop = 0
            asciiVal++;
            btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
            binding.answersLayout.addView(btn, params)
            val btn1 = requireActivity().findViewById(id_) as Button
            btn1.setOnClickListener(View.OnClickListener { view ->
                Toast.makeText(
                    view.context,
                    "Button clicked index = $id_, Text = ${btn.text}", Toast.LENGTH_SHORT
                ).show()
            })
        }

        //endregion
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun openPdfFromUrl(url: String) {
        with(binding) {

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
            settings.builtInZoomControls = true;
            settings.setSupportZoom(true)
            pdfView.invalidate()

            pdfView.loadUrl("https://docs.google.com/viewer?url=$url")
            //pdfView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=$url")

            pdfView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    if (newProgress == 100) {
                        binding.progressCircular.visibility = View.INVISIBLE
                        binding.progressText.visibility = View.INVISIBLE
                        binding.swiperefresh.isEnabled = false
                        binding.pdfView.visibility = View.VISIBLE
                    }
                    binding.progressText.text = newProgress.toString() + " %"
                }

            }
            pdfView.webViewClient = object : WebViewClient() {

                // Override onReceivedError to handle loading errors
                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    openPdfFromUrl(url)
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
                    pdfView.reload()
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
//                    binding.swiperefresh.isEnabled = false
//                    binding.pdfView.visibility = View.VISIBLE
                }
            }

        }

    }

    private fun showErrorPage() {
        binding.pdfView.loadDataWithBaseURL(
            null,
            "<html><body><h1>Failed to load page</h1></body></html>",
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
}