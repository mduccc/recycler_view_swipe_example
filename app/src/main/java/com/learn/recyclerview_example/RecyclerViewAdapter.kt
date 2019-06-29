package com.learn.recyclerview_example

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.item_layout.view.*

class RecyclerViewAdapter(private val context: Context, private val listContacts: ArrayList<ContactCollection>, private val onClickListener: OnClickListener)
    : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.item_layout, p0, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listContacts.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        val view = p0.itemView
        val contact = listContacts[p1]

        view.apply {
            name.text = contact.name
            phone_number.text = contact.phoneNumber
            email.text = contact.email
        }

        view.setOnClickListener {
            onClickListener.onItemClick(p1)
        }

        view.setOnLongClickListener {
            onClickListener.onItemLongClick(p1)
            true
        }

        // Animation
        val animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        view.startAnimation(animation)
    }

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}