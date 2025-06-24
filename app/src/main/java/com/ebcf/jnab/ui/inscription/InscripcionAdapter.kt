package com.ebcf.jnab.ui.inscription


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebcf.jnab.databinding.ItemInscripcionBinding

class InscripcionAdapter(
    private val inscripciones: List<Inscripcion>
) : RecyclerView.Adapter<InscripcionAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemInscripcionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemInscripcionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = inscripciones.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val inscripcion = inscripciones[position]
        holder.binding.tvUserId.text = inscripcion.userId
        holder.binding.tvEstado.text = inscripcion.estado.uppercase()

        // Cambiar color según estado
        when (inscripcion.estado.lowercase()) {
            "pendiente" -> holder.binding.tvEstado.setBackgroundColor(Color.YELLOW)
            "aprobado" -> holder.binding.tvEstado.setBackgroundColor(Color.GREEN)
            "rechazado" -> holder.binding.tvEstado.setBackgroundColor(Color.RED)
            else -> holder.binding.tvEstado.setBackgroundColor(Color.LTGRAY)
        }
    }
}