package com.example.zubrilkaenglish.screens.catalogCards

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.zubrilkaenglish.screens.catalogCards.fragments.searchCardFragment.SearchCardFragment
import com.example.zubrilkaenglish.screens.catalogCards.fragments.topicsCardsFragment.TopicsCardsFragment
import com.example.zubrilkaenglish.screens.catalogCards.fragments.userCardsFragment.UserCardsFragment

class ViewPager2Adapter(parentFragment: CatalogCardsFragment,
                        private val viewModel: CatalogCardsViewModel
): FragmentStateAdapter(parentFragment) {

    private val fragmentList = mutableListOf<Fragment>()

    init {
        fragmentList.add(TopicsCardsFragment(viewModel))
        fragmentList.add(UserCardsFragment(viewModel))
    }
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addSearchFragment(){
        fragmentList.add(SearchCardFragment(viewModel))
        notifyDataSetChanged()
    }
    fun removeSearchFragment(){
        fragmentList.removeLast()
        notifyDataSetChanged()
    }
}