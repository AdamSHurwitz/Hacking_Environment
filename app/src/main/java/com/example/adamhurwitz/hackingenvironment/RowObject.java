package com.example.adamhurwitz.hackingenvironment;

/**
 * Created by adamhurwitz on 10/31/15.
 */
public class RowObject {
    private long entryID;
    private String conceptName = "";
    private String tabNum = "";

    public RowObject insertID(long id) {
        entryID = id;
        return this;
    }

    public RowObject insertConceptName(String name) {
        conceptName = name;
        return this;
    }

    public RowObject insertTabNum(String num) {
        tabNum = num;
        return this;
    }

    public long getID() {
        return entryID;
    }

    public String getConceptName() {
        return conceptName;
    }

    public String getTabNum() {
        return tabNum;
    }

}
