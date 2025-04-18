package com.adolfosalado.practicavn.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.adolfosalado.practicavn.R
import com.adolfosalado.practicavn.data.models.InvoiceFilter
import com.adolfosalado.practicavn.data.viewmodels.InvoiceFilterViewModel
import com.adolfosalado.practicavn.databinding.ActivityInvoicesFilterBinding
import java.text.SimpleDateFormat
import java.util.*

class InvoicesFilter : AppCompatActivity() {
    private lateinit var binding: ActivityInvoicesFilterBinding
    private val viewModel: InvoiceFilterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoicesFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val existingFilter = intent.getParcelableExtra<InvoiceFilter>("filter")
        existingFilter?.let {
            viewModel.setFilter(it)
        }

        setupListeners()
        setupToolbar()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.inputFechaDesde.setOnClickListener {
            showDatePickerDialog { year, month, day ->
                val calendar = Calendar.getInstance().apply {
                    set(year, month, day, 0, 0, 0)
                }
                viewModel.setDateFrom(calendar.timeInMillis)
            }
        }

        binding.inputFechaHasta.setOnClickListener {
            showDatePickerDialog { year, month, day ->
                val calendar = Calendar.getInstance().apply {
                    set(year, month, day, 23, 59, 59)
                }
                viewModel.setDateTo(calendar.timeInMillis)
            }
        }

        binding.sliderImporte.addOnChangeListener { slider, value, fromUser ->
            viewModel.setAmountSelected(value.toDouble())
            binding.textRangoImporte.text = "${value.toInt()} €"
        }

        binding.btnAplicarFiltros.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("filter", viewModel.filter.value)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        binding.btnDeleteFilters.setOnClickListener {
            viewModel.setFilter(InvoiceFilter())
        }
    }

    private fun observeViewModel() {
        viewModel.maxAmount.observe(this) { max ->
            binding.sliderImporte.valueFrom = 0f
            binding.sliderImporte.valueTo = max.toFloat()
            viewModel.amount.value?.let {
                if (it <= max) {
                    binding.sliderImporte.value = it.toFloat()
                }
            }
            binding.textRangoImporte.text = getString(R.string.range_amount, 0, max.toInt())
        }

        viewModel.dateFrom.observe(this) { dateMillis ->
            binding.inputFechaDesde.setText(formatDateToString(dateMillis))
        }

        viewModel.dateTo.observe(this) { dateMillis ->
            binding.inputFechaHasta.setText(formatDateToString(dateMillis))
        }

        viewModel.amount.observe(this) { amount ->
            if (amount <= binding.sliderImporte.valueTo) {
                binding.sliderImporte.value = amount.toFloat()
                binding.textRangoImporte.text = "${amount.toInt()} €"
            }
        }

        viewModel.statusList.observe(this) { statuses ->
            binding.layoutEstados.removeAllViews()
            statuses.forEach { status ->
                val checkBox = CheckBox(this).apply {
                    text = status
                    isChecked = viewModel.filter.value?.statusList?.contains(status) == true
                    setOnCheckedChangeListener { _, isChecked ->
                        val currentStatuses =
                            viewModel.statusesSelected.value.orEmpty().toMutableList()
                        if (isChecked) {
                            currentStatuses.add(text.toString())
                        } else {
                            currentStatuses.remove(text.toString())
                        }
                        viewModel.setStatusesSelected(currentStatuses)
                    }
                }
                binding.layoutEstados.addView(checkBox)
            }
        }

        viewModel.statusesSelected.observe(this) { selectedStatuses ->
            binding.layoutEstados.children
                .filterIsInstance<CheckBox>()
                .forEach { it.isChecked = selectedStatuses.contains(it.text.toString()) }
        }

        viewModel.filter.observe(this) { filter ->
            // Este observador asegura que si el filtro se establece externamente (e.g., al recibir un filtro existente),
            // la UI se actualice completamente. Los observadores individuales de dateFrom, dateTo, amount y statusesSelected
            // también contribuyen a mantener la UI sincronizada.
        }
    }

    private fun setupToolbar() {
        setToolbarTitle(binding.filterToolbar, getString(R.string.filter_invoices))

        val btnBack = binding.filterToolbar.findViewById<View>(R.id.llBack)
        btnBack.visibility = View.INVISIBLE

        binding.filterToolbar.inflateMenu(R.menu.filter_invoice_menu)
        binding.filterToolbar.menu.findItem(R.id.action_menu).isVisible = true
        binding.filterToolbar.menu.findItem(R.id.action_menu).setIcon(R.drawable.close_icon)
        binding.filterToolbar.menu.findItem(R.id.action_menu).setOnMenuItemClickListener {
            finish()
            true
        }
    }

    private fun showDatePickerDialog(onDateSet: (year: Int, month: Int, day: Int) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day -> onDateSet(year, month, day) },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun formatDateToString(dateMillis: Long?): String {
        return dateMillis?.let {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.format(Date(it))
        } ?: ""
    }

    fun AppCompatActivity.setToolbarTitle(
        toolbar: androidx.appcompat.widget.Toolbar,
        title: String
    ) {
        val titleTextView = toolbar.findViewById<TextView>(R.id.tvTittle)
        titleTextView?.text = title
    }
}