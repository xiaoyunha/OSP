package com.json.osp.entity;

public class LoginLogoutInfo {
    private Long logId;
    private Long userId;
    private String ip;
    private String logType;
    private String logTime;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    @Override
    public String toString() {
        return "LoginLogoutInfo{" +
                "logId=" + logId +
                ", userId=" + userId +
                ", ip='" + ip + '\'' +
                ", logType='" + logType + '\'' +
                ", logTime='" + logTime + '\'' +
                '}';
    }
}
