package com.ebcf.jnab.ui.talk

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.ebcf.jnab.R
import com.ebcf.jnab.domain.model.SpeakerModel
import com.ebcf.jnab.domain.model.SymposiumModel
import com.ebcf.jnab.databinding.FragmentTalksFilterBottomSheetBinding
import com.ebcf.jnab.ui.speaker.list.SpeakersListViewModel
import com.ebcf.jnab.ui.symposium.SymposiumsListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


class TalksFilterBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentTalksFilterBottomSheetBinding? =null
    private val binding get() = _binding!!

    private val talksViewModel: TalksListViewModel by activityViewModels()
    private lateinit var speakersViewModel: SpeakersListViewModel
    private lateinit var symposiumsViewModel: SymposiumsListViewModel


    private var selectedDate: LocalDate? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTalksFilterBottomSheetBinding.inflate(inflater, container, false)
        val root: View = binding.root

        speakersViewModel = ViewModelProvider(requireActivity()).get(SpeakersListViewModel::class.java)
        symposiumsViewModel = ViewModelProvider(requireActivity()).get(SymposiumsListViewModel::class.java)

        setupDatePicker()
        setupSpinners()
        setupButtons()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.select_date))
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            selectedDate = Instant.ofEpochMilli(selection)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            binding.fechaTextView.text = selectedDate.toString()
        }

        binding.fechaTextView.setOnClickListener {
            datePicker.show(parentFragmentManager, "DATE_PICKER")
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupSpinners() {
        // Symposium Spinner
        symposiumsViewModel.symposiums.observe(viewLifecycleOwner) { symposiums ->
            val adapter = SymposiumSpinnerAdapter(
                requireContext(),
                symposiums
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            binding.symposiumSpinner.adapter = adapter
        }

        // Speaker Spinner
        speakersViewModel.speakers.observe(viewLifecycleOwner) { speakers ->
            val adapter = SpeakerSpinnerAdapter(
                requireContext(),
                speakers
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            binding.authorSpinner.adapter = adapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupButtons() {
        binding.applyButton.setOnClickListener {
            val selectedSymposium = binding.symposiumSpinner.selectedItem as? SymposiumModel
            val selectedSpeaker = binding.authorSpinner.selectedItem as? SpeakerModel

            talksViewModel.applyFilters(
                symposiumId = selectedSymposium?.id,
                speakerId = selectedSpeaker?.id,
                date = selectedDate
            )
            dismiss()
        }

        binding.clearButton.setOnClickListener {
            selectedDate = null
            binding.fechaTextView.text = ""
            binding.symposiumSpinner.setSelection(0)
            binding.authorSpinner.setSelection(0)
            talksViewModel.clearFilters()
            dismiss()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroyView() {
        super.onDestroyView()
        //talksViewModel.clearFilters()
        _binding = null
    }

    companion object {
        const val TAG = "TalksFilterBottomSheet"
    }
}


class SymposiumSpinnerAdapter(
    context: Context,
    private val symposiums: List<SymposiumModel>
) : ArrayAdapter<SymposiumModel>(context, android.R.layout.simple_spinner_item, symposiums) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        view.text = symposiums[position].title
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        view.text = symposiums[position].title
        return view
    }
}

class SpeakerSpinnerAdapter(
    context: Context,
    private val speakers: List<SpeakerModel>
) : ArrayAdapter<SpeakerModel>(context, android.R.layout.simple_spinner_item, speakers) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        view.text = "${speakers[position].firstName} ${speakers[position].lastName}"
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        view.text = "${speakers[position].firstName} ${speakers[position].lastName}"
        return view
    }
}