package com.example.sociogram.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.sociogram.R
import com.example.sociogram.daos.PostDao

class CreatePostActivity : AppCompatActivity() {
    lateinit var postbtn :Button
    lateinit var postInput : EditText
    lateinit var postDao: PostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        postbtn = findViewById(R.id.postBtn)
        postInput = findViewById(R.id.postInput)
        postDao = PostDao()

        postbtn.setOnClickListener {
            val input = postInput.text.toString().trim()
            if (input.isNotEmpty()){
                postDao.addpost(input)
                finish()
            }

        }
    }
}