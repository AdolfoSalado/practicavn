package com.adolfosalado.practicavn.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.adolfosalado.practicavn.R
import com.adolfosalado.practicavn.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        binding.etEstado.ivPopupIcon.setOnClickListener {
            Toast.makeText(context, "Icono pulsado", Toast.LENGTH_SHORT).show()
        }

        binding.etEstado.ivPopupIcon.visibility = View.VISIBLE

        return binding.root
    }


}