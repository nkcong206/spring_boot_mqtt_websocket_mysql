package com.dailycodebuffer.websocket;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.time.LocalTime;

@Controller
public class GreetingController {

    String topic1 = "device_1";
    String topic2 = "device_2";
    String topic3 = "device_3";
    int qos = 0;
    String broker = "tcp://127.0.0.1:1883";

    Device device;


    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private final SimpMessagingTemplate simpMessagingTemplate;

    public GreetingController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    String ClientId = MqttAsyncClient.generateClientId();

    {
        try {
            MqttClient mqttClient = new MqttClient(broker, ClientId);
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.isCleanSession();
            connectOptions.isAutomaticReconnect();

            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {

                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    System.out.println(mqttMessage.toString());
                    JSONObject jsonObject = new JSONObject(mqttMessage.toString());
                    String type = jsonObject.getString("type");
                    int value = jsonObject.getInt("value");
                    LocalTime localTime = LocalTime.now();
                    device = new Device(s, type, value, localTime);
                    simpMessagingTemplate.convertAndSend("/topic/greetings", "{\"deviceid\":\"" + s + "\",\"value\":" + value + ", \"time\":\"" + localTime + "\"}");
                    deviceRepository.save(device);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                }
            });
            mqttClient.connect(connectOptions);
            mqttClient.subscribe(topic1, qos);
            mqttClient.subscribe(topic2, qos);
            mqttClient.subscribe(topic3, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


}
