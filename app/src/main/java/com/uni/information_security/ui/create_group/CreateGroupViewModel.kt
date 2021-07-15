package com.uni.information_security.ui.create_group

import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.uni.information_security.data.DataManager
import com.uni.information_security.model.response.chat.Group
import com.uni.information_security.model.response.chat.User
import com.uni.information_security.model.response.chat.UserInGroup
import com.uni.information_security.utils.*
import com.uni.information_security.view_model.BaseViewModel
import javax.inject.Inject

class CreateGroupViewModel : BaseViewModel() {
    @Inject
    lateinit var dataManager: DataManager

    private val database = FirebaseDatabase.getInstance().reference

    val createGroupResponse = MutableLiveData<Boolean>()
    fun createGroup(
        groupName: String?,
        avatar: String?,
        userList: MutableMap<String?, UserInGroup?>
    ) {
        onRetrievePostListStart()
        val groupID = database.push().key
        val newGroup = Group(groupID, groupName, avatar, userList, mutableMapOf())
        database.child(GROUP_PATH).child(groupID!!).setValue(newGroup).addOnSuccessListener {
            createGroupResponse.postValue(true)
            onRetrievePostListFinish()
        }
            .addOnCanceledListener {
                createGroupResponse.postValue(false)
                onRetrievePostListFinish()
            }
    }


    val userAddResponse = MutableLiveData<List<User?>>()
    val userChangeResponse = MutableLiveData<User?>()
    val userRemovedResponse = MutableLiveData<User?>()
    private val userList = mutableListOf<User?>()
    fun getUsers() {
        onRetrievePostListStart()
        database.child(USER_PATH).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue<User>()
                if (user?.id != USER_DATA?.id) {
                    userList.add(user)
                    userAddResponse.postValue(userList)
                    onRetrievePostListFinish()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue<User>()
                if (user?.id != USER_DATA?.id)
                    userChangeResponse.postValue(user)
                onRetrievePostListFinish()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val user = snapshot.getValue<User>()
                if (user?.id != USER_DATA?.id)
                    userRemovedResponse.postValue(user)
                onRetrievePostListFinish()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                onRetrievePostListFinish()
            }

            override fun onCancelled(error: DatabaseError) {
                onRetrievePostListFinish()
            }

        })
    }

}