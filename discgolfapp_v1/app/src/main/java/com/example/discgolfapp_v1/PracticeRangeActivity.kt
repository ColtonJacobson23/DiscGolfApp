package com.example.discgolfapp_v1

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.discgolfapp_v1.ui.main.DiscInfo
import com.example.discgolfapp_v1.ui.main.ThrowInfo
import com.example.discgolfapp_v1.ui.main.ThrowListAdapter
import kotlin.math.*
import com.example.discgolfapp_v1.ui.main.VirtualBagData

class PracticeRangeActivity : AppCompatActivity(), LocationListener, PopupWindow.OnDismissListener {
    private lateinit var locationManager: LocationManager
    private lateinit var saveThrowView: View
    private lateinit var popupWindow: PopupWindow
    private var startLocation: Location? = null
    private var currentDist: Int? = null
    private val locationPermissionCode = 2
    private val throws = ArrayList<ThrowInfo>()
    private val discIds: ArrayList<Int>
        get() {
            val discs = ArrayList<Int>()

            for (d in VirtualBagData.distanceDrivers) {
                discs.add(d.id)
            }

            for (d in VirtualBagData.fairwayDrivers) {
                discs.add(d.id)
            }

            for (d in VirtualBagData.midranges) {
                discs.add(d.id)
            }

            for (d in VirtualBagData.putters) {
                discs.add(d.id)
            }

            return discs
        }
    private val discNames: ArrayList<String>
        get() {
            val discs = ArrayList<String>()

            for (d in VirtualBagData.distanceDrivers) {
                discs.add("${d.name} (DD)")
            }

            for (d in VirtualBagData.fairwayDrivers) {
                discs.add("${d.name} (FD)")
            }

            for (d in VirtualBagData.midranges) {
                discs.add("${d.name} (M)")
            }

            for (d in VirtualBagData.putters) {
                discs.add("${d.name} (P)")
            }

            return discs
        }
    private val getNewDisc = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val discId = result.data?.getIntExtra("discId", -1)

            val throwTypeSpinner = saveThrowView.findViewById<Spinner>(R.id.select_throw_type_spinner)
            val throwFlightSpinner = saveThrowView.findViewById<Spinner>(R.id.select_throw_flight_spinner)
            val notesEditText = saveThrowView.findViewById<EditText>(R.id.throw_notes_input)

            var discDisplayName = ""
            for (i in discIds.indices) {
                if (discIds[i] == discId) {
                    discDisplayName = discNames[i]
                }
            }

            val throwType = resources.getStringArray(R.array.throw_types)[throwTypeSpinner.selectedItemPosition]
            val throwFlight = resources.getStringArray(R.array.throw_flights)[throwFlightSpinner.selectedItemPosition]

            val throwInfo = ThrowInfo(
                discId!!,
                discDisplayName,
                throwType,
                throwFlight,
                if (notesEditText.text.toString() == "") null else notesEditText.text.toString(),
                currentDist!!
            )

            throws.add(throwInfo)

            val throwListView = findViewById<ListView>(R.id.throw_list)
            val throwAdapter = ThrowListAdapter(this, throws)
            throwListView.adapter = throwAdapter

            popupWindow.dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_range)

        val throwListView = findViewById<ListView>(R.id.throw_list)
        val throwAdapter = ThrowListAdapter(this, throws)
        throwListView.adapter = throwAdapter

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
    }

    fun startDistanceTracking(view: View) {
        val startDistButton = findViewById<Button>(R.id.start_location_button)
        val distDisplayTextView = findViewById<TextView>(R.id.distance_display)

        if (startLocation == null) {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                currentDist = 0

                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    1f,
                    this
                )

                distDisplayTextView.text = getString(R.string.distance_display_start)
                distDisplayTextView.setTextColor(getColor(R.color.green_200))
                startDistButton.text = getString(R.string.unset_start_location_button)
            }
        } else {
            locationManager.removeUpdates(this)

            startLocation = null
            currentDist = null

            distDisplayTextView.text = getString(R.string.distance_display_default)
            distDisplayTextView.setTextColor(getColor(R.color.red))
            startDistButton.text = getString(R.string.set_start_location_button)
        }
    }

    fun saveThrow(view: View) {
        if (currentDist != null) {
            locationManager.removeUpdates(this)
            popupSaveThrow()
        } else {
            Toast.makeText(this, "Start Location Not Set", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onLocationChanged(location: Location) {
        if (startLocation == null) {
            startLocation = location
        } else {
            val distDisplayTextView = findViewById<TextView>(R.id.distance_display)
            currentDist = distanceInFt(startLocation?.latitude, startLocation?.longitude, location.latitude, location.longitude)
            distDisplayTextView.text = "$currentDist ft"
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onDismiss() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                1f,
                this
            )
        }
    }

    private fun distanceInFt(lat1: Double?, lon1: Double?, lat2: Double, lon2: Double): Int {
        var dist = 0.0

        if (lat1 != null && lon1 != null) {
            dist = sin(degToRad(lat1)) * sin(degToRad(lat2))
            dist += cos(degToRad(lat1)) * cos(degToRad(lat2)) * cos(degToRad(lon2 - lon1))
            dist = acos(dist) * 3963.0 * 5280.0
        }

        return dist.roundToInt()
    }

    private fun degToRad(deg: Double): Double {
        return deg * PI / 180
    }

    private fun popupSaveThrow() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        saveThrowView = inflater.inflate(R.layout.popup_save_throw, null)
        popupWindow = PopupWindow(
            saveThrowView,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        popupWindow.isFocusable = true
        popupWindow.setOnDismissListener(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.elevation = 10.0f
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val slideIn = Slide()
            slideIn.slideEdge = Gravity.TOP
            popupWindow.enterTransition = slideIn

            val slideOut = Slide()
            slideOut.slideEdge = Gravity.TOP
            popupWindow.exitTransition = slideOut
        }

        val discSpinner = saveThrowView.findViewById<Spinner>(R.id.select_disc_spinner)
        val arrayList = arrayListOf("SELECT DISC", "NEW DISC")
        arrayList += discNames

        val arrayAdapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayList)
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        discSpinner.adapter = arrayAdapter

        val saveThrowButton = saveThrowView.findViewById<Button>(R.id.save_throw_button)

        saveThrowButton.setOnClickListener {
            val throwTypeSpinner = saveThrowView.findViewById<Spinner>(R.id.select_throw_type_spinner)
            val throwFlightSpinner = saveThrowView.findViewById<Spinner>(R.id.select_throw_flight_spinner)
            val notesEditText = saveThrowView.findViewById<EditText>(R.id.throw_notes_input)

            when (discSpinner.selectedItemPosition) {
                0 -> Toast.makeText(applicationContext, "Please Select A Disc", Toast.LENGTH_SHORT).show()
                1 -> getNewDisc.launch(Intent(this, AddDiscActivity::class.java))
                2 -> {
                    val discId = discIds[discSpinner.selectedItemPosition - 2]
                    val discDisplayName = discNames[discSpinner.selectedItemPosition - 2]
                    val throwType = resources.getStringArray(R.array.throw_types)[throwTypeSpinner.selectedItemPosition]
                    val throwFlight = resources.getStringArray(R.array.throw_flights)[throwFlightSpinner.selectedItemPosition]

                    val throwInfo = ThrowInfo(
                        discId,
                        discDisplayName,
                        throwType,
                        throwFlight,
                        if (notesEditText.text.toString() == "") null else notesEditText.text.toString(),
                        currentDist!!
                    )

                    throws.add(throwInfo)

                    val throwListView = findViewById<ListView>(R.id.throw_list)
                    val throwAdapter = ThrowListAdapter(this, throws)
                    throwListView.adapter = throwAdapter

                    popupWindow.dismiss()
                }
            }
        }

        val dismissPopupButton = saveThrowView.findViewById<Button>(R.id.save_throw_dismiss_button)

        dismissPopupButton.setOnClickListener {
            popupWindow.dismiss()
        }

        val rootLayout = findViewById<ConstraintLayout>(R.id.practice_range_layout)

        TransitionManager.beginDelayedTransition(rootLayout)
        popupWindow.showAtLocation(
            rootLayout, // Location to display popup window
            Gravity.CENTER, // Exact position of layout to display popup
            0, // X offset
            0 // Y offset
        )
    }
}