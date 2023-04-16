package com.lordierclaw.testapplication.Model;

import com.lordierclaw.testapplication.Listener.IBotMessageListener;

import java.io.Serializable;

public class MessageModel implements Serializable {
    Long time;
    String message;
    String sender;
    String receiver;
    String senderavatar, receiveravatar, sendername, targetname;
    Long money;


    // BOT MESSAGE
    public static final String SEND_MONEY_BOT = "send_money";
    public static final String SPLIT_MONEY_BOT = "split_money";
    int messageType;

    long transactionAmount = 0;
    String uidmessage;

    public String getUidmessage() {
        return uidmessage;
    }

    public void setUidmessage(String uidmessage) {
        this.uidmessage = uidmessage;
    }

    public long getTransactionAmount() {
        return transactionAmount;
    }


    public void setTransactionAmount(long transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getSendername() {
        return sendername;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public MessageModel(Long time, String message, String sender, String receiver, Long money) {
        this.time = time;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.senderavatar = senderavatar;
        this.receiveravatar = receiveravatar;
        this.sendername = sendername;
        this.targetname = targetname;
        this.money = money;
    }

    public String getTargetname() {
        return targetname;
    }

    public void setTargetname(String targetname) {
        this.targetname = targetname;
    }



    public String getSenderavatar() {
        return senderavatar;
    }

    public void setSenderavatar(String senderavatar) {
        this.senderavatar = senderavatar;
    }

    public String getReceiveravatar() {
        return receiveravatar;
    }

    public void setReceiveravatar(String receiveravatar) {
        this.receiveravatar = receiveravatar;
    }

    public MessageModel() {

    }

    public long getTime() {
        return time;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}