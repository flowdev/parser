package org.flowdev.parser.op;

import org.flowdev.base.Port;

import java.util.ArrayList;
import java.util.List;


public abstract class ParseWithMultipleSubOp<T, C> extends ParseSimple<T, C> {
    protected List<Port<T>> subOutPorts = new ArrayList<>();
    protected Port<T> subInPort = this::handleSubOpData;

    public ParseWithMultipleSubOp(Params<T> params) {
        super(params);
    }

    protected abstract void handleSubOpData(T data);

    public void setSubOutPort(int i, Port<T> subOutPort) {
        this.subOutPorts.set(i, subOutPort);
    }

    public Port<T> getSubInPort() {
        return subInPort;
    }
}
