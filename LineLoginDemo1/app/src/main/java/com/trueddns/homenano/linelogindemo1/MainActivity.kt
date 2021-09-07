package com.trueddns.homenano.linelogindemo1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.loader.content.AsyncTaskLoader
import com.linecorp.linesdk.LineApiResponse
import com.linecorp.linesdk.LineApiResponseCode
import com.linecorp.linesdk.Scope
import com.linecorp.linesdk.api.LineApiClient
import com.linecorp.linesdk.api.LineApiClientBuilder
import com.linecorp.linesdk.auth.LineAuthenticationParams
import com.linecorp.linesdk.auth.LineLoginApi
import com.linecorp.linesdk.auth.LineLoginResult
import java.lang.Exception
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private var loginButton: Button? = null
    private var logoutButton: Button? = null

    private val channelId = "1656369018"
    private val requestCode = 1
    private var lineApiClient: LineApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // LineApiClient
        val apiClientBuilder = LineApiClientBuilder(applicationContext, channelId)
        lineApiClient = apiClientBuilder.build()

        // Login
        loginButton = findViewById<Button>(R.id.loginButton)
        loginButton!!.setOnClickListener {
            lineLogin()
        }

        // Logout
        logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton!!.setOnClickListener {
            lineLogout()
        }



    }

    private fun lineLogout() {

        val thread = Thread {

            // Do some network request
            Log.e("Logout", "Logout success")
            lineApiClient?.logout()

            runOnUiThread {
                // Update UI
                Log.e("Logout", "runOnUiThread")

            }
        }
        thread.start()

    }

    private fun lineLogin() {
        try {
            val arr = arrayOf(Scope.PROFILE,Scope.OPENID_CONNECT, Scope.OC_EMAIL )
            val intent = LineLoginApi.getLoginIntent(
                this,
                channelId,
                LineAuthenticationParams.Builder()

                    .scopes(arr.asList())

                    .build()
            )
            startActivityForResult(intent, requestCode)
        }catch (e: Exception) {
            Log.e("LineLogin Errror", e.localizedMessage)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != this.requestCode) {
            Log.e("Error", "Unsupport request")
        }

        val result: LineLoginResult = LineLoginApi.getLoginResultFromIntent(data)

        when (result.responseCode) {
            LineApiResponseCode.SUCCESS -> {

               val token = result.lineCredential?.accessToken?.tokenString
               Log.e("Token", token!!)

                val email = result.lineIdToken?.email
                Log.e("Email", email.toString())

                val name = result.lineProfile?.displayName
                Log.e("Name", name!!)

                val url = result.lineProfile?.pictureUrl
                Log.e("Url", url.toString())

            }
            LineApiResponseCode.CANCEL -> {
                Log.e("Error", "Line login canceled by user.")
            }
            else -> {
                Log.e("Error", "Login failed!")
                Log.e("Error", result.errorData.message.toString() )
            }
        }

    }

}


