package com.adolfosalado.practicavn.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.adolfosalado.practicavn.data.models.InvoiceFilter
import com.adolfosalado.practicavn.data.viewmodels.InvoiceFilterViewModel
import com.adolfosalado.practicavn.data.viewmodels.InvoiceViewModel
import com.adolfosalado.practicavn.databinding.ActivityInvoicesFilterBinding
import java.util.Calendar

class InvoicesFilter : AppCompatActivity() {
    private lateinit var binding: ActivityInvoicesFilterBinding
    private val filterViewModel: InvoiceFilterViewModel by viewModels()
    private val viewModel: InvoiceViewModel by viewModels()

    private var dateFromMillis: Long? = null
    private var dateToMillis: Long? = null
    private var statusList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoicesFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()

        binding.btnAplicarFiltros.setOnClickListener {
            val filter = InvoiceFilter(
                dateFrom = dateFromMillis,
                dateTo = dateToMillis,
                amount = filterViewModel.amountSelected.value,
                statusList = statusList
            )

            Log.d("FILTER_ACTIVITY", "Filtro aplicado: $filter")
            viewModel.filterLiveData.value = filter
            finish()
        }

    }


    private fun setupListeners() {
        binding.inputFechaDesde.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    val date = Calendar.getInstance()
                    date.set(year, month, day)
                    dateFromMillis = date.timeInMillis
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.inputFechaHasta.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    val date = Calendar.getInstance()
                    date.set(year, month, day)
                    dateToMillis = date.timeInMillis
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        filterViewModel.statusList.observe(this) { statuses ->
            statuses.forEach { status ->
                val checkbox = CheckBox(this).apply {
                    text = status
                    setOnClickListener {
                        if (isChecked) statusList.add(status) else statusList.remove(status)
                    }
                }
                binding.layoutEstados.addView(checkbox)
            }
        }

        filterViewModel.amountRange.observe(this) { maxAmount ->
            binding.textRangoImporte.text = (maxAmount.toString())
            //binding.sliderImporte. = maxAmount.toInt()

        }
        filterViewModel.amountSelected.observe(this) { amount ->
            binding.textRangoImporte.text = amount.toString()
        }
    }
}
