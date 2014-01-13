package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseMultipleConfig;


public class ParseMultiple1<T> extends ParseMultiple<T> {
    public ParseMultiple1(Params params) {
        super(params);
        this.getConfigPort().send(new ParseMultipleConfig(1, Integer.MAX_VALUE));
    }
}
