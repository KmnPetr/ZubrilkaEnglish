package com.example.ze_adminandroid.ui.catalogWords

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * перелистывается между фрагментами
 * например межу фрагментов всех слов, изучаемых пользователем слов, и слов из поисковика
 */
class ViewPager2Adapter(parentFragment: CatalogWordsFragment): FragmentStateAdapter(parentFragment) {

    private var fragmentList = mutableListOf<Fragment>()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

//    fun addSearchFragment(searchCardFragment: SearchCardFragment){
//        fragmentList.add(searchCardFragment)
//        notifyDataSetChanged()
//    }
//    fun removeSearchFragment(){
//        fragmentList.removeLast()
//        notifyDataSetChanged()
//    }

    fun setList(list:MutableList<Fragment>){
        fragmentList =  list
        notifyDataSetChanged()
    }
    fun getFragment(position: Int):  Fragment{
        return fragmentList[position]
    }
}