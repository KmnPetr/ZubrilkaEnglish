package com.example.ze_adminandroid.screens.webView

import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ze_adminandroid.databinding.FragmentWebViewBinding
import com.example.ze_adminandroid.screens.editWord.popupStorage.FileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class WebViewFragment : Fragment() {

    private lateinit var viewModel: WebViewViewModel
    private lateinit var binding: FragmentWebViewBinding
    private lateinit var webView: WebView
    //содержит результат после выполнения кода на странице

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentWebViewBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(WebViewViewModel::class.java)

        setWebView()

        setListeners()
        waitSerchValue()
    }

    /**
     * подождет появления текста в поле поиска на странице
     */
    private fun waitSerchValue() {
        GlobalScope.launch {
            val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val str: String? = clipboardManager.primaryClip?.getItemAt(0)?.text?.toString()

            var stage = 0
            while (stage<7){
            delay(300)
                withContext(Dispatchers.Main){
                    when(stage){
                        0 -> {
                            //подождет появления текста в поле поиска на странице
                            webView.evaluateJavascript("""
                                (function() {
                                    var inputElement = document.getElementById('mlsw7-main-search-input');
                                    if (inputElement.value==='"""+str+"""') {
                                        console.log('совпало значение');
                                        console.log(inputElement.value);
                                        return true;
                                    } else {
                                        console.log(inputElement.value);
                                        console.log('Продолжаем ожидание.');
                                        return false;
                                    }
                                })();
                                """) { value ->
                                println("Result: "+value)
                                if (value=="true") stage = 1
                            }
                        }
                        1 -> {
                            //нажмем на кнопку поиска на странице
                            webView.evaluateJavascript("""
                                (function() {
                                    var buttonElement = document.querySelector('.ui.basic.icon.primary.button.mlsw7-search-action');
                                    if (buttonElement) {
                                        buttonElement.removeAttribute('disabled');
                                        buttonElement.click();
                                        //window.scrollBy(0, 500);
                                        return true;
                                    } else {
                                        console.log(inputElement.value);
                                        return false;
                                    }
                                })();
                                """) { value ->
                                println("Result: "+value)
                                if (value=="true") stage = 2
                            }
                        }
                        in 2..10 -> {
                            webView.evaluateJavascript("""
                                (function() {
                                    var mainPanel = document.querySelector('.ml-main-panel');

                                    // Проверяем, найден ли элемент
                                    if (mainPanel) {
                                        // Получаем координаты элемента относительно видимой части окна просмотра
                                        var rect = mainPanel.getBoundingClientRect();
    
                                        // Вычисляем необходимое количество пикселей для прокрутки
                                        var scrollAmount = rect.top - 200; // Пролистываем так, чтобы оставалось 200 пикселей до верха
    
                                        // Прокручиваем страницу на полученное количество пикселей
                                        window.scrollBy(0, scrollAmount);
                                        return true;
                                } else {
                                    console.log('Элемент не найден');
                                    return false;
                                }
                                })();
                                """) { value ->
                                println("Result: "+value)
                                if (value=="true") stage++
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * развесит слушатели на кнопки
     */
    private fun setListeners() {
        binding.buttonDownload1.setOnClickListener {
            webView.evaluateJavascript(JSCode.Get_href_list.value) { value ->
                println("Result: "+value)
                val listHref:List<String> = splitToList(value)
                saveFileByUrlAndGoBack(listHref.get(0))
            }
        }
        binding.buttonDownload2.setOnClickListener {
            webView.evaluateJavascript(JSCode.Get_href_list.value) { value ->
                println("Result: "+value)
                val listHref:List<String> = splitToList(value)
                saveFileByUrlAndGoBack(listHref.get(1))
            }
        }
    }

    /**
     * уберет лишние ковычки из строки и поделит ее пополам на две ссылки
     */
    private fun splitToList(value: String): List<String> {
        val cutStr = value.substring(1, value.length - 1)
        return cutStr.split("*****").flatMap{ it.split(" ") }
    }


    @SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
    private fun setWebView() {
        webView = binding.webView
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Здесь можно выполнить JavaScript код после загрузки страницы
//                webView.loadUrl("javascript:alert('Страница загружена')")
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: android.webkit.ConsoleMessage): Boolean {
                println("JS console:"+consoleMessage.message())
                return true
            }
        }

        // Открываем страницу по URL
        webView.loadUrl("https://myefe.ru/anglijskaya-transkriptsiya.html")

        // Обработка нажатий на элементы страницы
        webView.setOnTouchListener { v, event ->
            // Здесь можно обработать нажатия пользователя на элементы страницы
            false
        }
        webView.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            println("URL: $url")
            saveFileByUrlAndGoBack(url)
        }
        // Переопределение обработчика кнопки "Назад"
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        })
    }

    /**
     * вернет назад к EditFragment
     */
    private fun goBack() {
        findNavController().popBackStack()
    }

    /**
     * сохранит файл в папке download по переданному ему url
     */
    private fun saveFileByUrlAndGoBack(url: String) {
        GlobalScope.launch {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code $response")
                }

                val bytes = response.body?.bytes()

                val uri = Uri.parse(url)
                val fileName = uri.lastPathSegment

                FileManager.instanse.saveNewFile(fileName,bytes)
                withContext(Dispatchers.Main){
                    Toast.makeText(requireContext(),"Save file: \""+fileName+"\"",Toast.LENGTH_SHORT).show()
                    goBack()
                }
            }
        }
    }

}