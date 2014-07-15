package org.flowdev.parser.op;


public class ParseMultiple0<T> extends ParseMultiple<T> {
    public ParseMultiple0(Params<T> params) {
        super(params);
        this.getConfigPort().send(new ParseMultipleConfig(0, Integer.MAX_VALUE));
    }
}
