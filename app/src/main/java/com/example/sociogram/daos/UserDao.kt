package com.example.sociogram.daos

import com.example.sociogram.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {
    var db = FirebaseFirestore.getInstance()
    var userscollection = db.collection("users")

    fun adduser(user: User?){
        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                userscollection.document(user.uid).set(it)
            }

        }
    }

    fun getUserById(uid : String) : Task<DocumentSnapshot>{
        return userscollection.document(uid).get()
    }
}