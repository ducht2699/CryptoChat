package com.uni.information_security.ui.personal

import android.app.Person
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.uni.information_security.R
import com.uni.information_security.base.BaseActivity
import com.uni.information_security.databinding.ActivityPersonalBinding
import com.uni.information_security.model.response.chat.User
import com.uni.information_security.ui.login.LoginActivity
import com.uni.information_security.ui.register.RegisterActivity
import com.uni.information_security.utils.CommonUtils
import com.uni.information_security.utils.CommonUtils.showCustomUI
import com.uni.information_security.utils.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
import com.uni.information_security.utils.USER_DATA
import java.io.File

class PersonalActivity : BaseActivity<PersonalViewModel, ActivityPersonalBinding>(),
    View.OnClickListener {



    override fun getContentLayout(): Int {
        return R.layout.activity_personal
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(PersonalViewModel::class.java)
    }

    override fun initView() {
        showCustomUI()
        binding.toolbar.cvUser.visibility = View.GONE
        binding.toolbar.lnlBack.visibility = View.VISIBLE
        binding.toolbar.lnlRight.visibility = View.INVISIBLE
        CommonUtils.setImageFromBase64(USER_DATA?.avatar, binding.imvAvatar, this)
        binding.tvUserName.text = USER_DATA?.username
        binding.toolbar.tvTitleToolbar.text = resources.getString(R.string.str_me)
    }

    override fun initListener() {
        binding.toolbar.lnlBack.setOnClickListener(this)
        binding.imvAvatarHolder.setOnClickListener(this)
        binding.btnLogOut.setOnClickListener(this)
        binding.rltUpdateInfo.setOnClickListener(this)
    }

    override fun observerLiveData() {
        viewModel.setAvatarResult.observe(this, {avatar ->
            CommonUtils.setImageFromBase64(avatar, binding.imvAvatar, this)
            setResult(RESULT_OK)
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.lnl_back -> {
                if (!isDoubleClick()) {
                    finish()
                }
            }
            R.id.imv_avatar_holder -> {
                if (!isDoubleClick()) {
                    openImagePicker()
                }
            }
            R.id.btn_log_out -> {
                if (!isDoubleClick()) {
                    viewModel.logOut()
                    startActivity(LoginActivity.getIntent(this))
                    finishAffinity()
                }
            }
            R.id.rlt_update_info -> {
                if (!isDoubleClick()) {
                    if (binding.tilOldPass.isVisible) {
                        binding.tilOldPass.visibility = View.INVISIBLE
                        binding.tilNewPass.visibility = View.INVISIBLE
                        binding.tilVerifyPass.visibility = View.INVISIBLE
                        binding.btnChangePassword.visibility = View.INVISIBLE
                    } else {
                        binding.tilOldPass.visibility = View.VISIBLE
                        binding.tilNewPass.visibility = View.VISIBLE
                        binding.tilVerifyPass.visibility = View.VISIBLE
                        binding.btnChangePassword.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
    private fun openImagePicker() {
        ImagePicker.create(this)
            .returnMode(ReturnMode.ALL)
            .folderMode(true)
            .toolbarImageTitle(getString(R.string.str_tap_to_select))
            .toolbarArrowColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimary
                )
            )
            .includeVideo(false)
            .single()
            .showCamera(true)
            .enableLog(false)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
                val images = ImagePicker.getImages(data)
                if (images != null) {
                    viewModel.createImage(this, images)
                }

                val image = ImagePicker.getFirstImageOrNull(data)
                val file = File(image.path)

                val bmOptions = BitmapFactory.Options()
                val fileDecode = BitmapFactory.decodeFile(file.absolutePath, bmOptions)
                var scale = 1
                if (fileDecode.width > RegisterActivity.MAX_LENGTH || fileDecode.height > RegisterActivity.MAX_LENGTH) {
                    scale = Math.max(
                        fileDecode.width / RegisterActivity.MAX_LENGTH,
                        fileDecode.height / RegisterActivity.MAX_LENGTH
                    )
                }
                if (fileDecode != null) {
                    val bitmap = Bitmap.createScaledBitmap(
                        fileDecode,
                        fileDecode.width / scale,
                        fileDecode.height / scale,
                        true
                    )
                    viewModel.setAvatar(CommonUtils.convertBitmapToBase64(bitmap))
                }
            }
        }
    }
}