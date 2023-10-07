package com.example.zubrilkaenglish.screens.catalogCards

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.zubrilkaenglish.screens.catalogCards.fragments.searchCardFragment.SearchCardFragment

class ViewPager2Adapter(parentFragment: CatalogCardsFragment,
                        private val viewModel: CatalogCardsViewModel
): FragmentStateAdapter(parentFragment) {

    private var fragmentList = mutableListOf<Fragment>()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addSearchFragment(searchCardFragment: SearchCardFragment){
        fragmentList.add(searchCardFragment)
        notifyDataSetChanged()
    }
    fun removeSearchFragment(){
        fragmentList.removeLast()
        notifyDataSetChanged()
    }

    fun setList(list:MutableList<Fragment>){
        fragmentList=  list
        notifyDataSetChanged()
    }
    fun getFragment(position: Int):  Fragment{
        return fragmentList[position]
    }
}