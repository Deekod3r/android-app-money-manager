package com.project.hucemoney.repositories;

import android.app.Application;

import com.project.hucemoney.DAOs.UserDAO;
import com.project.hucemoney.database.AppDatabase;
import com.project.hucemoney.entities.User;
import com.project.hucemoney.models.requests.UserLoginRequest;
import com.project.hucemoney.models.requests.UserRegisterRequest;
import com.project.hucemoney.utils.FunctionUtils;

public class UserRepository {
    private UserDAO userDAO;
    public UserRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        this.userDAO = appDatabase.userDAO();
    }

    public User login(UserLoginRequest loginRequest) {
        try {
            User user = userDAO.findByUsername(loginRequest.getUsername());
            if (user == null) {
                throw new RuntimeException("Tài khoản không tồn tại");
            }
            if (!user.getPassword().equals(FunctionUtils.md5(loginRequest.getPassword()))) {
                throw new RuntimeException("Mật khẩu không chính xác");
            }
            return user;
        } catch (Exception e) {
            throw e;
        }
    }

    public User register(UserRegisterRequest registerRequest){
        try {
            if (userDAO.findByUsername(registerRequest.getUsername()) != null) {
                throw new RuntimeException("Tài khoản đã tồn tại");
            }
            if (userDAO.findByEmail(registerRequest.getEmail()) != null) {
                throw new RuntimeException("Email đã tồn tại");
            }
            User user = new User();
            user.setUUID(java.util.UUID.randomUUID().toString());
            user.setId("user" + System.currentTimeMillis());
            user.setUsername(registerRequest.getUsername());
            user.setPassword(FunctionUtils.md5(registerRequest.getPassword()));
            user.setEmail(registerRequest.getEmail());
            user.setDeleted(true);
            long rowID = userDAO.save(user);
            if (rowID <= 0) {
                throw new RuntimeException("Đăng ký thất bại");
            }
            return user;
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean verifyRegister(String UUID){
        try {
            User user = userDAO.findForVerifyRegister(UUID);
            if (user == null) {
                throw new RuntimeException("User không tồn tại");
            }
            user.setDeleted(false);
            return userDAO.update(user) > 0;
        } catch (Exception e) {
            throw e;
        }
    }

    public int deleteAll(){
        try {
            return userDAO.deleteAll();
        } catch (Exception e) {
            throw e;
        }
    }
}
