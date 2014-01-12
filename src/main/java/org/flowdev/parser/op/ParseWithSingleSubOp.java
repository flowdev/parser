package org.flowdev.parser.op;

import org.flowdev.base.Port;


public abstract class ParseWithSingleSubOp<T, C> extends ParseSimple<T, C> {
    protected Port<T> subOutPort;
    protected Port<T> subInPort = this::handleSubOpData;

    public ParseWithSingleSubOp(Params<T> params) {
        super(params);
    }

    protected abstract void handleSubOpData(T data);

    public void setSubOutPort(Port<T> subOutPort) {
        this.subOutPort = subOutPort;
    }

    public Port<T> getSubInPort() {
        return subInPort;
    }
}
