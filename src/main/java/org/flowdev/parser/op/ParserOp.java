package org.flowdev.parser.op;

import org.flowdev.base.Port;
import org.flowdev.base.op.Filter;

public interface ParserOp<T, C> extends Filter<T, C> {
    Port<T> getSemInPort();

    void setSemOutPort(Port<T> semOutPort);
}
