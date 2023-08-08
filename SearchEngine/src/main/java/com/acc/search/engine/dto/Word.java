package com.acc.search.engine.dto;

public class Word {

    private String str;
    private int editDis;

    public Word (String str, int editDis, int id) {
        this.str = str;
        this.editDis = editDis;
    }

    public void setStr(String str) { this.str = str; }

    public void setEditDis(int editDis) { this.editDis = editDis; }

    public int getEditDis() { return editDis; }

    public String getStr() { return str; }
}
