package org.flowdev.parser.op;

import org.flowdev.base.Port;
import org.flowdev.base.op.FilterOp;
import org.flowdev.parser.data.ParserData;

import static org.flowdev.parser.util.ParserUtil.matched;

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
        boolean matched = matched(parserData.getResult());
        if (matched) {
            if (semOutPort != null) {
                semOutPort.send(data);
                data = dataFromSemantics;
                parserData = params.getParserData.get(data);
            } else {
                defaultSemantics(parserData);
            }
        }
        parserData.setSubResults(null);
        return params.setParserData.set(data, parserData);
    }

    protected void defaultSemantics(ParserData data) {
        data.getResult().value(data.getResult().text());
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
