package org.flowdev.parser.op;


public class ParseMultiple1<T> extends ParseMultipleSync<T> {
    public ParseMultiple1(ParserParams<T> params) {
        super(params);
        this.getConfigPort().send(new ParseMultipleSyncConfig(1, Integer.MAX_VALUE, false));
    }
}
