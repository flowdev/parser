package org.flowdev.parser.op;

import org.flowdev.base.Port;
import org.flowdev.base.op.FilterOp;

public abstract class BaseParser<T, C> extends FilterOp<T, C> implements ParserOp<T, C> {
    protected Port<T> semOutPort;
    @SuppressWarnings("Convert2MethodRef")
    protected Port<T> semInPort = data -> outPort.send(data);

    protected final ParserParams<T> params;

    protected BaseParser(ParserParams<T> params) {
        this.params = params;
    }

    /**
     * Called during initialization phase.
     */
    public Port<T> getSemInPort() {
        return semInPort;
    }

    /**
     * Called during initialization phase.
     */
    public void setSemOutPort(Port<T> semOutPort) {
        this.semOutPort = semOutPort;
    }
}
