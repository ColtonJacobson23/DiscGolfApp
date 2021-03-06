package com.example.discgolfapp_v1

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.discgolfapp_v1.data.Disc
import com.example.discgolfapp_v1.data.DiscViewModel
import com.example.discgolfapp_v1.data.Throw
import com.example.discgolfapp_v1.data.ThrowViewModel
import com.example.discgolfapp_v1.ui.main.ThrowInfo
import com.example.discgolfapp_v1.ui.main.ThrowListAdapter
import kotlin.math.*
import com.example.discgolfapp_v1.ui.main.VirtualBagData
import java.lang.Exception

class PracticeRangeActivity : AppCompatActivity(), LocationListener, PopupWindow.OnDismissListener {
    private lateinit var mDiscViewModel: DiscViewModel
    private lateinit var mThrowViewModel: ThrowViewModel
    private lateinit var locationManager: LocationManager
    private lateinit var saveThrowView: View
    private lateinit var popupWindow: PopupWindow
    private var startLocation: Location? = null
    private var currentDist: Int? = null
    private val locationPermissionCode = 2
    private var popupInflated = false
    private val throws = ArrayList<ThrowInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_range)

        mDiscViewModel = ViewModelProvider(this).get(DiscViewModel::class.java)
        mDiscViewModel.readAllData.observe(this, Observer {
            if (popupInflated) {
                val throwTypeSpinner =
                    saveThrowView.findViewById<Spinner>(R.id.select_throw_type_spinner)
                val throwFlightSpinner =
                    saveThrowView.findViewById<Spinner>(R.id.select_throw_flight_spinner)
                val notesEditText = saveThrowView.findViewById<EditText>(R.id.throw_notes_input)

                var discId = -1
                var discDisplayName = ""
                if (mDiscViewModel.readAllData.value != null) {
                    for (d in mDiscViewModel.readAllData.value!!) {
                        if (d.id > discId) {
                            discId = d.id
                            when (d.type) {
                                0 -> discDisplayName = "${d.name} (DD)"
                                1 -> discDisplayName = "${d.name} (FD)"
                                2 -> discDisplayName = "${d.name} (M)"
                                3 -> discDisplayName = "${d.name} (P)"
                            }
                        }
                    }
                }

                val throwType =
                    resources.getStringArray(R.array.throw_types)[throwTypeSpinner.selectedItemPosition]
                val throwFlight =
                    resources.getStringArray(R.array.throw_flights)[throwFlightSpinner.selectedItemPosition]

                val throwInfo = ThrowInfo(
                    discId,
                    discDisplayName,
                    throwType,
                    throwFlight,
                    if (notesEditText.text.toString() == "") null else notesEditText.text.toString(),
                    currentDist!!
                )

                throws.add(throwInfo)
                popupInflated = false

                //Creates a new throw in the db
                insertDataToDatabase(
                    throwInfo.discId,
                    throwInfo.type,
                    throwInfo.flight,
                    throwInfo.notes,
                    throwInfo.distance
                )

                val throwListView = findViewById<ListView>(R.id.throw_list)
                val throwAdapter = ThrowListAdapter(this, throws)
                throwListView.adapter = throwAdapter

                popupWindow.dismiss()
            }
        })
        mThrowViewModel = ViewModelProvider(this).get(ThrowViewModel::class.java)

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) {
            try {
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                locationManager.removeUpdates(this)
            }
            catch (e: Exception) {}
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
        popupInflated = true
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

        val discIds = ArrayList<Int>()
        val discNames = ArrayList<String>()

        if (mDiscViewModel.readAllData.value != null) {
            for (d in mDiscViewModel.readAllData.value!!) {
                discIds.add(d.id)
                when (d.type) {
                    0 -> discNames.add("${d.name} (DD)")
                    1 -> discNames.add("${d.name} (FD)")
                    2 -> discNames.add("${d.name} (M)")
                    3 -> discNames.add("${d.name} (P)")
                }
            }
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
                1 -> startActivity(Intent(this, AddDiscActivity::class.java))
                else -> {
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
                    popupInflated = false

                    //Creates a new throw in the db
                    insertDataToDatabase(
                        throwInfo.discId,
                        throwInfo.type,
                        throwInfo.flight,
                        throwInfo.notes,
                        throwInfo.distance
                    )

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
            rootLayout,
            Gravity.CENTER,
            0,
            0
        )
    }

    private fun insertDataToDatabase(
        discId: Int,
        type: String,
        flight: String,
        notes: String?,
        distance: Int
    ) {
        val discThrow = Throw(0, discId, type, flight, notes, distance)
        mThrowViewModel.addThrow(discThrow)
    }
}