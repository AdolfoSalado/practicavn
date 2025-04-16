package com.adolfosalado.practicavn.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adolfosalado.practicavn.R
import com.adolfosalado.practicavn.data.models.Invoice
import com.adolfosalado.practicavn.databinding.InvoiceItemBinding


class InvoiceAdapter(private var invoices: List<Invoice>) :
    RecyclerView.Adapter<InvoiceAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: InvoiceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(invoice: Invoice) {
            binding.tvDate.text = invoice.date
            binding.tvAmount.text = invoice.amount.toString()
            binding.tvStatus.text = invoice.status

            if (invoice.status == "Pendiente de pago") {
                binding.tvStatus.setTextColor(binding.root.context.getColor(R.color.red))
                binding.tvStatus.visibility = View.VISIBLE

            } else {
                binding.tvStatus.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = InvoiceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(invoices[position])
    }

    override fun getItemCount(): Int = invoices.size

    fun updateInvoices(newInvoices: List<Invoice>) {
        this.invoices = newInvoices
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado
    }

    fun orderInvoicesByStatus() {
        invoices = invoices.sortedBy { it.status }
    }
}