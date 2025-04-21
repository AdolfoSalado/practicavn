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
import kotlin.getValue

class InvoicesFragment : Fragment() {
    private lateinit var binding: FragmentFacturasBinding
    private lateinit var adapter: InvoiceAdapter
    private var getFilter: InvoiceFilter = InvoiceFilter()
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
                getFilter = it
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
        viewModel.invoicesLiveData.observe(viewLifecycleOwner) { invoices ->
            updateRecyclerView(invoices)
            if (invoices.isEmpty()) {
                binding.rvFacturas.visibility = View.GONE
                binding.emptyTextView.visibility = View.VISIBLE
            } else {
                binding.rvFacturas.visibility = View.VISIBLE
                binding.emptyTextView.visibility = View.GONE
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
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
                R.id.action_menu -> {
                    val intent = Intent(requireContext(), InvoicesFilter::class.java)

                    if (getFilter != null) {
                        intent.putExtra("filter", getFilter)
                    }
                    filterLauncher.launch(intent)
                    true
                }

                else -> false
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.rvFacturas.visibility = if (isLoading) View.GONE else View.VISIBLE
            binding.emptyTextView.visibility = View.GONE
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
