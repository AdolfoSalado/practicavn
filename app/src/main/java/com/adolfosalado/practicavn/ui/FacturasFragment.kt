package com.adolfosalado.practicavn.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.adolfosalado.practicavn.R
import com.adolfosalado.practicavn.data.models.Invoice
import com.adolfosalado.practicavn.data.viewmodels.InvoiceViewModel
import com.adolfosalado.practicavn.databinding.FragmentFacturasBinding
import com.adolfosalado.practicavn.ui.adapters.InvoiceAdapter

class FacturasFragment : Fragment() {
    private lateinit var binding: FragmentFacturasBinding
    private lateinit var adapter: InvoiceAdapter
    private val viewModel: InvoiceViewModel by activityViewModels() // ViewModel compartido

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFacturasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        settingsToolbar(binding.toolbarFragmentFacturas)
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.rvFacturas.layoutManager = LinearLayoutManager(requireContext())
        adapter = InvoiceAdapter(emptyList())
        binding.rvFacturas.adapter = adapter
    }

    private fun updateRecyclerView(facturas: List<Invoice>) {
        if (facturas.isEmpty()) {
            Log.d("UPDATE_RECYCLERVIEW", "No hay datos para mostrar después del filtro")
            adapter.updateInvoices(emptyList())
        } else {
            adapter.updateInvoices(facturas)
        }
    }

    private fun observeViewModel() {
        // Observa los cambios en el filtro y aplica el filtro correspondiente
        viewModel.filterLiveData.observe(viewLifecycleOwner) { filter ->
            filter?.let {
                Log.d("FILTER_OBSERVED", "Observando filtro: $filter")
                viewModel.applyFilter(it) // Aplica el filtro al ViewModel
            }
        }

        // Observa las facturas filtradas y actualiza la lista
        viewModel.invoicesLiveData.observe(viewLifecycleOwner) { invoices ->
            if (invoices.isNotEmpty()) {
                Log.d(
                    "RECYCLERVIEW_UPDATE",
                    "Actualizando RecyclerView con ${invoices.size} facturas filtradas"
                )
                updateRecyclerView(invoices)
            } else {
                Log.d(
                    "RECYCLERVIEW_UPDATE",
                    "No hay facturas disponibles después de aplicar el filtro"
                )
            }
        }

        // Observa errores en el ViewModel
        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun settingsToolbar(toolbar: androidx.appcompat.widget.Toolbar) {
        setToolbarTitle(toolbar)
        setToolbarBackTitle(toolbar)

        val btnBack = toolbar.findViewById<android.widget.LinearLayout>(R.id.llBack)
        btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        toolbar.inflateMenu(R.menu.filter_invoice_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_next -> {
                    val intent = Intent(requireContext(), InvoicesFilter::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    private fun setToolbarTitle(toolbar: androidx.appcompat.widget.Toolbar) {
        val title = toolbar.findViewById<android.widget.TextView>(R.id.tvTittle)
        title.text = getString(R.string.facturas)
    }

    private fun setToolbarBackTitle(toolbar: androidx.appcompat.widget.Toolbar) {
        val title = toolbar.findViewById<android.widget.TextView>(R.id.toolbarBackText)
        title.text = getString(R.string.consumo)
    }
}
