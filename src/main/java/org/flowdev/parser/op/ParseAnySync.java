package org.flowdev.parser.op;

import org.flowdev.parser.data.ParserData;

public abstract class ParseAnySync<T, C> extends BaseParser<T, C> {
    protected ParseAnySync(ParserParams<T> params) {
        super(params);
    }

    @Override
    protected void filter(T data) {
        C cfg = getVolatileConfig();

        data = parseAnySync(data, cfg);

        outPort.send(handleSemantics(data));
    }

    protected void defaultSemantics(ParserData data) {
        super.defaultSemantics(data);
    }

    public abstract T parseAnySync(T data, C cfg);
}
