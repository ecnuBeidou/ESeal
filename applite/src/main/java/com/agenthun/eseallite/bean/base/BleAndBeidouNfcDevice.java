package com.agenthun.eseallite.bean.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @project ESeal
 * @authors agenthun
 * @date 2016/12/16 03:43.
 */

public class BleAndBeidouNfcDevice implements Parcelable {
    private String ContainerId;
    private String ContainerNo;
    private String FreightName;
    private String Origin;
    private String Frequency;
    private String NFCID;
    private String Operationer;
    private String Status;
    private String DeviceType;

    public BleAndBeidouNfcDevice(String containerId, String containerNo, String freightName, String origin, String frequency, String NFCID, String operationer, String status, String deviceType) {
        ContainerId = containerId;
        ContainerNo = containerNo;
        FreightName = freightName;
        Origin = origin;
        Frequency = frequency;
        this.NFCID = NFCID;
        Operationer = operationer;
        Status = status;
        DeviceType = deviceType;
    }

    public String getContainerId() {
        return ContainerId;
    }

    public void setContainerId(String containerId) {
        ContainerId = containerId;
    }

    public String getContainerNo() {
        return ContainerNo;
    }

    public void setContainerNo(String containerNo) {
        ContainerNo = containerNo;
    }

    public String getFreightName() {
        return FreightName;
    }

    public void setFreightName(String freightName) {
        FreightName = freightName;
    }

    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(String origin) {
        Origin = origin;
    }

    public String getFrequency() {
        return Frequency;
    }

    public void setFrequency(String frequency) {
        Frequency = frequency;
    }

    public String getNFCID() {
        return NFCID;
    }

    public void setNFCID(String NFCID) {
        this.NFCID = NFCID;
    }

    public String getOperationer() {
        return Operationer;
    }

    public void setOperationer(String operationer) {
        Operationer = operationer;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String deviceType) {
        DeviceType = deviceType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ContainerId);
        dest.writeString(this.ContainerNo);
        dest.writeString(this.FreightName);
        dest.writeString(this.Origin);
        dest.writeString(this.Frequency);
        dest.writeString(this.NFCID);
        dest.writeString(this.Operationer);
        dest.writeString(this.Status);
        dest.writeString(this.DeviceType);
    }

    protected BleAndBeidouNfcDevice(Parcel in) {
        this.ContainerId = in.readString();
        this.ContainerNo = in.readString();
        this.FreightName = in.readString();
        this.Origin = in.readString();
        this.Frequency = in.readString();
        this.NFCID = in.readString();
        this.Operationer = in.readString();
        this.Status = in.readString();
        this.DeviceType = in.readString();
    }

    public static final Creator<BleAndBeidouNfcDevice> CREATOR = new Creator<BleAndBeidouNfcDevice>() {
        @Override
        public BleAndBeidouNfcDevice createFromParcel(Parcel source) {
            return new BleAndBeidouNfcDevice(source);
        }

        @Override
        public BleAndBeidouNfcDevice[] newArray(int size) {
            return new BleAndBeidouNfcDevice[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BleAndBeidouNfcDevice that = (BleAndBeidouNfcDevice) o;

        return ContainerId.equals(that.ContainerId);

    }

    @Override
    public int hashCode() {
        return ContainerId.hashCode();
    }

    @Override
    public String toString() {
        return "BleAndBeidouNfcDevice{" +
                "ContainerId='" + ContainerId + '\'' +
                ", ContainerNo='" + ContainerNo + '\'' +
                ", FreightName='" + FreightName + '\'' +
                ", Origin='" + Origin + '\'' +
                ", Frequency='" + Frequency + '\'' +
                ", NFCID='" + NFCID + '\'' +
                ", Operationer='" + Operationer + '\'' +
                ", Status='" + Status + '\'' +
                ", DeviceType='" + DeviceType + '\'' +
                '}';
    }
}
