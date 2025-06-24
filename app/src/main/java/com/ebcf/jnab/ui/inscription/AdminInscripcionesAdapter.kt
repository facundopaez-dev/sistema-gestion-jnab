import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebcf.jnab.databinding.ItemInscripcionBinding
import com.ebcf.jnab.domain.model.InscripcionItem

class AdminInscripcionesAdapter(
    private val onAprobar: (InscripcionItem) -> Unit,
    private val onRechazar: (InscripcionItem) -> Unit,
    private val onVerComprobante: (String) -> Unit,  // recibe el base64
) : RecyclerView.Adapter<AdminInscripcionesAdapter.InscripcionViewHolder>() {

    private val items = mutableListOf<InscripcionItem>()

    fun submitList(newItems: List<InscripcionItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InscripcionViewHolder {
        val binding = ItemInscripcionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InscripcionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InscripcionViewHolder, position: Int) {
        holder.bind(items[position])

    }

    override fun getItemCount() = items.size

    inner class InscripcionViewHolder(private val binding: ItemInscripcionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: InscripcionItem) {
            binding.tvUserId.text = item.nombreCompleto
            binding.tvEstado.text = item.estado
            binding.tvFecha.text = item.fechaRegistro ?: "Sin fecha"

            val color = when (item.estado.lowercase()) {
                "aprobado" -> Color.parseColor("#4CAF50")
                "rechazado" -> Color.parseColor("#F44336")
                "pendiente" -> Color.parseColor("#FFC107")
                else -> Color.BLACK
            }

            binding.tvEstado.setTextColor(color)

            binding.btnAprobar.setOnClickListener {
                onAprobar(item)
            }

            binding.btnRechazar.setOnClickListener {
                onRechazar(item)
            }

            binding.btnVerComprobante.setOnClickListener {
                onVerComprobante(item.comprobanteBase64)
            }
        }
    }
}
