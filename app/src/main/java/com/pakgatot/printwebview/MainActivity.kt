package com.pakgatot.printwebview

import android.R.attr.data
import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintJob
import android.print.PrintManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private var mWebView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webView = WebView(this)
        val settings = webView.settings
        settings.javaScriptEnabled = true
        webView.addJavascriptInterface(MyJavascriptInterface(this, webView), "Android")

        val htmlDocument = "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>About Legend Blogs</title>\n" +
                "<script type=\"text/javascript\">\n" +
                "           function init()\n" +
                "           {\n" +
                "              Android.doPrint();\n" +
                "           }\n" +
                "        </script>" +
                "</head>\n" +
                "<body>\n" +
                "    <div>\n" +
                "        <h1>About Legend Blogs</h1>\n" +
                "        <p>Legend Blogs is the blog website related to information technology as well as other related topics. Our mission is to provide the best online resources on programming, web development and others technical information. We deliver the useful and best tutorials for you. Legend Blogs has free programming tutorials covering Android, PHP, HTML, Javascript, Jquery and much more topics. Any visitors of this site are free to browse our tutorials, live demos and download scripts.</p>\n" +
                "\n" +
                "        <h3>We Cover Topics :</h3>\n" +
                "        <p>We are providing the resources of the following technologies:</p>\n" +
                "\n" +
                "\n" +
                "        <p>\n" +
                "        <ol>\n" +
                "        <li>Programming</li>\n" +
                "        <li>Android</li>\n" +
                "        <li>php</li>\n" +
                "        <li>My SQL</li>\n" +
                "        <li>HTML-CSS</li>\n" +
                "        <li>Tips and Tricks</li>\n" +
                "        <li>Jquery-Java Script</li>\n" +
                "        </ol>\n" +
                "        </p>\n" +
                "    </div>\n" +
                "<div style=\"clear: both;height: 3px;\"> </div>\n" +
                "    <div>\n" +
                "      <input value=\"print\" type=\"button\" name=\"submit\"\n" +
                "           id=\"btnSubmit\" onclick=\"javascript:return init();\" />\n" +
                "    </div>" +
                "</body>\n" +
                "</html>"

        //load your html to webview
        webView.loadData(htmlDocument, "text/HTML", "UTF-8")

//        webView.loadUrl("Odoo receipt url")
        setContentView(webView)

        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        mWebView = webView
    }

    inner class MyJavascriptInterface(context: Context, view: WebView) {
        private val mPrintJobs: ArrayList<PrintJob> = arrayListOf()
        var context: Context = context
        var view: WebView = view

        @JavascriptInterface
        fun doPrint() {
            runOnUiThread { createWebPrintJob(view) }
        }

        @Suppress("DEPRECATION")
        private fun createWebPrintJob(webView: WebView) {

            // Get a PrintManager instance
            val printManager = context
                    .getSystemService(Context.PRINT_SERVICE) as PrintManager
            val jobName = "Document"

            // Get a print adapter instance
            val printAdapter = webView.createPrintDocumentAdapter(jobName)

            // Create a print job with name and adapter instance
            val printJob: PrintJob = printManager.print(jobName, printAdapter,
                    PrintAttributes.Builder().build())

            if (printJob.isCompleted) {
                Toast.makeText(applicationContext, R.string.print_complete, Toast.LENGTH_SHORT).show()
            } else if (printJob.isFailed) {
                Toast.makeText(applicationContext, R.string.print_failed, Toast.LENGTH_SHORT).show()
            }

            // Save the job object for later status checking
            mPrintJobs.add(printJob)
        }

    }
}