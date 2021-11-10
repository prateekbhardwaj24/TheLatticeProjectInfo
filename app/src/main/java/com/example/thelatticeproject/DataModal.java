package com.example.thelatticeproject;

public class DataModal {

    private String id, title, readNread;
    private boolean isHold;

    public DataModal() {

    }

    public DataModal(String id, String title, String readNread, boolean isHold) {
        this.id = id;
        this.title = title;
        this.readNread = readNread;
        this.isHold = isHold;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReadNread() {
        return readNread;
    }

    public void setReadNread(String readNread) {
        this.readNread = readNread;
    }

    public boolean isHold() {
        return isHold;
    }

    public void setHold(boolean hold) {
        isHold = hold;
    }
}
