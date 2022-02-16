package com.example.discgolfapp_v1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.discgolfapp_v1.ui.main.MainFragment

const val EXTRA_MESSAGE = "com.example.discgolfapp_v1.MESSAGE"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        //if (savedInstanceState == null) {
        //    supportFragmentManager.beginTransaction()
        //            .replace(R.id.container, MainFragment.newInstance())
        //            .commitNow()
        //}
    }

    fun sendMessage(view: View) {
        val editText = findViewById<EditText>(R.id.textbox1)
        val message = editText.text.toString()
        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }
}