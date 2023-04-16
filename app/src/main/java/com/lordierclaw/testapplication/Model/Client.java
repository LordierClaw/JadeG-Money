package com.lordierclaw.testapplication.Model;

public class Client {
    private String name;
    private String pfpUrl;
    private String cardId;

    private boolean isChecked;

    public Client(String name, String pfpUrl, String cardId) {
        this.name = name;
        this.pfpUrl = pfpUrl;
        this.cardId = cardId;
        isChecked = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPfpUrl() {
        return pfpUrl;
    }

    public void setPfpUrl(String pfpUrl) {
        this.pfpUrl = pfpUrl;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
