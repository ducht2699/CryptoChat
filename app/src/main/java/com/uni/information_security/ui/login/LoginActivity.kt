package com.uni.information_security.ui.login

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProviders
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.uni.information_security.R
import com.uni.information_security.base.BaseActivity
import com.uni.information_security.databinding.ActivityLoginBinding
import com.uni.information_security.model.request.chat.LoginRequest
import com.uni.information_security.ui.main.MainActivity
import com.uni.information_security.utils.CommonUtils
import com.uni.information_security.utils.CommonUtils.hideSoftKeyboard
import com.uni.information_security.utils.EMPTY_STRING
import com.uni.information_security.view_model.ViewModelFactory

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>(), View.OnClickListener {

    private val RC_SIGN_IN = 1000
    private val TAG: String = "LoginActivity"
    private var fbLoginManager: LoginManager? = null
    private var callbackManager: CallbackManager? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null


    companion object {
        fun getIntent(
            context: Context
        ): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_login
    }

    override fun observerLiveData() {
        viewModel.apply {
            loginResponse.observe(this@LoginActivity, { result ->
                if (result) {
                    startActivity(MainActivity.getIntent(this@LoginActivity))
                }
            })
        }
    }

    override fun initView() {
        binding.tvRegister.text = HtmlCompat.fromHtml(
            resources.getString(R.string.str_has_account),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    override fun initListener() {
        binding.btnLogin.setOnClickListener(this)

    }

    override fun initViewModel() {
        viewModel =
            ViewModelProviders.of(this, ViewModelFactory(this)).get(LoginViewModel::class.java)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                hideSoftKeyboard()
                val username = binding.edtUsername.text.toString().trim()

                //TODO: encrypt password
                val password = binding.edtPassword.text.toString().trim()

                if (username == EMPTY_STRING || password == EMPTY_STRING) {
                    Toast.makeText(this, resources.getString(R.string.str_missing_input), Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.login(LoginRequest(username, password))
                }

            }
        }
    }

}
