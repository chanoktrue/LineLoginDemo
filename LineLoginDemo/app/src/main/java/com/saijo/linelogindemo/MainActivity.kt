package com.saijo.linelogindemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.linecorp.linesdk.LineApiResponse
import com.linecorp.linesdk.LineApiResponseCode
import com.linecorp.linesdk.LoginDelegate
import com.linecorp.linesdk.Scope
import com.linecorp.linesdk.auth.LineAuthenticationParams
import com.linecorp.linesdk.auth.LineLoginApi
import com.linecorp.linesdk.auth.LineLoginResult
import java.lang.Exception
import java.util.Arrays.asList
import kotlin.math.log
import com.linecorp.linesdk.api.LineApiClient as LineApiClient1

class MainActivity : AppCompatActivity() {

    private  var channelId = "1656369018"
    private var loginButton: Button? = null
    private var logoutButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton = findViewById<Button>(R.id.loginButton)
        logoutButton = findViewById<Button>(R.id.logoutButton)

        loginButton!!.setOnClickListener {
            login()
        }

        logoutButton!!.setOnClickListener {
            logout()
        }

    }

    fun login() {
        try {
            val intent =  LineLoginApi.getLoginIntent(
                this,
                channelId,
                LineAuthenticationParams.Builder()
                    .scopes(listOf(Scope.PROFILE, Scope.OC_EMAIL))
                    .build()
            )
//            startActivity(intent)
            startActivityForResult(intent, 1)

        }catch (e: Exception) {
            println(e.localizedMessage)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != 1) {
            Log.e("Error", "Unsupport requset")
            return
        }

        val result: LineLoginResult = LineLoginApi.getLoginResultFromIntent(data)


        Log.e("when", result.responseCode.toString())

        when (result.responseCode) {

            LineApiResponseCode.SUCCESS -> {
                println("aaa")
            }

            LineApiResponseCode.CANCEL -> {
                println("bbb")
            }

            else -> {
                Log.e("ERROR", "LINE When else.")
            }

        }


    }

    fun logout() {
        println("Logout")
    }
}



