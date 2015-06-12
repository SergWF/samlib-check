package my.wf.samlib.model.dto;

import my.wf.samlib.model.entity.UserData;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class UserDto {
    private String email;
    private String password;
    private String nickName;

    public UserData toUserData(){
        UserData userData = new UserData();
        userData.setUsername(this.getEmail());
        userData.setPassword(this.getPassword());
        return userData;
    }

    @NotEmpty
    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Length(min = 6)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotEmpty
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
