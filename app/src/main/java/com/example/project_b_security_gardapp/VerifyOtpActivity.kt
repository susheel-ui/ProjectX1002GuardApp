package com.example.project_b_security_gardapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_b_security_gardapp.databinding.ActivityVerifyOtpBinding

class VerifyOtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupOtpInputs()

        binding.buttonVerify.setOnClickListener {
            val otp = getEnteredOtp()
            if (otp.length == 6) {
                Toast.makeText(this, "Verifying OTP: $otp", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ChangePasswordActivity::class.java))
                // ✅ You can trigger your verification coroutine here
            } else {
                Toast.makeText(this, "Please enter all 6 digits", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textResend.setOnClickListener {
            Toast.makeText(this, "Resending OTP...", Toast.LENGTH_SHORT).show()
            // ✅ Trigger resend API here
        }
    }

    private fun setupOtpInputs() {
        val otpFields = listOf(
            binding.otp1, binding.otp2, binding.otp3,
            binding.otp4, binding.otp5, binding.otp6
        )

        otpFields.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        // Move to next field
                        if (index < otpFields.size - 1) {
                            otpFields[index + 1].requestFocus()
                        } else {
                            editText.clearFocus()
                        }
                    } else if (s.isNullOrEmpty() && index > 0) {
                        // Move to previous field on delete
                        otpFields[index - 1].requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            // When user presses "Done" on last field
            if (index == otpFields.size - 1) {
                editText.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        binding.buttonVerify.performClick()
                        true
                    } else false
                }
            }
        }

        // 🔥 Paste full OTP (handles if user copies from SMS)
//        binding.otp1.setOnFocusChangeListener { v, hasFocus ->
//            if (hasFocus) {
//                (v as EditText).setOnPasteListener { pastedText ->
//                    if (pastedText.length == 6) {
//                        pastedText.forEachIndexed { i, c ->
//                            if (i < otpFields.size) otpFields[i].setText(c.toString())
//                        }
//                    }
//                }
//            }
//        }
    }

    private fun getEnteredOtp(): String {
        return binding.otp1.text.toString().trim() +
                binding.otp2.text.toString().trim() +
                binding.otp3.text.toString().trim() +
                binding.otp4.text.toString().trim() +
                binding.otp5.text.toString().trim() +
                binding.otp6.text.toString().trim()
    }
}