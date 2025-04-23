package com.adolfosalado.practicavn.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
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
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale
import kotlin.text.get

@AndroidEntryPoint
class InvoicesFilter : AppCompatActivity() {
    private lateinit var binding: ActivityInvoicesFilterBinding
    private val viewModel: InvoiceFilterViewModel by viewModels()

    private var dateFrom: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoicesFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTheme(R.style.Theme_Practicavn)

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
            showDatePickerDialog(
                initialDate = dateFrom,
                isFrom = true,
                onDateSet = { year, month, day ->
                    val calendar = Calendar.getInstance().apply {
                        set(year, month, day, 0, 0, 0)
                        set(Calendar.MILLISECOND, 999)
                    }
                    viewModel.setDateFrom(calendar.timeInMillis)
                    dateFrom = calendar.timeInMillis
                }
            )
        }

        binding.inputFechaHasta.setOnClickListener {
            showDatePickerDialog(
                initialDate = viewModel.dateTo.value,
                isFrom = false,
                minDate = dateFrom,
                onDateSet = { year, month, day ->
                    val calendar = Calendar.getInstance().apply {
                        set(year, month, day, 23, 59, 59)
                        set(Calendar.MILLISECOND, 999)
                    }
                    viewModel.setDateTo(calendar.timeInMillis)
                }
            )
        }

        binding.sliderImporte.addOnChangeListener { slider, value, fromUser ->
            viewModel.setAmountSelected(value.toDouble())
            binding.textRangoImporte.text = String.format(Locale.getDefault(), "%.2f €", value)
            slider.setTrackActiveTintList(getColorStateList(R.color.green))

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

        binding.sliderImporte.setCustomThumbDrawable(R.drawable.thumb)
    }

    private fun observeViewModel() {
        viewModel.maxAmount.observe(this) { max ->
            val currentAmount = viewModel.amount.value ?: 0.0

            binding.sliderImporte.valueFrom = 0f
            binding.sliderImporte.valueTo = max.toFloat()
            binding.textViewMaxValue.text = String.format(Locale.getDefault(), "%.2f €", max)

            // Establece el valor del slider solo si está dentro del rango
            if (currentAmount <= max) {
                binding.sliderImporte.value = currentAmount.toFloat()
                binding.textRangoImporte.text =
                    String.format(Locale.getDefault(), "%.2f €", currentAmount)
            } else {
                binding.sliderImporte.value = max.toFloat()
                binding.textRangoImporte.text = String.format(Locale.getDefault(), "%.2f €", max)
            }
        }

        viewModel.dateFrom.observe(this) { dateMillis ->
            binding.inputFechaDesde.setText(formatDateToString(dateMillis))
            dateFrom = dateMillis // Guarda el valor de dateFrom cuando vuelve al filtro
        }

        viewModel.dateTo.observe(this) { dateMillis ->
            binding.inputFechaHasta.setText(formatDateToString(dateMillis))
        }

        viewModel.statusList.observe(this) { statuses ->
            binding.layoutEstados.removeAllViews()
            statuses.forEach { status ->
                val checkBox = CheckBox(this).apply {
                    text = status
                    isChecked = viewModel.filter.value?.statusList?.contains(status) == true
                    buttonTintList = getColorStateList(R.color.checkbox_tint_green)

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
            binding.sliderImporte.value = viewModel.amount.value?.toFloat() ?: 0f
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

    private fun showDatePickerDialog(
        initialDate: Long? = null,
        isFrom: Boolean,
        minDate: Long? = null,
        onDateSet: (year: Int, month: Int, day: Int) -> Unit
    ) {
        val calendar = Calendar.getInstance()

        //Vamos a poner la fecha del parametro, sino la actual.
        if (initialDate != null) {
            calendar.timeInMillis = initialDate
        }

        val datePickerDialog = DatePickerDialog(
            this,
            R.style.GreenDatePickerDialog,
            { _, year, month, day -> onDateSet(year, month, day) },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        //Vamos a asignar el minDate, sino es null.
        minDate?.let { datePickerDialog.datePicker.minDate = it }

        datePickerDialog.show()
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