package org.flowdev.parser.op;


public class ParseMultiple0<T> extends ParseMultipleSync<T> {
    public ParseMultiple0(ParserParams<T> params) {
        super(params);
        this.getConfigPort().send(new ParseMultipleSyncConfig(0, Integer.MAX_VALUE, false));
    }
}
