package com.example.mvvmkotlin

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.mvvmkotlin.utils.FragmentListener

abstract class BaseFragment : Fragment(){

    var fragmentListener : FragmentListener? = null


    override fun onResume() {
        super.onResume()
        if (fragmentListener != null) {
            val title = getTitle()
            fragmentListener?.setTitle(title)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        addBackStackListener()
        if (context is FragmentListener) {
            fragmentListener = context
        } else {
            throw RuntimeException("$context must implement FragmentListener")
        }
    }


    override fun onDetach() {
        super.onDetach()
        fragmentListener = null
    }

    private fun addBackStackListener(){
        (activity as MainActivity).supportFragmentManager.addOnBackStackChangedListener {
            setTitle(getTitle())
        }
    }
    /**
     * Gets title.
     *
     * @return the title
     */
    protected abstract fun getTitle(): String

    /**
     * Sets title.
     *
     * @param title the title
     */
    fun setTitle(title: String) {
        fragmentListener?.setTitle(title)
    }
}