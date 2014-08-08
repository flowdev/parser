package org.flowdev.parser.op;

import org.flowdev.base.Port;
import org.flowdev.parser.data.ParserData;


public abstract class ParseWithSingleSubOp<T, C> extends ParseSimple<T, C> {
    protected Port<T> subOutPort;
    protected Port<T> subInPort = this::handleSubOpData;

    public ParseWithSingleSubOp(ParserParams<T> params) {
        super(params);
    }

    protected abstract void handleSubOpData(T data);

    public void setSubOutPort(Port<T> subOutPort) {
        this.subOutPort = subOutPort;
    }

    public Port<T> getSubInPort() {
        return subInPort;
    }

    @Override
    protected void defaultSemantics(ParserData data) {
        // intentionally empty: super class is doing it all
    }

    @Override
    public void parseSimple(String substring, C cfg, ParserData parserData) {
        throw new UnsupportedOperationException("The filter method should handle everything itself!");
    }
}
