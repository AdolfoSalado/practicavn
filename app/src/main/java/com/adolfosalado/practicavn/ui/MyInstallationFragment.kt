package com.adolfosalado.practicavn.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adolfosalado.practicavn.R
import com.adolfosalado.practicavn.databinding.FragmentMyInstallationBinding

class MyInstallationFragment : Fragment() {
    private lateinit var binding: FragmentMyInstallationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyInstallationBinding.inflate(inflater, container, false)

        fillInFieldValues()

        return binding.root
    }

    private fun fillInFieldValues() {
        binding.tvFirstDescription.text = getString(R.string.first_description)
        binding.tvSecondDescription.text = getString(R.string.autoconsumo)
        binding.tvNumericValue.text = getString(R.string.numeric_value)
        binding.ivGraph.setImageResource(R.drawable.grafico1)
    }
}