package com.purabmodi.payment.utility

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.purabmodi.payment.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class SwipeGesture(context: Context) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    private val deleteColor = ContextCompat.getColor(context, R.color.expense_color)
    private val deleteIcon = R.drawable.ic_delete

    private val editColor = ContextCompat.getColor(context, R.color.income_color)
    private val editIcon = R.drawable.ic_edit

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeLeftActionIcon(deleteIcon)
            .addSwipeLeftLabel("Delete")
            .addSwipeRightBackgroundColor(editColor)
            .addSwipeRightActionIcon(editIcon)
            .addSwipeRightLabel("Edit")
            .create()
            .decorate()


        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}