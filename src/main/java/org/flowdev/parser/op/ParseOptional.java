package org.flowdev.parser.op;

import org.flowdev.base.Port;
import org.flowdev.parser.data.ParseMultipleConfig;


public class ParseOptional<T> {
    private ParseMultiple parser;

    public ParseOptional() {
        parser.getConfigPort().send(new ParseMultipleConfig(0, 1));
    }

    public Port<T> getSubInPort() {
        return parser.getSubInPort();
    }

    public void setSubOutPort(Port<T> subOutPort) {
        parser.setSubOutPort(subOutPort);
    }

    public Port<T> getSemInPort() {
        return parser.getSemInPort();
    }

    public void setSemOutPort(Port<T> semOutPort) {
        parser.setSemOutPort(semOutPort);
    }

    public Port<T> getInPort() {
        return parser.getInPort();
    }

    public void setOutPort(Port<T> outPort) {
        parser.setOutPort(outPort);
    }
}
