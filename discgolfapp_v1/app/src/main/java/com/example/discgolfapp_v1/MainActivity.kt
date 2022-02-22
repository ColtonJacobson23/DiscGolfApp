package com.example.discgolfapp_v1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.discgolfapp_v1.ui.main.MainFragment

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

    fun startInventoryActivity(view: View) {
        val intent = Intent(this, InventoryActivity::class.java)
        startActivity(intent)
    }

    fun startScoreKeeperActivity(view: View) {
        val intent = Intent(this, ScoreKeeperActivity::class.java)
        startActivity(intent)
    }

    fun startDiscFinderActivity(view: View) {
        val intent = Intent(this, DiscFinderActivity::class.java)
        startActivity(intent)
    }

    fun startDiscTracerActivity(view: View) {
        val intent = Intent(this, DiscTracerActivity::class.java)
        startActivity(intent)
    }

    fun startFormAnalyzerActivity(view: View) {
        val intent = Intent(this, FormAnalyzerActivity::class.java)
        startActivity(intent)
    }
}