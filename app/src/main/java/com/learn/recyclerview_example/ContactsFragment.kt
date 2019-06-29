package com.learn.recyclerview_example

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.item_layout.*

class ContactsFragment : Fragment() {

    private val listContacts = ArrayList<ContactCollection>()
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private var deleteButtonVisible = false
    private var posSwiped = -1
    private var lastSwipe = -1
    private var moving = false

    private val onClickListener = object : OnClickListener {
        override fun onItemClick(position: Int) {
            requireActivity().runOnUiThread {
                Toast.makeText(requireActivity(), "Clicked on " + listContacts[position].name, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        override fun onItemLongClick(position: Int) {
            requireActivity().runOnUiThread {
                Toast.makeText(requireActivity(), "Long clicked on " + listContacts[position].name, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private val swipeController = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START) {
        override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
            moving = true
            return false
        }

        override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
            moving = false
            Log.d("Moving", "$moving")
            val position = p0.layoutPosition

            // Close item swiped before
            if (lastSwipe != -1 && lastSwipe != position)
                recyclerViewAdapter.notifyItemChanged(lastSwipe)

            lastSwipe = position
            deleteButtonVisible = true
            Log.d("Button Visible", deleteButtonVisible.toString())
        }

        override fun onChildDraw(
            c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
            dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
        ) {

            posSwiped = viewHolder.adapterPosition

            Log.d("dX", dX.toString())
            Log.d("Item postion", posSwiped.toString())
            val view = viewHolder.itemView

            val paint = Paint()
            paint.color = resources.getColor(R.color.colorAccent)
            paint.textSize = 30f
            paint.isAntiAlias = true

            // Fix position for button
            val deleteButtonLeft = view.right - (view.right / 5f)
            val deleteButtonTop = view.top.toFloat()
            val deleteButtonRight = view.right.toFloat() - view.paddingRight
            val deleteButtonBottom = view.bottom.toFloat()

            Log.d("Delete Button Left X", deleteButtonLeft.toString())

            // Draw a button
            val radius = 15f
            val deleteButtonDelete = RectF(deleteButtonLeft, deleteButtonTop, deleteButtonRight, deleteButtonBottom)
            c.drawRoundRect(deleteButtonDelete, radius, radius, paint)

            // Set color for draw text inside button
            paint.color = resources.getColor(R.color.colorWhite)

            // Button text
            val textButton = "Delete"

            // Get width, height of button text
            val rect = Rect()
            paint.getTextBounds(textButton, 0, textButton.length, rect)

            c.drawText(
                "Delete",
                deleteButtonDelete.centerX() - rect.width() / 2f,
                deleteButtonDelete.centerY() + rect.height() / 2f,
                paint
            )

            // dX of item run from 0 to `-X` width of screen

            if (dX <= - deleteButtonLeft) {
                deleteButtonVisible = true
                moving = false
            } else
            {
                deleteButtonVisible = false
                moving = true
            }

            if (dX == 0.0f)
                moving = false

            Log.d("Moving", "$moving")
            Log.d("Button Visible", deleteButtonVisible.toString())

            // Check button ís visible
            if (deleteButtonVisible)
                clickDeleteButtonListener(recyclerView, viewHolder, posSwiped)

            // Item will stop in dX / 5,
            super.onChildDraw(c, recyclerView, viewHolder, dX / 5f, dY, actionState, isCurrentlyActive)
        }

        // Swipe back (start, end, top, down)
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            return makeMovementFlags(0, ItemTouchHelper.START)
        }

    }

    private fun clickDeleteButtonListener(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, posSwiped: Int) {
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(posSwiped)
        val item = viewHolder?.itemView

        item?.let {
            recyclerView.setOnTouchListener { v, event ->
                Log.d("X click", event.x.toString())
                Log.d("X item end", "${item.x + item.width}")
                Log.d("Y click", event.y.toString())
                Log.d("Y item start", item.y.toString())
                Log.d("Y item end", "${item.y + item.height}")
                Log.d("Button Visible", deleteButtonVisible.toString())

                if(event.action == MotionEvent.ACTION_UP && event.y > item.y && event.y < item.y + item.height
                    && event.x > item.x + item.width && !moving){
                    if (deleteButtonVisible) {
                        Toast.makeText(requireContext(), "Click to Button Delete ($posSwiped)", Toast.LENGTH_SHORT).show()
                        deleteItem(posSwiped)
                        deleteButtonVisible = false
                    }
                }
                false
            }
        }
    }


    private fun deleteItem(position: Int) {
        if (position < listContacts.size) {
            listContacts.removeAt(position)
            recyclerViewAdapter.notifyDataSetChanged()
        }
    }

    init {
        listContacts.add(ContactCollection("Duc", "0376460778", "minhduc@gmail.com"))
        listContacts.add(ContactCollection("Tung", "0576460887", "tung@gmail.com"))
        listContacts.add(ContactCollection("Kien", "0676760779", "kien@gmail.com"))
        listContacts.add(ContactCollection("Tam", "0376870799", "tam@gmail.com"))
        listContacts.add(ContactCollection("Hanh", "0376760676", "hanh@gmail.com"))
        listContacts.add(ContactCollection("Anh", "0376490747", "anh@gmail.com"))
        listContacts.add(ContactCollection("Binh", "0676560797", "biinh@gmail.com"))
        listContacts.add(ContactCollection("Tuan Anh", "0576460777", "tuananh@gmail.com"))
        listContacts.add(ContactCollection("Thao", "057646078", "thao@gmail.com"))
        listContacts.add(ContactCollection("Phuong", "0376460798", "phuong@gmail.com"))
        listContacts.add(ContactCollection("Hien", "0376460756", "hien@gmail.com"))
        listContacts.add(ContactCollection("Huong", "0376460745", "huong@gmail.com"))
        listContacts.add(ContactCollection("Mai", "0376460711", "mai@gmail.com"))
        listContacts.add(ContactCollection("Lan", "0376460734", "lan@gmail.com"))
        listContacts.add(ContactCollection("Hong", "0376460700", "hong@gmail.com"))

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewAdapter = RecyclerViewAdapter(requireContext(), listContacts, onClickListener)

        recycler_view.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        //val divider_primary = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        val divider = object : RecyclerView.ItemDecoration() {

            private val layoutPrimary = resources.getDrawable(R.drawable.divider_primary)

            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)

                val start = info.left
                val end = parent.width - item_layout.paddingRight

                for (i in 0 until parent.childCount) {
                    val itemView = parent.getChildAt(i)

                    val top = itemView.bottom + layoutPrimary.intrinsicHeight * 2
                    val bottom = itemView.bottom + layoutPrimary.intrinsicHeight * 3

                    layoutPrimary.setBounds(start, top, end, bottom)
                    layoutPrimary.draw(c)
                }
            }
        }

        // draw custom divider under a item
        recycler_view.addItemDecoration(divider)
        recycler_view.adapter = recyclerViewAdapter

        ItemTouchHelper(swipeController).attachToRecyclerView(recycler_view)
    }
}
