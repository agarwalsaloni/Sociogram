package com.example.sociogram.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sociogram.R
import com.example.sociogram.models.Post
import com.example.sociogram.utils.Utils
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PostAdapter(options: FirestoreRecyclerOptions<Post> , val listener : IPostAdapter) : FirestoreRecyclerAdapter<Post,PostAdapter.PostViewHolder>(
    options
) {

    class PostViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val usrImg : ImageView = itemView.findViewById(R.id.usrimg)
        val usrName : TextView = itemView.findViewById(R.id.usrName)
        val createdTime : TextView = itemView.findViewById(R.id.createdTime)
        val postDetail : TextView = itemView.findViewById(R.id.postDetail)
        val likeBtn : ImageView = itemView.findViewById(R.id.likeBtn)
        val likedCount : TextView = itemView.findViewById(R.id.likedCount)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val viewHolder = PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post,parent,false))
        viewHolder.likeBtn.setOnClickListener {
            listener.onLikeClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.usrName.text = model.createdBy.uname
        holder.createdTime.text = Utils.getTimeAgo(model.createdAt)
        holder.likedCount.text = model.likedBy.size.toString()
        holder.postDetail.text = model.text
        Glide.with(holder.usrImg.context).load(model.createdBy.uimg).circleCrop().into(holder.usrImg)

        val auth  = Firebase.auth
        val currentUserId = auth.currentUser!!.uid
        val isLiked = model.likedBy.contains(currentUserId)
        if(isLiked){
            holder.likeBtn.setImageDrawable(ContextCompat.getDrawable(holder.likeBtn.context,R.drawable.ic_liked))
        }else{
            holder.likeBtn.setImageDrawable(ContextCompat.getDrawable(holder.likeBtn.context,R.drawable.ic_unlike))
        }

    }
}

interface IPostAdapter{
    fun onLikeClicked(postId : String)
}