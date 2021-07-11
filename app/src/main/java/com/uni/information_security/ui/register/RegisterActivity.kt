package com.uni.information_security.ui.register

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProviders
import com.facebook.common.Common
import com.uni.information_security.R
import com.uni.information_security.base.BaseActivity
import com.uni.information_security.databinding.ActivityRegisterBinding
import com.uni.information_security.model.request.chat.CreateAccountRequest
import com.uni.information_security.model.request.chat.LoginRequest
import com.uni.information_security.ui.login.LoginActivity
import com.uni.information_security.ui.login.LoginViewModel
import com.uni.information_security.utils.CommonUtils
import com.uni.information_security.utils.CommonUtils.hideSoftKeyboard
import com.uni.information_security.utils.EMPTY_STRING

class RegisterActivity : BaseActivity<LoginViewModel, ActivityRegisterBinding>(),
    View.OnClickListener {

    companion object {
        fun getIntent(
            context: Context
        ): Intent {
            return Intent(context, RegisterActivity::class.java)
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_register
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun initView() {
        binding.toolbar.tvTitleToolbar.text = resources.getString(R.string.str_register)
        binding.toolbar.lnlBack.visibility = View.VISIBLE
        binding.toolbar.lnlRight.visibility = View.GONE
        binding.toolbar.cvUser.visibility = View.GONE
    }

    override fun initListener() {
        binding.nestedScrollView.setOnClickListener(this)
        binding.toolbar.lnlBack.setOnClickListener(this)
        binding.btnFinish.setOnClickListener(this)
    }

    override fun observerLiveData() {
        viewModel.apply {
            createAccountResponse.observe(this@RegisterActivity, {
                Handler(Looper.getMainLooper()).postDelayed({
                    Toast.makeText(this@RegisterActivity, resources.getString(R.string.str_success), Toast.LENGTH_SHORT).show()
                }, 300)
                startActivity(LoginActivity.getIntent(this@RegisterActivity))
                finishAffinity()
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.nestedScrollView -> {
                hideSoftKeyboard()
            }
            R.id.lnl_back -> {
                hideSoftKeyboard()
                if (!isDoubleClick())
                    finish()
            }
            R.id.btn_finish -> {
                hideSoftKeyboard()
                if (!isDoubleClick()) {
                    val username = binding.edtUsername.text.toString().trim()
                    val password = binding.edtPassword.text.toString().trim()
                    val verPassword = binding.edtVerifyPassword.text.toString().trim()

                    if (username == EMPTY_STRING || password == EMPTY_STRING || verPassword == EMPTY_STRING) {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.str_missing_input),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (password != verPassword) {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.str_false_verification),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        viewModel.createAccount(
                            CreateAccountRequest(
                                username,
                                password
                            )
                        )
                    }
                }
            }
        }
    }

}