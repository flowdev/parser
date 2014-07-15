package org.flowdev.parser.op;


public class ParseMultiple1<T> extends ParseMultiple<T> {
    public ParseMultiple1(Params<T> params) {
        super(params);
        this.getConfigPort().send(new ParseMultipleConfig(1, Integer.MAX_VALUE));
    }
}
