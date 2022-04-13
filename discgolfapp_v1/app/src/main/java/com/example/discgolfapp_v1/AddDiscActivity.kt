package com.example.discgolfapp_v1

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.discgolfapp_v1.ui.main.DiscInfo
import com.example.discgolfapp_v1.ui.main.VirtualBagData


class AddDiscActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_disc)

        val speedEditText = findViewById<EditText>(R.id.disc_speed_input)
        speedEditText.onFocusChangeListener = discSpeedFocusChangeListener
    }
/*
    private val nameEditText = findViewById<EditText>(R.id.disc_name_input)
    private val colorView = findViewById<View>(R.id.disc_color_preview)
    private val speedEditText = findViewById<EditText>(R.id.disc_speed_input)
    private val glideEditText = findViewById<EditText>(R.id.disc_glide_input)
    private val turnEditText = findViewById<EditText>(R.id.disc_turn_input)
    private val fadeEditText = findViewById<EditText>(R.id.disc_fade_input)
    private val typeSpinner = findViewById<Spinner>(R.id.disc_type_input)
    private val weightEditText = findViewById<EditText>(R.id.disc_weight_input)
    private val manufacturerEditText = findViewById<EditText>(R.id.disc_manufacturer_input)
    private val plasticEditText = findViewById<EditText>(R.id.disc_plastic_input)
    private val notesEditText = findViewById<EditText>(R.id.disc_additional_notes_input)
*/
    private val discSpeedFocusChangeListener =
        OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                val discSpeedInputText = (view as EditText).text.toString()
                val typeSpinner = findViewById<Spinner>(R.id.disc_type_input)

                if (discSpeedInputText != "") {
                    when (discSpeedInputText.toInt()) {
                        9, 10, 11, 12, 13, 14 -> {
                            if (typeSpinner.selectedItemPosition != 0) {
                                typeSpinner.setSelection(0)
                                Toast.makeText(
                                    applicationContext,
                                    "Disc Type changed to 'Distance Driver'",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        6, 7, 8 -> {
                            if (typeSpinner.selectedItemPosition != 1) {
                                typeSpinner.setSelection(1)
                                Toast.makeText(
                                    applicationContext,
                                    "Disc Type changed to 'Fairway Driver'",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        4, 5 -> {
                            if (typeSpinner.selectedItemPosition != 2) {
                                typeSpinner.setSelection(2)
                                Toast.makeText(
                                    applicationContext,
                                    "Disc Type changed to 'Midrange'",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        1, 2, 3 -> {
                            if (typeSpinner.selectedItemPosition != 3) {
                                typeSpinner.setSelection(3)
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

    private var colorIndex = 0
    private val colors = intArrayOf(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN)

    fun changeDiscColor(view: View) {
        val colorView = findViewById<View>(R.id.disc_color_preview)
        colorIndex = (colorIndex + 1) % 5
        colorView.setBackgroundColor(colors[colorIndex])
    }

    fun saveDisc(view: View) {
        val nameEditText = findViewById<EditText>(R.id.disc_name_input)

        if (nameEditText.text.toString() == "") {
            warnRequiredFields()
            return
        }

        val colorView = findViewById<View>(R.id.disc_color_preview)
        val speedEditText = findViewById<EditText>(R.id.disc_speed_input)
        val glideEditText = findViewById<EditText>(R.id.disc_glide_input)
        val turnEditText = findViewById<EditText>(R.id.disc_turn_input)
        val fadeEditText = findViewById<EditText>(R.id.disc_fade_input)
        val typeSpinner = findViewById<Spinner>(R.id.disc_type_input)
        val weightEditText = findViewById<EditText>(R.id.disc_weight_input)
        val manufacturerEditText = findViewById<EditText>(R.id.disc_manufacturer_input)
        val plasticEditText = findViewById<EditText>(R.id.disc_plastic_input)
        val notesEditText = findViewById<EditText>(R.id.disc_additional_notes_input)

        val flightNums = Array<Int?>(4) {null}
        var noFlightNum = true

        if (speedEditText.text.toString() != "") {
            flightNums[0] = speedEditText.text.toString().toInt()
            noFlightNum = false
        }
        if (glideEditText.text.toString() != "") {
            flightNums[1] = glideEditText.text.toString().toInt()
            noFlightNum = false
        }
        if (turnEditText.text.toString() != "") {
            flightNums[2] = turnEditText.text.toString().toInt()
            noFlightNum = false
        }
        if (fadeEditText.text.toString() != "") {
            flightNums[3] = fadeEditText.text.toString().toInt()
            noFlightNum = false
        }

        var discId: Int = 0

        when (typeSpinner.selectedItemPosition) {
            0 -> {
                discId = VirtualBagData.distanceDrivers.size
            }
            1 -> {
                discId = 1000 + VirtualBagData.fairwayDrivers.size
            }
            2 -> {
                discId = 2000 + VirtualBagData.midranges.size
            }
            3 -> {
                discId = 3000 + VirtualBagData.putters.size
            }
        }

        val discInfo = DiscInfo(discId,
                                nameEditText.text.toString(),
                                (colorView.background as ColorDrawable).color,
                                typeSpinner.selectedItemPosition,
                                null, // Add image file
                                if (noFlightNum) null else flightNums,
                                if (weightEditText.text.toString() == "") null else weightEditText.text.toString().toInt(),
                                if (manufacturerEditText.text.toString() == "") null else manufacturerEditText.text.toString(),
                                if (plasticEditText.text.toString() == "") null else plasticEditText.text.toString(),
                                if (notesEditText.text.toString() == "") null else notesEditText.text.toString()
            )

        when (typeSpinner.selectedItemPosition) {
            0 -> {
                VirtualBagData.distanceDrivers.add(discInfo)
            }
            1 -> {
                VirtualBagData.fairwayDrivers.add(discInfo)
            }
            2 -> {
                VirtualBagData.midranges.add(discInfo)
            }
            3 -> {
                VirtualBagData.putters.add(discInfo)
            }
        }

        val intent = Intent()
        intent.putExtra("discId", discId)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun warnRequiredFields() {
        val namePrompt = findViewById<TextView>(R.id.disc_name_prompt)
        val nameEditText = findViewById<EditText>(R.id.disc_name_input)
        val scrollView = findViewById<ScrollView>(R.id.disc_scroll)

        scrollView.scrollTo(0, namePrompt.top)
        nameEditText.requestFocus()
        Toast.makeText(
            applicationContext,
            "Name field required",
            Toast.LENGTH_SHORT
        ).show()
    }
}