package com.project.hucemoney.viewmodels;

import static com.project.hucemoney.utils.ValidationUtils.containsSpecialCharacter;
import static com.project.hucemoney.utils.ValidationUtils.isNullOrEmpty;
import static com.project.hucemoney.utils.ValidationUtils.isValidEmail;
import static com.project.hucemoney.utils.ValidationUtils.isValidPassword;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.common.ResponseMessage;
import com.project.hucemoney.database.AppDatabase;
import com.project.hucemoney.entities.User;
import com.project.hucemoney.models.Response;
import com.project.hucemoney.models.requests.UserLoginRequest;
import com.project.hucemoney.models.requests.UserRegisterRequest;
import com.project.hucemoney.repositories.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class UserViewModel extends AndroidViewModel{
    private UserLoginRequest userLoginRequest = new UserLoginRequest();
    private UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
    private MutableLiveData<Response<Map<String, String>>> loginResult = new MutableLiveData<>();
    private MutableLiveData<Response<String>> registerResult = new MutableLiveData<>();
    private MutableLiveData<Response<String>> verifyRegisterResult = new MutableLiveData<>();
    private MutableLiveData<Response<String>> verifyForgotPasswordResult = new MutableLiveData<>();
    private UserRepository userRepository;

    public UserViewModel(Application application) {
        super(application);
        this.userRepository = new UserRepository(AppDatabase.getDatabase(application));
    }

    public void login() {
        Response<Map<String, String>> response = new Response<>();
        try {
            if (isNullOrEmpty(userLoginRequest.getUsername()) || isNullOrEmpty(userLoginRequest.getPassword())) {
                response.setStatus(ResponseCode.FAIL);
                response.setMessage("Vui lòng nhập đầy đủ thông tin");
            } else {
                User user = userRepository.login(userLoginRequest);
                if (user != null) {
                    Map<String, String> data = new HashMap<>();
                    data.put("UUID", user.getUUID());
                    data.put("username", user.getUsername());
                    data.put("email", user.getEmail());
                    response.setData(data);
                    response.setStatus(ResponseCode.SUCCESS);
                    response.setMessage(String.format(ResponseMessage.SUCCESS, "Đăng nhập"));
                } else {
                    response.setStatus(ResponseCode.FAIL);
                    response.setMessage(String.format(ResponseMessage.FAIL, "Đăng nhập"));
                }
                loginResult.setValue(response);
            }
        } catch (Exception e) {
            Log.e("UserViewModel", "login: " + e.getMessage());
            //response.setMessage("Except: Đăng nhập thất bại");
            response.setMessage( e.getMessage());
            loginResult.setValue(response);
        }
    }

    public void register() {
        Response<String> response = new Response<>();
        try {
            if (isNullOrEmpty(userRegisterRequest.getUsername())
                    || isNullOrEmpty(userRegisterRequest.getPassword())
                    || isNullOrEmpty(userRegisterRequest.getEmail())
                    || isNullOrEmpty(userRegisterRequest.getConfirmPassword())) {
                response.setMessage("Vui lòng nhập đầy đủ thông tin đăng ký");
                registerResult.setValue(response);
                return;
            }
            if(containsSpecialCharacter(userRegisterRequest.getUsername()) || userRegisterRequest.getUsername().length() < 2){
                response.setMessage("Tên đăng nhập không được chứa ký tự đặc biệt và phải có ít nhất 2 ký tự");
                registerResult.setValue(response);
                return;
            }
            if (!isValidEmail(userRegisterRequest.getEmail())) {
                response.setMessage("Định dạng email không hợp lệ");
                registerResult.setValue(response);
                return;
            }
            if(!isValidPassword(userRegisterRequest.getPassword())){
                response.setMessage("Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số");
                registerResult.setValue(response);
                return;
            }
            if (!userRegisterRequest.getPassword().equals(userRegisterRequest.getConfirmPassword())) {
                response.setMessage("Mật khẩu không khớp");
                registerResult.setValue(response);
                return;
            }
            User user = userRepository.register(userRegisterRequest);
            if (user != null) {
                response.setData(user.getUUID());
                response.setStatus(ResponseCode.SUCCESS);
                response.setMessage(String.format(ResponseMessage.SUCCESS, "Vui lòng kiểm tra email để kích hoạt tài khoản"));
            } else {
                response.setMessage(String.format(ResponseMessage.FAIL, "Đăng ký"));
            }
            registerResult.setValue(response);
        } catch (Exception e) {
            Log.e("UserViewModel", "register: " + e.getMessage());
            //response.setMessage("Except: Đăng ký thất bại");
            response.setMessage( e.getMessage());
            registerResult.setValue(response);
        }
    }

    public void verifyRegister(String UUID) {
        Response<String> response = new Response<>();
        try {
            boolean verify = userRepository.verifyRegister(UUID);
            if (verify) {
                response.setStatus(ResponseCode.SUCCESS);
                response.setMessage(String.format(ResponseMessage.SUCCESS, "Xác thực tài khoản"));
            } else {
                response.setStatus(ResponseCode.FAIL);
                response.setMessage(String.format(ResponseMessage.FAIL, "Xác thực tài khoản"));
            }
            verifyRegisterResult.setValue(response);
        } catch (Exception e) {
            Log.e("UserViewModel", "verify: " + e.getMessage());
            //response.setMessage("Except: Xác thực thất bại");
            response.setMessage( e.getMessage());
            verifyRegisterResult.setValue(response);
        }
    }

    public LiveData<Response<String>> getVerifyRegisterResult() {
        return verifyRegisterResult;
    }

    public LiveData<Response<String>> getVerifyForgotPasswordResult() {
        return verifyForgotPasswordResult;
    }

    public LiveData<Response<String>> getRegisterResult() {
        return registerResult;
    }

    public LiveData<Response<Map<String, String>>> getLoginResult() {
        return loginResult;
    }

    public UserLoginRequest getUserLoginRequest() {
        return userLoginRequest;
    }

    public UserRegisterRequest getUserRegisterRequest() {
        return userRegisterRequest;
    }
}
