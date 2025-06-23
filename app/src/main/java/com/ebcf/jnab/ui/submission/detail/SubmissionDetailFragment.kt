package com.ebcf.jnab.ui.submission.detail

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.widget.FrameLayout
import android.util.TypedValue
import android.view.*
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.core.content.ContextCompat.getColor
import com.google.android.material.snackbar.Snackbar
import java.time.format.DateTimeFormatter
import com.ebcf.jnab.databinding.FragmentSubmissionDetailBinding
import com.ebcf.jnab.R
import com.ebcf.jnab.domain.model.Submission
import com.ebcf.jnab.data.repository.SubmissionRepository
import com.ebcf.jnab.domain.model.RejectionReason
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SubmissionDetailFragment : Fragment() {

    private var _binding: FragmentSubmissionDetailBinding? = null
    private val binding get() = _binding!!

    private val args: SubmissionDetailFragmentArgs by navArgs()

    private lateinit var viewModel: SubmissionDetailViewModel
    private var currentSnackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubmissionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()

        val factory = SubmissionDetailViewModelFactory(SubmissionRepository)
        viewModel = ViewModelProvider(this, factory)[SubmissionDetailViewModel::class.java]

        viewModel.submission.observe(viewLifecycleOwner) { submission ->
            displaySubmissionDetails(submission)
            viewModel.updateShowButtons(submission.status)
        }

        // Modifica dinamicamente el color del estado de un trabajo
        viewModel.statusColorRes.observe(viewLifecycleOwner) { colorRes ->
            val color = getColor(requireContext(), colorRes)
            binding.textStatus.setTextColor(color)
        }

        // Muestra los botones "Aprobar" y "Rechazar" si el trabajo tiene el estado "Pendiente"
        viewModel.showActionButtons.observe(viewLifecycleOwner) { show ->
            val visibility = if (show) View.VISIBLE else View.GONE
            binding.buttonApprove.visibility = visibility
            binding.buttonReject.visibility = visibility
        }

        viewModel.load(args.submissionId)

        binding.buttonApprove.setOnClickListener {
            approve()
        }

        binding.buttonReject.setOnClickListener {
            showRejectionDialog()
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.baseline_keyboard_backspace_24)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displaySubmissionDetails(submission: Submission) {
        binding.textTitle.text = submission.title
        binding.textAuthor.text = submission.author
        binding.textDescription.text = submission.description
        binding.textStatus.text = submission.status.displayName
        binding.textSubmittedAt.text =
            submission.submittedAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))

        // Actualiza dinamicamente el color del estado
        viewModel.updateStatusColor(submission.status)

        // Mostrar el motivo de rechazo solo si existe
        if (!submission.rejectionReason.isNullOrBlank()) {
            binding.textRejectionReasonLabel.visibility = View.VISIBLE
            binding.textRejectionReason.visibility = View.VISIBLE
            binding.textRejectionReason.text = submission.rejectionReason
        } else {
            binding.textRejectionReasonLabel.visibility = View.GONE
            binding.textRejectionReason.visibility = View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun approve() {
        viewModel.approve(args.submissionId)

        Snackbar.make(binding.root, "Trabajo aprobado", Snackbar.LENGTH_LONG)
            .setAction("Deshacer") {
                viewModel.undoApprove(args.submissionId)
            }
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    if (event != DISMISS_EVENT_ACTION) {
                        findNavController().navigateUp()
                    }
                }
            })
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showRejectionDialog() {
        val reasons = RejectionReason.entries.toTypedArray()
        val reasonLabels = reasons.map { it.label }.toTypedArray()

        AlertDialog.Builder(requireContext())
            .setTitle("Seleccione un motivo de rechazo")
            .setItems(reasonLabels) { _, which ->
                val selectedReason = reasons[which]
                if (selectedReason == RejectionReason.OTHER) {
                    showCustomReasonDialog()
                } else {
                    rejectWithReason(selectedReason, null)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showCustomReasonDialog() {
        val maxChars = 150

        val inputLayout = TextInputLayout(requireContext()).apply {
            isErrorEnabled = true
            isCounterEnabled = true
            counterMaxLength = maxChars
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val input = TextInputEditText(requireContext()).apply {
            hint = "Escribe el motivo"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            maxLines = 4
            minLines = 2
            filters = arrayOf(InputFilter.LengthFilter(maxChars))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        inputLayout.addView(input)

        val container = FrameLayout(requireContext()).apply {
            setPadding(24.dp(), 0, 24.dp(), 0)
            addView(inputLayout)
        }

        // Limpia el error mientras se escribe
        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                inputLayout.error = null
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Otro motivo")
            .setView(container)
            .setPositiveButton("Aceptar", null)
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val customReason = input.text?.toString()?.trim() ?: ""

            when {
                customReason.isEmpty() -> {
                    inputLayout.error = "Este campo es obligatorio"
                }

                !customReason.matches(Regex("^[\\p{L}\\p{N}\\s.,:;()¿?!¡%\"'-]+$")) -> {
                    inputLayout.error = "El texto contiene caracteres no permitidos"
                }

                customReason.matches(Regex("^\\d+[\\d\\s]*$")) -> {
                    inputLayout.error = "Debe contener también texto, no solo números"
                }

                else -> {
                    inputLayout.error = null
                    dialog.dismiss()
                    rejectWithReason(RejectionReason.OTHER, customReason)
                }
            }
        }
    }

    fun Int.dp(): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
        ).toInt()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun rejectWithReason(reason: RejectionReason, customReason: String?) {
        val reasonText = customReason ?: reason.label
        viewModel.reject(args.submissionId, reasonText)

        // Muestra un Snackbar con la opcion deshacer
        currentSnackbar = Snackbar.make(binding.root, "Trabajo rechazado", Snackbar.LENGTH_LONG)
            .setAction("Deshacer") {
                viewModel.undoReject(args.submissionId)
            }
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    // Valida que el fragmento sigue asociado antes de navegar
                    if (event != DISMISS_EVENT_ACTION &&
                        isAdded &&
                        view != null &&
                        parentFragmentManager.findFragmentById(id) != null
                    ) {
                        findNavController().navigateUp()
                    }
                }
            })

        currentSnackbar?.show()
    }

    override fun onDestroyView() {
        // Cancela el Snackbar de la accion "Deshacer", que se muestra al rechazar un trabajo,
        // para evitar que su callback se dispare despues de que la vista del fragmento haya sido destruida
        currentSnackbar?.dismiss()
        _binding = null
        super.onDestroyView()
    }
}