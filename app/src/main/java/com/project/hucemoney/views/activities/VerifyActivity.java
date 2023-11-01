package com.project.hucemoney.views.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.project.hucemoney.common.Constants;
import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.common.enums.DialogType;
import com.project.hucemoney.database.AppDatabase;
import com.project.hucemoney.databinding.ActivityVerifyBinding;
import com.project.hucemoney.utils.FunctionUtils;
import com.project.hucemoney.utils.NotificationUtils;
import com.project.hucemoney.viewmodels.UserViewModel;

import java.util.Objects;


public class VerifyActivity extends AppCompatActivity {

    private ActivityVerifyBinding binding;
    private static int countResend = 0;
    private UserViewModel userViewModel;
    private String typeVerify;
    private String codeVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        controlAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void init() {
        //AppDatabase appDatabase = AppDatabase.getDatabase(this);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        Intent intent = getIntent();
        String typeVerify = intent.getStringExtra("typeVerify");
        if (Objects.equals(typeVerify, Constants.TYPE_VERIFY_REGISTER)) {
            binding.tvTitleVerify.setText("Xác thực đăng ký");
            this.typeVerify = "register";
        } else if (Objects.equals(typeVerify, Constants.TYPE_VERIFY_FORGOT_PASSWORD)) {
            binding.tvTitleVerify.setText("Xác thực quên mật khẩu");
            this.typeVerify = "forgotPassword";
        } else {
            Toast.makeText(this, "Lỗi xác thực", Toast.LENGTH_SHORT).show();
            finish();
        }
        this.codeVerify = FunctionUtils.randomCodeVerify();
        NotificationUtils.showNotification(this, "Mã xác thực", this.codeVerify);
    }

    private void controlAction() {
        binding.number1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeModeButtonVerify();
                if ((s.length() == 1)) {
                    binding.number2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.number2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeModeButtonVerify();
                if ((s.length() == 1)) {
                    binding.number3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.number3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeModeButtonVerify();
                if ((s.length() == 1)) {
                    binding.number4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.number4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeModeButtonVerify();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        binding.resend.setOnClickListener(v -> {
            if (countResend < 3) {
                countResend++;
                Toast.makeText(this, "Mã xác thực đã gửi lại", Toast.LENGTH_SHORT).show();
            } else {
                binding.resend.setEnabled(false);
                binding.warning.setVisibility(android.view.View.VISIBLE);
                final Handler handler = new Handler();
                final int delay = 10000;
                final long startTime = System.currentTimeMillis();

                handler.postDelayed(() -> {
                    countResend = 0;
                    binding.warning.setText("");
                    binding.warning.setVisibility(android.view.View.GONE);
                    binding.resend.setEnabled(true);
                }, delay);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        long timeElapsed = System.currentTimeMillis() - startTime;
                        long timeRemaining = delay - timeElapsed;
                        int secondsRemaining = (int) (timeRemaining / 1000);
                        String text = "Thao tác quá nhiều. Vui lòng thử lại sau " + secondsRemaining + " giây";
                        SpannableString spannableString = new SpannableString(text);
                        int start = text.indexOf(String.valueOf(secondsRemaining));
                        int end = start + String.valueOf(secondsRemaining).length();
                        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        binding.warning.setText(spannableString);

                        if (timeRemaining > 0) {
                            handler.postDelayed(this, 1000);
                        }
                    }
                });
            }
        });


        binding.btnVerify.setOnClickListener(v -> {
            String number1 = binding.number1.getText().toString().trim();
            String number2 = binding.number2.getText().toString().trim();
            String number3 = binding.number3.getText().toString().trim();
            String number4 = binding.number4.getText().toString().trim();
            if (number1.isEmpty() || number2.isEmpty() || number3.isEmpty() || number4.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã xác thực", Toast.LENGTH_SHORT).show();
                return;
            }
            if (String.format("%s%s%s%s", number1, number2, number3, number4).equals(codeVerify)) {
                if (typeVerify.equals("register")) {
                    Intent intent = getIntent();
                    String UUID = intent.getStringExtra("UUID");
                    userViewModel.verifyRegister(UUID);
                    userViewModel.getVerifyRegisterResult().observe(this, response -> {
                        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        if (Objects.equals(response.getStatus(), ResponseCode.SUCCESS)) {
                            Intent intent1 = new Intent(this, LoginActivity.class);
                            startActivity(intent1);
                            finish();
                        }
                    });
                } else if (typeVerify.equals("forgotPassword")) {
                    Toast.makeText(this, "Mật khẩu mới đã được gửi về email", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LauncherActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                FunctionUtils.showDialogNotify(this, "", "Mã xác thực không đúng", DialogType.ERROR);
            }
        });
    }

    private void changeModeButtonVerify() {
        String number1 = binding.number1.getText().toString().trim();
        String number2 = binding.number2.getText().toString().trim();
        String number3 = binding.number3.getText().toString().trim();
        String number4 = binding.number4.getText().toString().trim();
        binding.btnVerify.setEnabled(!number1.isEmpty() && !number2.isEmpty() && !number3.isEmpty() && !number4.isEmpty());
    }
}