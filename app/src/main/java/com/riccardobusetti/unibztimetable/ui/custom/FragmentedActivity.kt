package com.riccardobusetti.unibztimetable.ui.custom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.ui.roomcheck.RoomCheckFragment
import kotlinx.android.synthetic.main.activity_fragmented.*

/**
 * Extension of [AppCompatActivity] which is responsible of housing a fragment
 * inside of a standard activity.
 *
 * The purpose of this class is having the ability to start new fragmented activities
 * so that we write all the UI code mainly in [Fragment] to allow for more flexibility.
 * Having all the code in fragments enables the application to mount dynamically various parts
 * of the application in any container available, thus reusing code.
 *
 * @author Riccardo Busetti
 */
class FragmentedActivity : AppCompatActivity() {

    companion object {
        const val FRAGMENT_KEY = "fragment"

        fun <T : Fragment> launch(context: Context, fragment: Class<T>) {
            Intent(context, FragmentedActivity::class.java).apply {
                putExtra(FRAGMENT_KEY, fragment.simpleName)
                context.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragmented)

        setupUI()

        intent.extras?.let { bundle ->
            bundle.getString(FRAGMENT_KEY)?.let { fragmentName ->
                instantiateFragment(fragmentName)?.let {
                    mountFragment(it)
                }
            }
        }
    }

    private fun setupUI() {
        val toolbar = activity_fragmented_toolbar
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.app_name)
        toolbar.setTitleTextAppearance(this, R.style.LogoTextAppearance)
    }

    private fun mountFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.activity_fragmented_container, fragment)
            .commit()
    }

    private fun instantiateFragment(name: String): Fragment? {
        return when (name) {
            RoomCheckFragment::class.java.simpleName -> RoomCheckFragment()
            else -> null
        }
    }
}