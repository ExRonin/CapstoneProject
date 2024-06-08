package com.capstoneproject.`common-adapter`

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.data.model.TutorialPayment
import com.capstoneproject.databinding.ItemSingleTutorialBinding

class TutorialPaymentAdapter : RecyclerView.Adapter<TutorialPaymentAdapter.ViewHolder>() {

    private var listTutorial = ArrayList<TutorialPayment>()

    fun setListTutorial(newList: List<TutorialPayment>) {
        listTutorial.clear()
        listTutorial.addAll(newList)
        notifyDataSetChanged()
    }

    class ViewHolder(var binding: ItemSingleTutorialBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding =
            ItemSingleTutorialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listTutorial.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(listTutorial[position]) {
                binding.tvTitle.text = this.name
                binding.tvDescription.text = this.description
                binding.expandedView.visibility = if (this.expand) View.VISIBLE else View.GONE
                binding.cardLayout.setOnClickListener {
                    this.expand = !this.expand
                    notifyDataSetChanged()
                }
            }
        }
    }


}