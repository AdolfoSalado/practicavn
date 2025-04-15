package com.adolfosalado.practicavn.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

    // Nuevo método para actualizar los datos del adapter
    fun updateInvoices(newInvoices: List<Invoice>) {
        this.invoices = newInvoices
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado
    }
}