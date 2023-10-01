package com.example.zubrilkaenglish.screens.catalogCards

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.zubrilkaenglish.screens.catalogCards.fragments.TopicsCardsFragment

class ViewPager2Adapter(parentFragment: CatalogCardsFragment): FragmentStateAdapter(parentFragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                TopicsCardsFragment()
            }

            1 -> {
                TopicsCardsFragment()
            }

            else ->{
                TopicsCardsFragment()
            }
        }
    }
}