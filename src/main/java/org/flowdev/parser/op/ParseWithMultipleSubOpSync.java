package org.flowdev.parser.op;

import org.flowdev.base.Port;

import java.util.ArrayList;
import java.util.List;


public abstract class ParseWithMultipleSubOpSync<T, C> extends ParseAnySync<T, C> {
    protected T dataFromSubOp;
    protected List<Port<T>> subOutPorts = new ArrayList<>();
    protected Port<T> subInPort = (data) -> dataFromSubOp = data;

    public ParseWithMultipleSubOpSync(ParserParams<T> params) {
        super(params);
    }

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
}
