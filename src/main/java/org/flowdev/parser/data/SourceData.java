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

    public String content() {
        return content;
    }

    public int pos() {
        return pos;
    }

    public int wherePrevNl() {
        return wherePrevNl;
    }

    public int whereLine() {
        return whereLine;
    }

}
