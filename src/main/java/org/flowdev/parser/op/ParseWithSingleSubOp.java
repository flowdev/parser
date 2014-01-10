package org.flowdev.parser.op;

import org.flowdev.base.Port;


public abstract class ParseWithSingleSubOp<T, C> extends ParseSimple<T, C> {
    private Port<T> subOutPort;
    private Port<T> subInPort = data -> outPort.send(data);

    public ParseWithSingleSubOp(Params<T> params) {
        super(params);
    }

    public void setSubOutPort(Port<T> subOutPort) {
        this.subOutPort = subOutPort;
    }

    public Port<T> getSubInPort() {
        return subInPort;
    }
}
