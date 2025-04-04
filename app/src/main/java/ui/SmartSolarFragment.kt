package ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adolfosalado.practicavn.R
import com.adolfosalado.practicavn.databinding.FragmentSmartSolarBinding
import com.google.android.material.tabs.TabLayoutMediator
import ui.adapters.ViewPagerAdapter

class SmartSolarFragment : Fragment() {
    private lateinit var binding: FragmentSmartSolarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSmartSolarBinding.inflate(inflater, container, false)

        settingToolbar(binding.toolbarFragmentSmartSolar)

        val adapter = ViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Mi instalación"
                1 -> tab.text = "Energía"
                2 -> tab.text = "Detalles"
            }
        }.attach()

        return binding.root
    }

    private fun settingToolbar(toolbar: androidx.appcompat.widget.Toolbar) {

        setTitle(toolbar)

        val btnBack = toolbar.findViewById<LinearLayout>(R.id.llBack)
        btnBack.setOnClickListener {
            (activity as AppCompatActivity).onBackPressed()
        }


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

    private fun setTitle(toolbar: androidx.appcompat.widget.Toolbar) {
        val title = toolbar.findViewById<TextView>(R.id.tvTittle)
        title.text = "Smart Solar"
    }
}