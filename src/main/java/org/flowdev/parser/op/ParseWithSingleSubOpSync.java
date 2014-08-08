package org.flowdev.parser.op;

import org.flowdev.base.Port;


public abstract class ParseWithSingleSubOpSync<T, C> extends ParseAnySync<T, C> {
    protected T dataFromSubOp;
    protected Port<T> subOutPort;
    protected Port<T> subInPort = (data) -> dataFromSubOp = data;

    public ParseWithSingleSubOpSync(ParserParams<T> params) {
        super(params);
    }

    public void setSubOutPort(Port<T> subOutPort) {
        this.subOutPort = subOutPort;
    }

    public Port<T> getSubInPort() {
        return subInPort;
    }
}
