package com.uni.information_security.ui.create_group

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.uni.information_security.R
import com.uni.information_security.base.BaseRecyclerAdapter
import com.uni.information_security.databinding.LayoutUserCreateRoomItemBinding
import com.uni.information_security.model.response.chat.User
import com.uni.information_security.utils.CommonUtils
import com.uni.information_security.utils.EMPTY_STRING

class UserCreateRoomAdapter(dataSet: MutableList<User?>?, val iOnUserClicked: IOnUserClicked) :
    BaseRecyclerAdapter<User, UserCreateRoomAdapter.ViewHolder>(dataSet) {
    inner class ViewHolder(val binding: LayoutUserCreateRoomItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: User?, position: Int) {
            CommonUtils.setImageFromBase64(data?.avatar, binding.imvAvatar, binding.root.context)
            binding.tvUsername.text = data?.username ?: EMPTY_STRING
            binding.root.setOnClickListener {
                binding.rdbSelect.isChecked = !binding.rdbSelect.isChecked
            }
            binding.rdbSelect.setOnClickListener {
            }
            binding.rdbSelect.setOnCheckedChangeListener { buttonView, isChecked ->
                iOnUserClicked.onUserClicked(data, isChecked)
            }
        }

    }

    interface IOnUserClicked {
        fun onUserClicked(data: User?, isChecked: Boolean)
    }

    override fun getLayoutResourceItem(): Int {
        return R.layout.layout_user_create_room_item
    }

    override fun onCreateBasicViewHolder(binding: ViewDataBinding?): ViewHolder {
        return ViewHolder(binding as LayoutUserCreateRoomItemBinding)
    }

    override fun onBindBasicItemView(holder: ViewHolder, position: Int) {
        holder.bindData(getDataSet()?.get(position), position)
    }
}