package ai.elimu.launcher.ui

import ai.elimu.model.v2.gson.application.ApplicationGson
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(
    activity: AppCompatActivity,
    private val apps: MutableList<ApplicationGson>) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return PlaceholderFragment.newInstance(position + 1, apps)
    }

    override fun getItemCount(): Int {
        return 4
    }
}