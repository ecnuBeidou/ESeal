package com.agenthun.eseal.bean.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @project ESeal
 * @authors agenthun
 * @date 2016/12/16 05:40.
 */

public class DeviceLocation implements Parcelable {
    private String ReportTime;
    private String UploadType;
    private String SecurityLevel;
    private String ClosedFlag;
    private String BaiduCoordinate;
    private String Temperature;
    private String Humidity;
    private String Vibration;

    public DeviceLocation(String reportTime, String uploadType, String securityLevel, String closedFlag, String baiduCoordinate,
                          String temperature, String humidity, String vibration) {
        ReportTime = reportTime;
        UploadType = uploadType;
        SecurityLevel = securityLevel;
        ClosedFlag = closedFlag;
        BaiduCoordinate = baiduCoordinate;
        Temperature = temperature;
        Humidity = humidity;
        Vibration = vibration;
    }

    public String getReportTime() {
        return ReportTime;
    }

    public void setReportTime(String reportTime) {
        ReportTime = reportTime;
    }

    public String getUploadType() {
        return UploadType;
    }

    public void setUploadType(String uploadType) {
        UploadType = uploadType;
    }

    public String getSecurityLevel() {
        return SecurityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        SecurityLevel = securityLevel;
    }

    public String getClosedFlag() {
        return ClosedFlag;
    }

    public void setClosedFlag(String closedFlag) {
        ClosedFlag = closedFlag;
    }

    public String getBaiduCoordinate() {
        return BaiduCoordinate;
    }

    public void setBaiduCoordinate(String baiduCoordinate) {
        BaiduCoordinate = baiduCoordinate;
    }

    public String getTemperature() {
        return Temperature;
    }

    public void setTemperature(String temperature) {
        Temperature = temperature;
    }

    public String getHumidity() {
        return Humidity;
    }

    public void setHumidity(String humidity) {
        Humidity = humidity;
    }

    public String getVibration() {
        return Vibration;
    }

    public void setVibration(String vibration) {
        Vibration = vibration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ReportTime);
        dest.writeString(this.UploadType);
        dest.writeString(this.SecurityLevel);
        dest.writeString(this.ClosedFlag);
        dest.writeString(this.BaiduCoordinate);
        dest.writeString(this.Temperature);
        dest.writeString(this.Humidity);
        dest.writeString(this.Vibration);
    }

    protected DeviceLocation(Parcel in) {
        this.ReportTime = in.readString();
        this.UploadType = in.readString();
        this.SecurityLevel = in.readString();
        this.ClosedFlag = in.readString();
        this.BaiduCoordinate = in.readString();
        this.Temperature = in.readString();
        this.Humidity = in.readString();
        this.Vibration = in.readString();
    }

    public static final Parcelable.Creator<DeviceLocation> CREATOR = new Parcelable.Creator<DeviceLocation>() {
        @Override
        public DeviceLocation createFromParcel(Parcel source) {
            return new DeviceLocation(source);
        }

        @Override
        public DeviceLocation[] newArray(int size) {
            return new DeviceLocation[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceLocation that = (DeviceLocation) o;

        if (!ReportTime.equals(that.ReportTime)) return false;
        if (!UploadType.equals(that.UploadType)) return false;
        return BaiduCoordinate.equals(that.BaiduCoordinate);

    }

    @Override
    public int hashCode() {
        int result = ReportTime.hashCode();
        result = 31 * result + UploadType.hashCode();
        result = 31 * result + BaiduCoordinate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DeviceLocation{" +
                "ReportTime='" + ReportTime + '\'' +
                ", UploadType='" + UploadType + '\'' +
                ", SecurityLevel='" + SecurityLevel + '\'' +
                ", ClosedFlag='" + ClosedFlag + '\'' +
                ", BaiduCoordinate='" + BaiduCoordinate + '\'' +
                ", Temperature='" + Temperature + '\'' +
                ", Humidity='" + Humidity + '\'' +
                ", Vibration='" + Vibration + '\'' +
                '}';
    }

    public Boolean isInvalid() {
        return ReportTime == null || UploadType == null || BaiduCoordinate == null;
    }
}
