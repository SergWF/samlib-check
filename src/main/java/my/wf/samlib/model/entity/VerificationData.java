package my.wf.samlib.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "verifcation_data")
public class VerificationData {
    private String id;
    private UserData userData;
    private Date expirationDate;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "user_id")
    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
