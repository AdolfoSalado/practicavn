package com.adolfosalado.practicavn.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adolfosalado.practicavn.data.models.Invoice
import com.adolfosalado.practicavn.databinding.InvoiceItemBinding


class InvoiceAdapter(private var invoices: List<Invoice>) :
    RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder>() {

    class InvoiceViewHolder(private val binding: InvoiceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(invoice: Invoice) {
            binding.tvStatus.text = invoice.descEstado
            binding.tvAmount.text = invoice.importeOrdenacion.toString()
            binding.tvDate.text = invoice.fecha
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        val binding = InvoiceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InvoiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        holder.bind(invoices[position])
    }

    override fun getItemCount() = invoices.size

    fun updateInvoices(newInvoices: List<Invoice>) {
        invoices = newInvoices
        notifyDataSetChanged()
    }
}