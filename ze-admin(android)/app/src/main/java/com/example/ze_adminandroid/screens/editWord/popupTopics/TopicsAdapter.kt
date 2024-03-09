package com.example.ze_adminandroid.screens.editWord.popupTopics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ze_adminandroid.R
import com.example.ze_adminandroid.databinding.ViewTopicBinding

class TopicsAdapter(
    private val onClickTopic: (String) -> Unit
) : RecyclerView.Adapter<TopicsAdapter.TopicHolder>() {

    private var listTopics= emptyList<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_topic,parent,false)
        return TopicHolder(view)
    }

    override fun getItemCount(): Int = listTopics.size

    override fun onBindViewHolder(holder: TopicHolder, position: Int) {
        holder.bind(listTopics[position], onClickTopic)
    }

    fun setList(list: List<String>){
        this.listTopics = list
        notifyDataSetChanged()
    }


    class TopicHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding: ViewTopicBinding = ViewTopicBinding.bind(view)
        fun bind(
            topicName: String,
            onClickTopic: (String) -> Unit
        ) {
            binding.topic.setText(topicName)

            binding.root.setOnClickListener {
                onClickTopic(topicName)
            }

        }
    }
}