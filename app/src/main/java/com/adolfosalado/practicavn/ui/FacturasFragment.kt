package com.adolfosalado.practicavn.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.adolfosalado.practicavn.R
import com.adolfosalado.practicavn.data.models.Invoice
import com.adolfosalado.practicavn.data.models.InvoiceFilter
import com.adolfosalado.practicavn.data.viewmodels.InvoiceViewModel
import com.adolfosalado.practicavn.databinding.FragmentFacturasBinding
import com.adolfosalado.practicavn.ui.adapters.InvoiceAdapter

class FacturasFragment : Fragment() {
    private lateinit var binding: FragmentFacturasBinding
    private lateinit var adapter: InvoiceAdapter
    private val viewModel: InvoiceViewModel by activityViewModels() // ViewModel compartido


    private val filterLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val data = result.data
            val filter = data?.getParcelableExtra<InvoiceFilter>("filter")
            filter?.let {
                Log.d("FRAGMENT_FILTER", "Aplicando filtro: $filter")
                viewModel.applyFilter(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFacturasBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        observeViewModel()
        setupRecyclerView()
        settingsToolbar(binding.toolbarFragmentFacturas)


    }

    private fun setupRecyclerView() {
        binding.rvFacturas.layoutManager = LinearLayoutManager(requireContext())
        adapter = InvoiceAdapter(emptyList())
        binding.rvFacturas.adapter = adapter
    }

    private fun observeViewModel() {
        /*viewModel.filterLiveData.observe(viewLifecycleOwner) { filter ->
            filter?.let {
                Log.d("FILTER_OBSERVED", "Observando filtro: $filter")
                viewModel.applyFilter(filter) // Aplica el filtro al ViewModel
            }
        }*/

        viewModel.invoicesLiveData.observe(viewLifecycleOwner) { invoices ->
            Log.d("OBSERVED_INVOICES", "Facturas recibidas en el Fragment: ${invoices.size}")
            invoices.forEach {
                Log.d("OBSERVED_INVOICE", "Factura: $it")
            }
            updateRecyclerView(invoices)
        }

        /*viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }*/
    }

    private fun updateRecyclerView(invoices: List<Invoice>) {
        adapter.updateInvoices(invoices)
        binding.rvFacturas.post { binding.rvFacturas.invalidate() }
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
                    viewModel.filterLiveData.value?.let {
                        intent.putExtra("filter", it)
                    }
                    filterLauncher.launch(intent)
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
