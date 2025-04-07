package com.adolfosalado.practicavn.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.adolfosalado.practicavn.ui.DetailsFragment
import com.adolfosalado.practicavn.ui.EnergyFragment
import com.adolfosalado.practicavn.ui.MyInstallationFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyInstallationFragment()
            1 -> EnergyFragment()
            2 -> DetailsFragment()
            else -> MyInstallationFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }

}