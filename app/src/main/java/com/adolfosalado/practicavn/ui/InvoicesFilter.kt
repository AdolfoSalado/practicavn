package com.adolfosalado.practicavn.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.adolfosalado.practicavn.data.models.InvoiceFilter
import com.adolfosalado.practicavn.data.viewmodels.InvoiceFilterViewModel
import com.adolfosalado.practicavn.databinding.ActivityInvoicesFilterBinding
import java.text.SimpleDateFormat
import java.util.*

class InvoicesFilter : AppCompatActivity() {
    private lateinit var binding: ActivityInvoicesFilterBinding
    private val filterViewModel: InvoiceFilterViewModel by viewModels()

    private var dateFromMillis: Long? = null
    private var dateToMillis: Long? = null

    private val statusCheckboxes = mutableListOf<CheckBox>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoicesFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()

        // Observando el LiveData del filtro para mantener los valores cuando regresamos a esta Activity
        filterViewModel.filter.observe(this) { filter ->
            filter?.let {
                // Actualiza las fechas desde y hasta
                dateFromMillis = it.dateFrom
                dateToMillis = it.dateTo

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                // Si la fecha de "Desde" no es nula, actualiza el campo correspondiente
                it.dateFrom?.let { millis ->
                    binding.inputFechaDesde.setText(dateFormat.format(Date(millis)))
                }

                // Si la fecha de "Hasta" no es nula, actualiza el campo correspondiente
                it.dateTo?.let { millis ->
                    binding.inputFechaHasta.setText(dateFormat.format(Date(millis)))
                }

                // Actualiza el valor del slider (importe)
                binding.sliderImporte.value = it.amount?.toFloat() ?: 0f
                binding.textRangoImporte.text =
                    "${binding.sliderImporte.value.toInt()} € - ${binding.sliderImporte.valueTo.toInt()} €"

                // Marca los checkboxes correspondientes de los estados
                binding.layoutEstados.children.forEach { checkbox ->
                    if (checkbox is CheckBox) {
                        checkbox.isChecked = it.statusList.contains(checkbox.text.toString())
                    }
                }
            } ?: run {
                // Si 'filter' es nulo, puedes proporcionar valores por defecto o mostrar un mensaje de error
                Log.e("InvoicesFilter", "El filtro está vacío o es nulo")
            }
        }

        // Botón de aplicar filtros
        binding.btnAplicarFiltros.setOnClickListener {

            val selectedAmount = binding.sliderImporte.value.toDouble()

            val selectedStatuses = statusCheckboxes
                .filter { it.isChecked }
                .map { it.text.toString() }

            val filter = InvoiceFilter(
                dateFrom = dateFromMillis,
                dateTo = dateToMillis,
                amount = if (selectedAmount > 0) selectedAmount else null,
                statusList = selectedStatuses
            )
            val resultIntent = Intent()
            resultIntent.putExtra("filter", filter)
            setResult(RESULT_OK, resultIntent)
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
                    date.set(year, month, day, 23, 59, 59)
                    dateFromMillis = date.timeInMillis
                    val formattedDate = formatDateToString(dateFromMillis) // Convertir a String
                    binding.inputFechaDesde.setText(formattedDate) // Mostrar en el UI
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
                    date.set(year, month, day, 23, 59, 59)
                    dateToMillis = date.timeInMillis
                    val formattedDate = formatDateToString(dateToMillis)
                    binding.inputFechaHasta.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Estados dinámicos
        filterViewModel.statusList.observe(this) { statuses ->
            binding.layoutEstados.removeAllViews()
            statusCheckboxes.clear()

            statuses.forEach { status ->
                val checkbox = CheckBox(this).apply {
                    text = status
                }
                statusCheckboxes.add(checkbox)
                binding.layoutEstados.addView(checkbox)
            }
        }

        filterViewModel.amountRange.observe(this) { maxAmount ->
            binding.sliderImporte.valueTo = maxAmount.toFloat() // Ajusta el valor máximo
            binding.textRangoImporte.text =
                "${binding.sliderImporte.value.toInt()} € - ${binding.sliderImporte.valueTo.toInt()} €"
        }

        filterViewModel.amountSelected.observe(this) { amount ->
            binding.textRangoImporte.text = amount.toString()
        }
    }


    // Función para convertir Long (fecha en milisegundos) a String con formato dd/MM/yyyy
    private fun formatDateToString(dateInMillis: Long?): String? {
        if (dateInMillis == null) return null
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = Date(dateInMillis)
        return sdf.format(date)
    }
}


