package com.example.discgolfapp_v1

import android.os.Bundle
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AddDiscActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_disc)

        val discSpeedInput = findViewById<EditText>(R.id.disc_speed_input)
        discSpeedInput.onFocusChangeListener = discSpeedFocusChangeListener
    }

    private val discSpeedFocusChangeListener =
        OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                val discSpeedInputText = (view as EditText).text.toString()

                if (discSpeedInputText != "") {
                    val discTypeDropdown = findViewById<Spinner>(R.id.disc_type_input)

                    when (discSpeedInputText.toInt()) {
                        9, 10, 11, 12, 13, 14 -> {
                            if (discTypeDropdown.selectedItemPosition != 0) {
                                discTypeDropdown.setSelection(0)
                                Toast.makeText(
                                    applicationContext,
                                    "Disc Type changed to 'Distance Driver'",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        6, 7, 8 -> {
                            if (discTypeDropdown.selectedItemPosition != 1) {
                                discTypeDropdown.setSelection(1)
                                Toast.makeText(
                                    applicationContext,
                                    "Disc Type changed to 'Fairway Driver'",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        4, 5 -> {
                            if (discTypeDropdown.selectedItemPosition != 2) {
                                discTypeDropdown.setSelection(2)
                                Toast.makeText(
                                    applicationContext,
                                    "Disc Type changed to 'Midrange'",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        1, 2, 3 -> {
                            if (discTypeDropdown.selectedItemPosition != 3) {
                                discTypeDropdown.setSelection(3)
                                Toast.makeText(
                                    applicationContext,
                                    "Disc Type changed to 'Putter'",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
}