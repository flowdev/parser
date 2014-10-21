package org.flowdev.parser.op;

import org.flowdev.base.Port;
import org.flowdev.base.op.FilterOp;
import org.flowdev.parser.data.ParserData;

import static org.flowdev.parser.util.ParserUtil.*;

public abstract class BaseParser<T, C> extends FilterOp<T, C> implements ParserOp<T, C> {
    protected T dataFromSemantics;

    protected Port<T> semOutPort;
    protected Port<T> semInPort = semInPort = (data) -> dataFromSemantics = data;

    protected final ParserParams<T> params;

    protected BaseParser(ParserParams<T> params) {
        this.params = params;
    }

    protected T handleSemantics(T data) {
        ParserData parserData = params.getParserData.get(data);
        parserData.result().feedback(collectFeedback(parserData.result(), parserData.subResults()));
        boolean matched = matched(parserData.result());
        if (matched && isOk(parserData.result())) {
            if (semOutPort != null) {
                semOutPort.send(data);
                data = dataFromSemantics;
                parserData = params.getParserData.get(data);
            } else {
                defaultSemantics(parserData);
            }
        }
        parserData.subResults(null);
        return params.setParserData.set(data, parserData);
    }

    protected void defaultSemantics(ParserData data) {
        data.result().value(data.result().text());
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
