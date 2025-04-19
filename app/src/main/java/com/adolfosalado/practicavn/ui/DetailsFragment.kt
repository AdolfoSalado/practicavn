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
        observeViewModel()

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

    private fun observeViewModel() {
        detailsViewModel.details.observe(viewLifecycleOwner) { detail ->
            binding.etCau.etCustom.setText(detail.cau)
            binding.etEstado.etCustom.setText(detail.estadoAlta)
            binding.etTipo.etCustom.setText(detail.tipoAutoconsumo)
            binding.etCompensacion.etCustom.setText(detail.compensacion)
            binding.etPotencia.etCustom.setText(detail.potencia)

            // Ocultar ProgressBar y mostrar los EditTexts cuando los datos están disponibles
            binding.loadingProgressBar.visibility = View.GONE
            binding.etCau.root.visibility = View.VISIBLE
            binding.etEstado.root.visibility = View.VISIBLE
            binding.etTipo.root.visibility = View.VISIBLE
            binding.etCompensacion.root.visibility = View.VISIBLE
            binding.etPotencia.root.visibility = View.VISIBLE
        }

        detailsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            // También podrías controlar la visibilidad de los EditTexts aquí si lo prefieres
            binding.etCau.root.visibility = if (isLoading) View.GONE else View.VISIBLE
            binding.etEstado.root.visibility = if (isLoading) View.GONE else View.VISIBLE
            binding.etTipo.root.visibility = if (isLoading) View.GONE else View.VISIBLE
            binding.etCompensacion.root.visibility = if (isLoading) View.GONE else View.VISIBLE
            binding.etPotencia.root.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        detailsViewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            binding.loadingProgressBar.visibility =
                View.GONE // Ocultar la ProgressBar en caso de error
        }
    }

    fun mostrarPopupEstadoAutoconsumoPersonalizado(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_alert_dialog, null)

        val builder = AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(false)

        val alertDialog = builder.create()
        alertDialog.show()

        val tituloTextView =
            view.findViewById<TextView>(R.id.tvPopupTitle)
        val mensajeTextView =
            view.findViewById<TextView>(R.id.tvPopupMessage)
        val botonAceptar =
            view.findViewById<Button>(R.id.btnPopupAccept)

        tituloTextView.text = "Estado solicitud autoconsumo"
        mensajeTextView.text =
            "El tiempo estimado de activación de tu autoconsumo es de 1 a 2 meses, éste variará en función de tu comunidad autónoma y distribuidora"

        botonAceptar.setOnClickListener {
            alertDialog.dismiss()
        }
    }


}