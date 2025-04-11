package com.adolfosalado.practicavn.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.CheckBox
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.adolfosalado.practicavn.R
import com.adolfosalado.practicavn.data.viewmodels.InvoiceFilterViewModel
import com.adolfosalado.practicavn.databinding.ActivityInvoicesFilterBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.collections.get
import kotlin.math.max
import kotlin.math.min

class InvoicesFilter : AppCompatActivity() {
    private lateinit var binding: ActivityInvoicesFilterBinding
    private lateinit var invoiceFilterViewModel: InvoiceFilterViewModel
    var dateFromMillis: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInvoicesFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        datePickerSettings()
        sliderSettings()
        checkboxSettings()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun datePickerSettings() {
        binding.inputFechaDesde.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val date = Calendar.getInstance()
                    date.set(year, month, dayOfMonth)

                    dateFromMillis = date.timeInMillis // guardamos fecha "desde"

                    val fechaFormateada =
                        String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                    binding.inputFechaDesde.setText(fechaFormateada)

                    // (Opcional) Borrar la fecha "hasta" si ya no es válida
                    val fechaHastaTexto = binding.inputFechaHasta.text.toString()
                    if (fechaHastaTexto.isNotEmpty()) {
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val fechaHasta = sdf.parse(fechaHastaTexto)
                        if (fechaHasta != null && fechaHasta.before(date.time)) {
                            binding.inputFechaHasta.setText("") // reseteamos
                        }
                    }

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePicker.show()
        }

        binding.inputFechaHasta.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val fechaFormateada =
                        String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                    binding.inputFechaHasta.setText(fechaFormateada)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // Si ya se seleccionó fechaDesde, se usa como mínima
            dateFromMillis?.let {
                datePicker.datePicker.minDate = it
            }

            datePicker.show()
        }
    }


    private fun sliderSettings() {
        invoiceFilterViewModel = ViewModelProvider(this)[InvoiceFilterViewModel::class.java]

        invoiceFilterViewModel.amountRange.observe(this) { maxValue ->
            binding.sliderImporte.valueFrom = 0f
            binding.sliderImporte.valueTo = maxValue

        }
        invoiceFilterViewModel.amountSelected.observe(this) { amount ->
            // Set the selected value. Get min and max.
            val max = binding.sliderImporte.valueTo
            val min = binding.sliderImporte.valueFrom
            // If amount is not within min and max, set the slider to min or max.
            val value = max(min, min(max, amount))

            // Set the value
            binding.sliderImporte.value = value
            binding.textRangoImporte.text = String.format("%.2f €", value)
        }

        // Handle changes on the slider
        binding.sliderImporte.addOnChangeListener { slider, value, fromUser ->
            invoiceFilterViewModel.setAmountSelected(value)
        }

        invoiceFilterViewModel.loadAmountRange()
    }


    // Establece los estados dinámicamente según el estado de las facturas
    private fun checkboxSettings() {
        invoiceFilterViewModel = ViewModelProvider(this)[InvoiceFilterViewModel::class.java]
        lifecycleScope.launch {
            invoiceFilterViewModel.statusList.observe(this@InvoicesFilter) { statusList ->
                val checkboxes = statusList.map { status ->
                    val checkbox = CheckBox(this@InvoicesFilter)
                    checkbox.text = status
                    checkbox
                }

                binding.layoutEstados.removeAllViews()
                checkboxes.forEach { checkbox ->
                    binding.layoutEstados.addView(checkbox)
                }
            }
        }
    }
}

