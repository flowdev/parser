package org.flowdev.parser.data;


public class SourceData {
    private String name;
    private String content;
    private int pos;
    private int wherePrevNl = -1;
    private int whereLine = 1;


    public SourceData name(final String name) {
        this.name = name;
        return this;
    }

    public SourceData content(final String content) {
        this.content = content;
        return this;
    }

    public SourceData pos(final int pos) {
        this.pos = pos;
        return this;
    }

    public SourceData wherePrevNl(final int wherePrevNl) {
        this.wherePrevNl = wherePrevNl;
        return this;
    }

    public SourceData whereLine(final int whereLine) {
        this.whereLine = whereLine;
        return this;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String content() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int pos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int wherePrevNl() {
        return wherePrevNl;
    }

    public void setWherePrevNl(int wherePrevNl) {
        this.wherePrevNl = wherePrevNl;
    }

    public int whereLine() {
        return whereLine;
    }

    public void setWhereLine(int whereLine) {
        this.whereLine = whereLine;
    }
}
