package com.adolfosalado.practicavn.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
           mostrarPopupEstadoAutoconsumo(requireContext())
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

    fun mostrarPopupEstadoAutoconsumo(context: Context) {
        val builder = AlertDialog.Builder(context, R.style.CustomAlertDialogStyle)
            .setTitle("Estado solicitud autoconsumo")
            .setMessage("El tiempo estimado de activación de tu\nautoconsumo es de 1 a 2 meses, éste\nvariará en función de tu comunidad\nautónoma y distribuidora")
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss() // Cierra el diálogo al hacer clic en "Aceptar"
                // Aquí puedes añadir cualquier otra acción que quieras realizar al aceptar
            }
            .setCancelable(false) // Evita que el diálogo se cierre al tocar fuera de él o al pulsar el botón de retroceso

        val alertDialog = builder.create()
        alertDialog.show()

        // Opcional: Personalizar el color del botón "Aceptar" (requiere acceder al botón después de mostrarlo)
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(context.getColor(R.color.green))
    }

}