package tn.esprit.nascar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.nascar.dao.UserApiInterface
import tn.esprit.nascar.databinding.ActivityLoginBinding
import tn.esprit.nascar.models.User

const val PREF_FILE = "NASCAR_PREF"
const val EMAIL = "EMAIL"
const val PASSWORD = "PASSWORD"
const val IS_REMEMBERED = "IS_REMEMBERED"

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mSharedPreferences = getSharedPreferences(PREF_FILE, MODE_PRIVATE)

        binding.tiEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmail()
            }

            override fun afterTextChanged(s: Editable?) {
                return
            }
        })

        binding.tiPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword()
            }

            override fun afterTextChanged(s: Editable?) {
                return
            }
        })

        if (mSharedPreferences.getBoolean(IS_REMEMBERED, false)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            if (validateEmail() && validatePassword()) {

                if (binding.btnRememberMe.isChecked) {
                    val userInterface= UserApiInterface.create()
                    userInterface.login(User(1,binding.tiEmail.text.toString(),binding.tiPassword.text.toString(),10))
                        .enqueue(object :Callback<User>{
                            override fun onResponse(call: Call<User>, response: Response<User>) {
                                if (response.code()==200){
                                    mSharedPreferences.edit().apply{
                                        putString(EMAIL, binding.tiEmail.text.toString())
                                        putString(PASSWORD, binding.tiPassword.text.toString())
                                        putBoolean(IS_REMEMBERED, binding.btnRememberMe.isChecked)
                                    }.apply()
                                }

                                startActivity(Intent(applicationContext, MainActivity::class.java))
                                finish()
                                }


                            override fun onFailure(call: Call<User>, t: Throwable) {
                                Snackbar.make(
                                    binding.contextView,
                                    "error server",
                                    Snackbar.LENGTH_SHORT
                                )
                                    .show()
                            }

                        })

                } else {
                    Snackbar.make(
                        binding.contextView,
                        getString(R.string.msg_error_inputs),
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

    }

    private fun validateEmail(): Boolean {
        binding.tiEmailLayout.isErrorEnabled = false

        if (binding.tiEmail.text.toString().isEmpty()) {
            binding.tiEmailLayout.error = getString(R.string.msg_must_not_be_empty)
            binding.tiEmail.requestFocus()
            return false
        }else{
            binding.tiEmailLayout.isErrorEnabled = false
        }

        if (Patterns.EMAIL_ADDRESS.matcher(binding.tiEmail.text.toString()).matches()) {
            binding.tiEmailLayout.error = getString(R.string.msg_check_your_email)
            binding.tiEmail.requestFocus()
            return false
        }else{
            binding.tiEmailLayout.isErrorEnabled = false
        }

        return true
    }

    private fun validatePassword(): Boolean {
        binding.tiPasswordLayout.isErrorEnabled = false

        if (binding.tiPassword.text.toString().isEmpty()) {
            binding.tiPasswordLayout.error = getString(R.string.msg_must_not_be_empty)
            binding.tiPassword.requestFocus()
            return false
        }else{
            binding.tiPasswordLayout.isErrorEnabled = false
        }

        if (binding.tiPassword.text.toString().length < 6) {
            binding.tiPasswordLayout.error = getString(R.string.msg_check_your_characters)
            binding.tiPassword.requestFocus()
            return false
        }else{
            binding.tiPasswordLayout.isErrorEnabled = false
        }

        return true
    }

}