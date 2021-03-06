package cn.gdeiassistant.Service.Account;

import cn.gdeiassistant.Repository.SQL.Mysql.Mapper.GdeiAssistant.User.UserMapper;
import cn.gdeiassistant.Exception.CommonException.PasswordIncorrectException;
import cn.gdeiassistant.Pojo.Entity.User;
import cn.gdeiassistant.Tools.StringEncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 修改用户账号密码
     *
     * @param username
     * @param oldPassword
     * @param newPassword
     * @throws Exception
     */
    public void ChangeUserPassword(String username, String oldPassword, String newPassword) throws Exception {
        User user = userMapper.selectUser(StringEncryptUtils.encryptString(username));
        if (user != null) {
            User decryptedUser = user.decryptUser();
            if (decryptedUser.getPassword().equals(oldPassword)) {
                decryptedUser.setPassword(newPassword);
                User encryptedUser = decryptedUser.encryptUser();
                userMapper.updateUser(encryptedUser);
            } else {
                throw new PasswordIncorrectException("用户旧密码不匹配");
            }
        }
    }

}
