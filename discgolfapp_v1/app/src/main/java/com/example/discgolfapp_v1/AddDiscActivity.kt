package com.example.discgolfapp_v1

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.UserHandle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.discgolfapp_v1.data.Disc
import com.example.discgolfapp_v1.data.DiscViewModel
import com.example.discgolfapp_v1.ui.main.DiscInfo
import com.example.discgolfapp_v1.ui.main.VirtualBagData
import java.sql.Blob


class AddDiscActivity : AppCompatActivity() {

    private lateinit var mDiscViewModel: DiscViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_disc)
        mDiscViewModel = ViewModelProvider(this).get(DiscViewModel::class.java)

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

        if (speedEditText.text.toString() != "") {
            flightNums[0] = speedEditText.text.toString().toInt()
        }
        if (glideEditText.text.toString() != "") {
            flightNums[1] = glideEditText.text.toString().toInt()
        }
        if (turnEditText.text.toString() != "") {
            flightNums[2] = turnEditText.text.toString().toInt()
        }
        if (fadeEditText.text.toString() != "") {
            flightNums[3] = fadeEditText.text.toString().toInt()
        }

        //Creates a new disc in the db
        insertDataToDatabase(
            nameEditText.text.toString(),
            null, // Add image file
            (colorView.background as ColorDrawable).color,
            flightNums[0],
            flightNums[1],
            flightNums[2],
            flightNums[3],
            typeSpinner.selectedItemPosition,
            if (weightEditText.text.toString() == "") null else weightEditText.text.toString().toInt(),
            if (manufacturerEditText.text.toString() == "") null else manufacturerEditText.text.toString(),
            if (plasticEditText.text.toString() == "") null else plasticEditText.text.toString(),
            if (notesEditText.text.toString() == "") null else notesEditText.text.toString()
        )

        finish()
    }

    private fun insertDataToDatabase(
        name: String,
        photo: String?,
        color: Int,
        speed: Int?,
        glide: Int?,
        turn: Int?,
        fade: Int?,
        type: Int,
        weight: Int?,
        manufacturer: String?,
        plastic: String?,
        notes: String?
        ) {
        val disc = Disc(0, name, photo, color, speed, glide, turn, fade, type, weight, manufacturer, plastic, notes)
        mDiscViewModel.addDisc(disc)
    }

    private fun duplicateCheck(name: String, color: Int, type: Int): Boolean {
        return true
    }

    private fun inputCheck(
        name: String?,
        photo: String?,
        color: Int,
        speed: Int?,
        glide: Int?,
        turn: Int?,
        fade: Int?,
        type: Int,
        weight: Int?,
        manufacturer: String?,
        plastic: String?,
        notes: String?
    ): Boolean {
        return (
            name!!.isNotEmpty() &&
            photo!!.isNotEmpty() &&
            color != null &&
            speed != null &&
            glide != null &&
            turn != null &&
            fade != null &&
            type != null &&
            weight != null &&
            manufacturer!!.isNotEmpty() &&
            plastic!!.isNotEmpty() &&
            notes!!.isNotEmpty()
            )
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