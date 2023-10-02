package com.example.zubrilkaenglish.screens.catalogCards

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.zubrilkaenglish.screens.catalogCards.fragments.SearchCardFragment.SearchCardFragment
import com.example.zubrilkaenglish.screens.catalogCards.fragments.TopicsCardsFragment
import com.example.zubrilkaenglish.screens.catalogCards.fragments.UserCardsFragment

class ViewPager2Adapter(parentFragment: CatalogCardsFragment): FragmentStateAdapter(parentFragment) {

    private val fragmentList = mutableListOf<Fragment>()

    init {
        fragmentList.add(TopicsCardsFragment())
        fragmentList.add(UserCardsFragment())
    }
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addSearchFragment(viewModel: CatalogCardsViewModel){
        fragmentList.add(SearchCardFragment(viewModel))
        notifyDataSetChanged()
    }
    fun removeSearchFragment(){
        fragmentList.removeLast()
        notifyDataSetChanged()
    }
}