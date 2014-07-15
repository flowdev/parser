package org.flowdev.parser.data;


import org.flowdev.base.data.Feedback;

public class ParseResult {
    private int pos;
    private String text;
    private Object value;
    private int errPos;
    private Feedback feedback;


    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getErrPos() {
        return errPos;
    }

    public void setErrPos(int errPos) {
        this.errPos = errPos;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }
}
