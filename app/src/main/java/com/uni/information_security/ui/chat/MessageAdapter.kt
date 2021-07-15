package com.uni.information_security.ui.chat

import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.uni.information_security.R
import com.uni.information_security.base.BaseRecyclerAdapter
import com.uni.information_security.databinding.LayoutMessageItemBinding
import com.uni.information_security.model.response.chat.Message
import com.uni.information_security.model.response.chat.User
import com.uni.information_security.utils.CommonUtils
import com.uni.information_security.utils.USER_DATA

class MessageAdapter(
    dataSet: MutableList<Message?>?,
    val isInGroup: Boolean,
    val userList: MutableList<User?>
) :
    BaseRecyclerAdapter<Message, MessageAdapter.ViewHolder>(dataSet) {
    inner class ViewHolder(val binding: LayoutMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: Message?, position: Int) {
            if (data?.idUser == USER_DATA?.id) {
                binding.cvAvatarOther.visibility = View.GONE
                binding.tvUserName.visibility = View.GONE
                binding.tvMessageOther.visibility = View.GONE
                binding.tvMessageSelf.text = data?.content
                CommonUtils.setImageFromBase64(
                    USER_DATA?.avatar,
                    binding.imvAvatarSelf,
                    binding.root.context
                )
            } else {
                binding.cvAvatarSelf.visibility = View.GONE
                binding.tvMessageSelf.visibility = View.GONE
                binding.tvMessageOther.text = data?.content
            }
            for (user in userList) {
                if (user?.id == data?.idUser) {
                    binding.tvUserName.text = user?.username
                    CommonUtils.setImageFromBase64(
                        user?.avatar,
                        binding.imvAvatarOther,
                        binding.root.context
                    )
                    break
                }
            }
            binding.root.setOnLongClickListener(object : View.OnLongClickListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onLongClick(v: View?): Boolean {
                    Toast.makeText(
                        binding.root.context,
                        CommonUtils.encrypt(data?.content!!, data.idMessage),
                        Toast.LENGTH_SHORT
                    ).show()
                    return true
                }

            })
        }

    }

    override fun getLayoutResourceItem(): Int {
        return R.layout.layout_message_item
    }

    override fun onCreateBasicViewHolder(binding: ViewDataBinding?): ViewHolder {
        return ViewHolder(binding as LayoutMessageItemBinding)
    }

    override fun onBindBasicItemView(holder: ViewHolder, position: Int) {
        holder.bindData(getDataSet()?.get(position), position)
    }
}