package org.flowdev.parser.data;


import org.flowdev.base.data.Feedback;

public class ParseResult {
    public int pos;
    public String text;
    public Object value;
    public int errPos;
    public Feedback feedback;
}
