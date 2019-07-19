package com.example.mvvmkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mvvmkotlin.projectListView.view.ProjectListFragment
import com.example.mvvmkotlin.utils.FragmentListener
import com.example.mvvmkotlin.utils.replaceFragmentInActivity
import com.example.mvvmkotlin.utils.setupActionBar

class MainActivity : AppCompatActivity(), FragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Intially setup the project list screen
        if(savedInstanceState == null)
            setupViewFragment()

    }

    private fun setupViewFragment() {
        supportFragmentManager.findFragmentById(R.id.fragment_container)
            ?: replaceFragmentInActivity(ProjectListFragment.newInstance(), R.id.fragment_container)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            return
        }
        super.onBackPressed()
    }

    private fun setUpActionBarTitle(titleText: String) {
        this.setupActionBar(R.id.toolbar){
            title = titleText
        }
    }

    override fun setTitle(title: String) {
        setUpActionBarTitle(title)
    }
}
