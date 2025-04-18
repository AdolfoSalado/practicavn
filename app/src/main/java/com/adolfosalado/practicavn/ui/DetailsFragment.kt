package com.adolfosalado.practicavn.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.adolfosalado.practicavn.R
import com.adolfosalado.practicavn.data.viewmodels.DetailsViewModel
import com.adolfosalado.practicavn.databinding.CustomEditTextBinding
import com.adolfosalado.practicavn.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    val detailsViewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        setEditTextTitle()

        detailsViewModel.details.observe(viewLifecycleOwner) { detail ->
            binding.etCau.etCustom.setText(detail.cau)
            binding.etEstado.etCustom.setText(detail.estadoAlta)
            binding.etTipo.etCustom.setText(detail.tipoAutoconsumo)
            binding.etCompensacion.etCustom.setText(detail.compensacion)
            binding.etPotencia.etCustom.setText(detail.potencia)

        }

        binding.etEstado.ivPopupIcon.setOnClickListener {
            mostrarPopupEstadoAutoconsumoPersonalizado(requireContext())
        }

        binding.etEstado.ivPopupIcon.visibility = View.VISIBLE

        detailsViewModel.getDetails()

        return binding.root
    }

    private fun setEditTextTitle() {
        binding.etCau.tvTitleOfDetail.text = "CAU (Código Autoconsumo)"
        binding.etEstado.tvTitleOfDetail.text = "Estado solicitud alta autoconsumidor"
        binding.etTipo.tvTitleOfDetail.text = "Tipo autoconsumo"
        binding.etCompensacion.tvTitleOfDetail.text = "Compensación de excedentes"
        binding.etPotencia.tvTitleOfDetail.text = "Potencia de instalación"

    }

    fun mostrarPopupEstadoAutoconsumoPersonalizado(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_alert_dialog, null)

        val builder = AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(false) // O true si quieres que se pueda cancelar tocando fuera

        val alertDialog = builder.create()
        alertDialog.show()

        // Acceder a las vistas dentro de tu layout personalizado
        val tituloTextView =
            view.findViewById<TextView>(R.id.tvPopupTitle) // Asegúrate de que este ID exista en tu layout
        val mensajeTextView =
            view.findViewById<TextView>(R.id.tvPopupMessage) // Asegúrate de que este ID exista en tu layout
        val botonAceptar =
            view.findViewById<Button>(R.id.btnPopupAccept) // El primer botón en el layout

        // Opcional: Configurar el texto dinámicamente si es necesario
        tituloTextView.text = "Estado solicitud autoconsumo"
        mensajeTextView.text =
            "El tiempo estimado de activación de tu autoconsumo es de 1 a 2 meses, éste variará en función de tu comunidad autónoma y distribuidora"

        // Manejar el clic del botón "Aceptar"
        botonAceptar.setOnClickListener {
            alertDialog.dismiss()
            // Aquí puedes añadir cualquier otra acción que quieras realizar al aceptar
        }
    }


}