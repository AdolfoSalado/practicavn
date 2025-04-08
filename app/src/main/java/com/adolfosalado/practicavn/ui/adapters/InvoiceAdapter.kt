package com.adolfosalado.practicavn.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adolfosalado.practicavn.data.models.Invoice
import com.adolfosalado.practicavn.R

class InvoiceAdapter(private val invoices: List<Invoice>) :
    RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder>() {

    class InvoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)

        fun bind(invoice: Invoice) {
            tvDate.text = invoice.date
            tvStatus.text = invoice.status
            tvAmount.text = invoice.amount.toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InvoiceViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.invoice_item, parent, false)

        return InvoiceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val invoice = invoices[position]
        holder.bind(invoice)
    }

    override fun getItemCount(): Int = invoices.size
}
