package com.adolfosalado.practicavn.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adolfosalado.practicavn.data.models.Invoice
import com.adolfosalado.practicavn.R
import com.adolfosalado.practicavn.data.repositories.InvoiceRepository
import com.adolfosalado.practicavn.data.viewmodels.InvoiceViewModel
import com.adolfosalado.practicavn.databinding.FragmentFacturasBinding
import com.adolfosalado.practicavn.ui.adapters.InvoiceAdapter


class FacturasFragment : Fragment() {
    private lateinit var binding: FragmentFacturasBinding
    private lateinit var viewModel: InvoiceViewModel
    private lateinit var adapter: InvoiceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFacturasBinding.inflate(inflater, container, false)

        settingsToolbar(binding.toolbarFragmentFacturas)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(InvoiceViewModel::class.java)

        binding.rvFacturas.layoutManager = LinearLayoutManager(context)

        viewModel.invoicesResponse.observe(viewLifecycleOwner) { invoicesResponse ->
            val listaFacturas = invoicesResponse.facturas

            if (listaFacturas.isNotEmpty()) {
                adapter = InvoiceAdapter(listaFacturas)
                binding.rvFacturas.adapter = adapter
            } else {
                Log.d("MiFragment", "La lista de facturas está vacía")
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            // Muestra el error en la interfaz de usuario
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }

        viewModel.getInvoices()
    }

    fun settingsToolbar(toolbar: Toolbar) {
        setToolbarTitle(toolbar)
        setToolbarBackTitle(toolbar)

        val btnBack = toolbar.findViewById<LinearLayout>(R.id.llBack)
        btnBack.setOnClickListener {
            (activity as AppCompatActivity).onBackPressed()
        }

        toolbar.inflateMenu(R.menu.filter_invoice_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_next -> {
                    Toast.makeText(context, "Atrás", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> {}
            }
            false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setToolbarTitle(toolbar: Toolbar) {
        val title = toolbar.findViewById<TextView>(R.id.tvTittle)
        title.text = getString(R.string.facturas)
    }

    private fun setToolbarBackTitle(toolbar: Toolbar) {
        val title = toolbar.findViewById<TextView>(R.id.toolbarBackText)
        title.text = getString(R.string.consumo)
    }
}