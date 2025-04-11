package com.adolfosalado.practicavn.ui

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
import com.adolfosalado.practicavn.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    val detailsViewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        detailsViewModel.details.observe(viewLifecycleOwner) { detail ->
            binding.etCau.etCustom.setText(detail.cau)
            binding.etEstado.etCustom.setText(detail.estadoAlta)
            binding.etTipo.etCustom.setText(detail.tipoAutoconsumo)
            binding.etCompensacion.etCustom.setText(detail.compensacion)
            binding.etPotencia.etCustom.setText(detail.potencia)

        }

        binding.etEstado.ivPopupIcon.setOnClickListener {
            Toast.makeText(context, "Icono pulsado", Toast.LENGTH_SHORT).show()
        }

        binding.etEstado.ivPopupIcon.visibility = View.VISIBLE

        detailsViewModel.getDetails()

        return binding.root
    }


}