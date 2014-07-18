package org.flowdev.parser.op;

import org.flowdev.base.Getter;
import org.flowdev.base.Port;
import org.flowdev.base.Setter;
import org.flowdev.base.op.Filter;
import org.flowdev.parser.data.ParserData;

@SuppressWarnings("WeakerAccess")
public abstract class BaseParser<T, C> extends Filter<T, C> {
    public static class Params<T> {
        public Getter<T, ParserData> getParserData;
        public Setter<ParserData, T, T> setParserData;
    }

    protected Port<T> semOutPort;
    @SuppressWarnings("Convert2MethodRef")
    protected Port<T> semInPort = data -> outPort.send(data);

    protected final Params<T> params;

    protected BaseParser(Params<T> params) {
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
