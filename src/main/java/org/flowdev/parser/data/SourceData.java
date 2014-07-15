package org.flowdev.parser.data;


public class SourceData {
    private String name;
    private String content;
    private int pos;
    private int wherePrevNl = -1;
    private int whereLine = 1;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getWherePrevNl() {
        return wherePrevNl;
    }

    public void setWherePrevNl(int wherePrevNl) {
        this.wherePrevNl = wherePrevNl;
    }

    public int getWhereLine() {
        return whereLine;
    }

    public void setWhereLine(int whereLine) {
        this.whereLine = whereLine;
    }
}
