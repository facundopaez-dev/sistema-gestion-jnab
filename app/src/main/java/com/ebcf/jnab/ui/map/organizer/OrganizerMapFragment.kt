package com.ebcf.jnab.ui.map.organizer

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.ebcf.jnab.R
import com.ebcf.jnab.databinding.FragmentMapOrganizerBinding
import com.ebcf.jnab.ui.map.user.MapLocation
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.material.floatingactionbutton.FloatingActionButton

class OrganizerMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapOrganizerBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap

    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private lateinit var btnMyLocation: FloatingActionButton

    // Lista para guardar los marcadores que hay en el mapa
    private val markers = mutableListOf<Marker>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapOrganizerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiKey = getMetaDataApiKey(requireContext())
        if (!Places.isInitialized() && apiKey != null) {
            Places.initialize(requireContext(), apiKey)
        }

        val autocompleteFragment = childFragmentManager
            .findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        val autocompleteView = autocompleteFragment.view
        autocompleteView?.post {
            val editText = autocompleteView.findViewById<EditText>(
                com.google.android.libraries.places.R.id.places_autocomplete_search_input
            )

            val isDarkTheme = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> true
                else -> false
            }

            if (isDarkTheme) {
                editText.setTextColor(Color.WHITE)
                editText.setHintTextColor(Color.LTGRAY)
            } else {
                editText.setTextColor(Color.BLACK)
                editText.setHintTextColor(Color.DKGRAY)
            }

            editText.setBackgroundColor(Color.TRANSPARENT)
        }

        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
        autocompleteFragment.setHint("Buscar lugar...")

        val sudoeste = LatLng(-42.80, -65.10)
        val noreste = LatLng(-42.74, -65.00)

        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(sudoeste, noreste))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val latLng = place.latLng
                if (latLng != null) {
                    val existingMarker = findMarkerNear(latLng)
                    if (existingMarker != null) {
                        // Ya existe marcador cerca, movemos cámara y mostramos info
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(existingMarker.position, 15f))
                        existingMarker.showInfoWindow()
                    } else {
                        // No hay marcador cerca, agregamos uno nuevo y guardamos
                        val newMarker = mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(place.name)
                        )
                        newMarker?.let {
                            markers.add(it)
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                            it.showInfoWindow()
                        }
                    }
                }
            }

            override fun onError(status: com.google.android.gms.common.api.Status) {
                //Toast.makeText(requireContext(), "Error: ${status.statusMessage}", Toast.LENGTH_SHORT).show()
            }
        })

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Inicializar botón flotante para centrar en ubicación actual
        btnMyLocation = binding.root.findViewById(R.id.btnMyLocation)
        btnMyLocation.setOnClickListener {
            if (isLocationPermissionGranted()) {
                centerMapOnUserLocation()
            } else {
                requestLocationPermission()
            }
        }
    }

    private fun findMarkerNear(latLng: LatLng, radiusMeters: Double = 20.0): Marker? {
        for (marker in markers) {
            val distance = FloatArray(1)
            android.location.Location.distanceBetween(
                latLng.latitude, latLng.longitude,
                marker.position.latitude, marker.position.longitude,
                distance
            )
            if (distance[0] <= radiusMeters) {
                return marker
            }
        }
        return null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val centro = LatLng(-42.7692, -65.0385)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centro, 14f))

        for (location in locations) {
            val icon = when (location.category) {
                "Restaurante" -> BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurante)
                "Hotel" -> BitmapDescriptorFactory.fromResource(R.drawable.ic_alojamiento)
                "Agencia" -> BitmapDescriptorFactory.fromResource(R.drawable.ic_agencia)
                else -> BitmapDescriptorFactory.defaultMarker()
            }

            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(location.latLng)
                    .title(location.name)
                    .snippet(location.description)
                    .icon(icon)
            )
            marker?.tag = location.category
            marker?.let {
                markers.add(it)
            }
        }

        mMap.setOnMapLongClickListener { latLng ->
            showAddMarkerDialog(latLng)
        }

        mMap.setOnInfoWindowClickListener { marker ->
            showEditMarkerDialog(marker)
        }

        if (isLocationPermissionGranted()) {
            enableUserLocation()
        } else {
            requestLocationPermission()
        }
    }
    private fun enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true

            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
            }
        }
    }

    private fun centerMapOnUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(requireContext(), "Permiso de ubicación no concedido", Toast.LENGTH_SHORT).show()
            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
            } else {
                Toast.makeText(requireContext(), "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            enableUserLocation()
            centerMapOnUserLocation()
        } else {
            Toast.makeText(requireContext(), "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAddMarkerDialog(position: LatLng) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_marker_organizer, null)
        val titleInput = dialogView.findViewById<EditText>(R.id.inputTitle)
        val descInput = dialogView.findViewById<EditText>(R.id.inputDescription)
        val spinner = dialogView.findViewById<Spinner>(R.id.spinnerType)

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.marker_types,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        AlertDialog.Builder(requireContext())
            .setTitle("Nuevo marcador")
            .setView(dialogView)
            .setPositiveButton("Agregar") { _, _ ->
                val title = titleInput.text.toString() + " "
                val desc = descInput.text.toString()
                val type = spinner.selectedItem.toString()

                val icon = when (type) {
                    "Restaurante" -> BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurante)
                    "Alojamiento" -> BitmapDescriptorFactory.fromResource(R.drawable.ic_alojamiento)
                    "Agencia" -> BitmapDescriptorFactory.fromResource(R.drawable.ic_agencia)
                    else -> BitmapDescriptorFactory.defaultMarker()
                }

                val marker = mMap.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title(title)
                        .snippet(desc)
                        .icon(icon)
                )
                marker?.tag = type
                marker?.let {
                    markers.add(it) // Guardar el nuevo marcador
                    it.showInfoWindow()
                }
                marker?.showInfoWindow()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showEditMarkerDialog(marker: Marker) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_marker_organizer, null)
        val titleInput = dialogView.findViewById<EditText>(R.id.inputTitle)
        val descInput = dialogView.findViewById<EditText>(R.id.inputDescription)
        val spinner = dialogView.findViewById<Spinner>(R.id.spinnerType)

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.marker_types,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        titleInput.setText(marker.title)
        descInput.setText(marker.snippet)
        val tipoActual = marker.tag as? String ?: "Otro"
        val positionToSelect = adapter.getPosition(tipoActual)
        spinner.setSelection(positionToSelect)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Editar marcador")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val newTitle = titleInput.text.toString()
                val newDesc = descInput.text.toString()
                val newType = spinner.selectedItem.toString()

                marker.title = newTitle
                marker.snippet = newDesc
                marker.tag = newType

                val newIcon = when (newType) {
                    "Restaurante" -> BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurante)
                    "Alojamiento" -> BitmapDescriptorFactory.fromResource(R.drawable.ic_alojamiento)
                    "Agencia" -> BitmapDescriptorFactory.fromResource(R.drawable.ic_agencia)
                    else -> BitmapDescriptorFactory.defaultMarker()
                }

                marker.setIcon(newIcon)
                marker.showInfoWindow()
            }
            .setNeutralButton("Eliminar") { _, _ ->
                marker.remove() // Elimina el marcador del mapa
                markers.remove(marker) // Elimina el marcador de la lista
                Toast.makeText(requireContext(), "Marcador eliminado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }


    fun getMetaDataApiKey(context: Context): String? {
        return try {
            val appInfo = context.packageManager
                .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            appInfo.metaData.getString("com.google.android.geo.API_KEY")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


data class MapLocation(
    val name: String,
    val description: String,
    val latLng: LatLng,
    val category: String
)

val locations = listOf(
    MapLocation("Hotel Piren", "15% de descuento", LatLng(-42.7692, -65.0385), "Hotel"),
    MapLocation("Hotel Península", "10% de descuento", LatLng(-42.7696, -65.0420), "Hotel"),
    MapLocation("Restaurante Borde Lago", "5% de descuento", LatLng(-42.7741, -65.0360), "Restaurante"),
    MapLocation("Agencia Bedeu", "15% de descuento", LatLng(-42.7675, -65.0435), "Agencia")
)
