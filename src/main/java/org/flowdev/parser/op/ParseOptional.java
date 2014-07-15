package org.flowdev.parser.op;


public class ParseOptional<T> extends ParseMultiple<T> {
    public ParseOptional(Params<T> params) {
        super(params);
        this.getConfigPort().send(new ParseMultipleConfig(0, 1));
    }
}
