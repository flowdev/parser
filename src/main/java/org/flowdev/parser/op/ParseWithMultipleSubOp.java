package org.flowdev.parser.op;

import org.flowdev.base.Port;
import org.flowdev.parser.data.ParserData;

import java.util.ArrayList;
import java.util.List;


public abstract class ParseWithMultipleSubOp<T, C> extends ParseSimple<T, C> {
    protected List<Port<T>> subOutPorts = new ArrayList<>();
    protected Port<T> subInPort = this::handleSubOpData;

    public ParseWithMultipleSubOp(ParserParams<T> params) {
        super(params);
    }

    protected abstract void handleSubOpData(T data);

    public void setSubOutPort(int i, Port<T> subOutPort) {
        if (i == subOutPorts.size()) {
            this.subOutPorts.add(subOutPort);
        } else {
            this.subOutPorts.set(i, subOutPort);
        }
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
