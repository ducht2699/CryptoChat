package com.uni.information_security.ui.main.fragment

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uni.information_security.R
import com.uni.information_security.base.BaseRecyclerAdapter
import com.uni.information_security.databinding.LayoutUserItemBinding
import com.uni.information_security.model.response.chat.User
import com.uni.information_security.utils.CommonUtils
import com.uni.information_security.utils.EMPTY_STRING

class UserAdapter(dataSet: MutableList<User?>, val iUserCallback: IUserCallBack) : BaseRecyclerAdapter<User, UserAdapter.ViewHolder>(dataSet) {
    inner class ViewHolder(val binding: LayoutUserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: User?) {
            binding.tvUserTitle.text = data?.username
            CommonUtils.setImageFromBase64(data?.avatar, binding.imvAvatar, binding.root.context)
            binding.root.setOnClickListener {
                iUserCallback.onUserClick(data)
            }
        }

    }

    interface IUserCallBack {
        fun onUserClick(user: User?)
    }

    override fun getLayoutResourceItem(): Int {
        return R.layout.layout_user_item
    }

    override fun onCreateBasicViewHolder(binding: ViewDataBinding?): ViewHolder {
        return ViewHolder(binding as LayoutUserItemBinding)
    }

    override fun onBindBasicItemView(holder: ViewHolder, position: Int) {
        holder.bindData(getDataSet()?.get(position))
    }
}