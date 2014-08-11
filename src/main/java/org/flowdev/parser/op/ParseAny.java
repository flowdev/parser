package org.flowdev.parser.op;

import org.flowdev.parser.data.ParserData;

public abstract class ParseAny<T, C> extends BaseParser<T, C> {
    protected ParseAny(ParserParams<T> params) {
        super(params);
    }

    @Override
    protected void filter(T data) {
        C cfg = getVolatileConfig();

        data = parseAny(data, cfg);

        outPort.send(handleSemantics(data));
    }

    protected void defaultSemantics(ParserData data) {
        super.defaultSemantics(data);
    }

    public abstract T parseAny(T data, C cfg);
}
