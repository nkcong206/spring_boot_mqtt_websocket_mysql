package com.dailycodebuffer.websocket;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "device")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String deviceId;

    @Column
    private String type;

    @Column
    private int value;

    @Column(name = "local_time", columnDefinition = "TIME")
    private LocalTime localTime;

    public Device(String deviceId, String type, int value, LocalTime localTime) {
        this.deviceId = deviceId;
        this.type = type;
        this.value = value;
        this.localTime = localTime;
    }

    public Device() {

    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }
}
