package ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.adolfosalado.domain.models.Factura
import com.adolfosalado.practicavn.R
import com.adolfosalado.practicavn.databinding.FragmentFacturasBinding
import ui.adapters.InvoiceAdapter


class FacturasFragment : Fragment() {
    private lateinit var binding: FragmentFacturasBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFacturasBinding.inflate(inflater, container, false)

        settingsToolbar(binding.toolbarFragmentFacturas)

        val adapter = InvoiceAdapter(getInvoicesList())
        binding.rvFacturas.layoutManager = LinearLayoutManager(context)
        binding.rvFacturas.adapter = adapter

        return binding.root
    }

    fun settingsToolbar(toolbar: androidx.appcompat.widget.Toolbar) {

        setTitle(toolbar)

        val title = toolbar.findViewById<TextView>(R.id.toolbar_back_text)
        title.text = "Consumo"

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

    fun getInvoicesList(): List<Factura> {
        return listOf(
            Factura(1, "31 Ago 2020", 54.56, "Pendiente de pago"),
            Factura(2, "31 Ago 2020", 54.56, "Pendiente de pago"),
            Factura(3, "31 Ago 2020", 54.56, "Pendiente de pago"),
            Factura(4, "31 Ago 2020", 54.56, "Pendiente de pago"),
            Factura(5, "31 Ago 2020", 54.56, "Pendiente de pago"),
            Factura(6, "31 Ago 2020", 54.56, "Pendiente de pago"),
            Factura(7, "31 Ago 2020", 54.56, "Pendiente de pago"),
            Factura(8, "31 Ago 2020", 54.56, "Pendiente de pago"),
            Factura(9, "31 Ago 2020", 54.56, "Pendiente de pago"),
            Factura(10, "31 Ago 2020", 54.56, "Pendiente de pago")
        )
    }

    @SuppressLint("SetTextI18n")
    private fun setTitle(toolbar: androidx.appcompat.widget.Toolbar) {
        val title = toolbar.findViewById<TextView>(R.id.tvTittle)
        title.text = "Facturas"
    }
}