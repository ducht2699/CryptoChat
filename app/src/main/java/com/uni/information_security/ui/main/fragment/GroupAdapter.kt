package com.uni.information_security.ui.main.fragment

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.uni.information_security.R
import com.uni.information_security.base.BaseRecyclerAdapter
import com.uni.information_security.databinding.LayoutGroupItemBinding
import com.uni.information_security.databinding.LayoutUserItemBinding
import com.uni.information_security.model.response.chat.Group
import com.uni.information_security.utils.CommonUtils
import com.uni.information_security.utils.EMPTY_STRING

class GroupAdapter(dataSet: MutableList<Group?>?): BaseRecyclerAdapter<Group, GroupAdapter.ViewHolder>(dataSet) {
    inner class ViewHolder(val binding: LayoutGroupItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: Group?, position: Int) {
            binding.tvGroupTitle.text = data?.name?: EMPTY_STRING
            CommonUtils.setImageFromBase64(data?.avatar?: EMPTY_STRING, binding.imvAvatar, binding.root.context)
            binding.tvLastMessage.text = data?.messageList?.get(data.messageList.size)?.content?: EMPTY_STRING
        }

    }

    override fun getLayoutResourceItem(): Int {
        return R.layout.layout_group_item
    }

    override fun onCreateBasicViewHolder(binding: ViewDataBinding?): ViewHolder {
        return ViewHolder(binding as LayoutGroupItemBinding)
    }

    override fun onBindBasicItemView(holder: ViewHolder, position: Int) {
        holder.bindData(getDataSet()?.get(position), position)
    }

}