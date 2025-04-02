package ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adolfosalado.practicavn.R
import com.adolfosalado.practicavn.databinding.FragmentFacturasBinding

class FacturasFragment : Fragment() {
    private lateinit var binding: FragmentFacturasBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFacturasBinding.inflate(inflater, container, false)

        settingsToolbar(binding.toolbarFragmentFacturas)

        return binding.root
    }

    fun settingsToolbar(toolbar: androidx.appcompat.widget.Toolbar) {
        toolbar.title = "Facturas"
        toolbar.setNavigationIcon(R.drawable.ic_launcher_foreground)
        toolbar.navigationIcon?.setTint(resources.getColor(R.color.white))
        toolbar.inflateMenu(R.menu.filter_invoice_menu)
        toolbar.setNavigationOnClickListener {
            (activity as AppCompatActivity).onBackPressed()
        }
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
}