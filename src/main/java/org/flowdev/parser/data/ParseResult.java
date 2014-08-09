package org.flowdev.parser.data;


import org.flowdev.base.data.Feedback;

public class ParseResult {
    private int pos;
    private String text;
    private Object value;
    private int errPos;
    private Feedback feedback;


    public ParseResult pos(final int pos) {
        this.pos = pos;
        return this;
    }

    public ParseResult text(final String text) {
        this.text = text;
        return this;
    }

    public ParseResult value(final Object value) {
        this.value = value;
        return this;
    }

    public ParseResult errPos(final int errPos) {
        this.errPos = errPos;
        return this;
    }

    public ParseResult feedback(final Feedback feedback) {
        this.feedback = feedback;
        return this;
    }

    public int pos() {
        return pos;
    }

    public String text() {
        return text;
    }

    public Object value() {
        return value;
    }

    public int errPos() {
        return errPos;
    }

    public Feedback feedback() {
        return feedback;
    }
}
