package com.riccardobusetti.unibztimetable.ui.roomcheck

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.RawRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.domain.entities.app.AppSection
import com.riccardobusetti.unibztimetable.domain.entities.availability.RoomAvailability
import com.riccardobusetti.unibztimetable.ui.custom.BaseBottomSheetDialogFragment
import com.riccardobusetti.unibztimetable.ui.items.AvailableItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_room_check.*

class RoomCheckFragment : BaseBottomSheetDialogFragment<RoomCheckViewModel>() {

    enum class CheckStatus(@RawRes val resId: Int? = null, val loop: Boolean? = null) {
        SEARCH(R.raw.search, true),
        LOADING(R.raw.loading, true),
        ERROR(R.raw.sad_face, true),
        LOADED_WITH_AVAILABILITIES,
        LOADED_WITH_NO_AVAILABILITIES(R.raw.sad_face, true),
        LOADED_WITH_FULL_DAY_AVAILABLE(R.raw.green_check, false),
    }

    override val appSection: AppSection
        get() = AppSection.ROOM_CHECK

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    private lateinit var searchEditText: TextInputEditText
    private lateinit var searchResultText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var statusView: LottieAnimationView

    override fun initViewModel(): RoomCheckViewModel {
        return ViewModelProviders.of(
            this,
            RoomCheckViewModelFactory()
        ).get(RoomCheckViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.AppTheme)
        return inflater.cloneInContext(contextThemeWrapper).inflate(R.layout.fragment_room_check, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }

    override fun setupUI() {
        searchEditText = fragment_room_check_room_text_input_edit_text
        searchEditText.setOnKeyListener { _, _, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                searchEditText.text?.let {
                    model?.checkRoomAvailability(it.toString())
                }
                true
            } else {
                false
            }
        }

        searchResultText = fragment_room_check_headline_2

        recyclerView = fragment_room_check_recyclerview
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = groupAdapter
            layoutAnimation = AnimationUtils.loadLayoutAnimation(
                activity,
                R.anim.layout_animation_slide_from_bottom
            )
        }

        statusView = fragment_room_check_lottie_status_view
    }

    override fun attachObservers() {
        model?.let {
            it.availability.observe(this, Observer { roomAvailability ->
                showAvailabilities(roomAvailability)
            })

            it.status.observe(this, Observer { status ->
                handleStatus(status)
            })
        }
    }

    private fun showAvailabilities(roomAvailability: RoomAvailability) {
        searchResultText.text = if (roomAvailability.isDayEmpty) {
            getString(R.string.availability_for, roomAvailability.room)
        } else {
            getString(R.string.availability_for, roomAvailability.room)
        }

        groupAdapter.clear()
        roomAvailability.availabilities.forEach {
            groupAdapter.add(AvailableItem(it))
        }
        recyclerView.scheduleLayoutAnimation()
    }

    // TODO: implement status messages.
    private fun handleStatus(status: CheckStatus) {
        if (status == CheckStatus.LOADED_WITH_AVAILABILITIES) {
            hideStatus()
        } else {
            showStatus()
            loadStatusAnimation(status)
        }
    }

    private fun showStatus() {
        searchResultText.visibility = View.GONE
        recyclerView.visibility = View.GONE
        statusView.visibility = View.VISIBLE
    }

    private fun hideStatus() {
        searchResultText.visibility = View.VISIBLE
        recyclerView.visibility = View.VISIBLE
        statusView.visibility = View.GONE
    }

    private fun loadStatusAnimation(status: CheckStatus) {
        status.resId?.let {
            statusView.setAnimation(it)
            statusView.loop(status.loop ?: false)
            statusView.playAnimation()
        }
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams
        val windowHeight = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight / 2
        }
        bottomSheet.layoutParams = layoutParams
        // behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}