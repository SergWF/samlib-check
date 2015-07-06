package my.wf.samlib.model.dto;

public class IpCheckState {
    private String ip;
    private boolean inSpamList;
    private boolean blocked;
    private boolean otherError;
    private String info;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isInSpamList() {
        return inSpamList;
    }

    public void setInSpamList(boolean inSpamList) {
        this.inSpamList = inSpamList;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean isBlocked) {
        this.blocked = isBlocked;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isOtherError() {
        return otherError;
    }

    public void setOtherError(boolean otherError) {
        this.otherError = otherError;
    }

    public boolean isOk(){
        return !blocked && !inSpamList && !otherError;
    }
}
