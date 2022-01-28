package com.example.sociogram.activities

import android.app.DownloadManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sociogram.R
import com.example.sociogram.adapter.IPostAdapter
import com.example.sociogram.adapter.PostAdapter
import com.example.sociogram.daos.PostDao
import com.example.sociogram.daos.UserDao
import com.example.sociogram.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.protobuf.Internal

class MainActivity : AppCompatActivity() , IPostAdapter {

    lateinit var fab :FloatingActionButton
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: PostAdapter
    lateinit var postDao: PostDao
    lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab = findViewById(R.id.fab)
        recyclerView = findViewById(R.id.recyclerView)
        postDao = PostDao()
        userDao = UserDao()

        fab.setOnClickListener {
            val intent = Intent(this,CreatePostActivity ::class.java)
            startActivity(intent)
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val postCollection = postDao.postscollection
        val query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query,Post::class.java).build()

        adapter = PostAdapter(recyclerOptions , this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.signOutBtn){
            signOut()
        }
        return super.onOptionsItemSelected(item)
    }

    fun signOut(){
        val auth = Firebase.auth
        val currentUserId = auth.currentUser!!.uid
        userDao.userscollection.document(currentUserId).delete()
        auth.signOut()
        startActivity(Intent(this,SignInActivity::class.java))
    }

}