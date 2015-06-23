package org.flowdev.parser.op;

import org.flowdev.base.Port;
import org.flowdev.parser.data.UseTextSemanticConfig;

import static org.flowdev.parser.op.ParseMultiple.ParseMultipleConfig;

public class ParseMultiple0<T> implements ParserOp<T, UseTextSemanticConfig> {
    private ParseMultiple<T> parseMultiple;
    private Port<UseTextSemanticConfig> configPort = (config) ->
            parseMultiple.getConfigPort().send(new ParseMultipleConfig().min(0).max(Integer.MAX_VALUE).useTextSemantic(config.useTextSemantic()));

    public ParseMultiple0(ParserParams<T> params) {
        parseMultiple = new ParseMultiple<>(params);
        this.getConfigPort().send(new UseTextSemanticConfig().useTextSemantic(false));
    }

    public Port<T> getSubInPort() {
        return parseMultiple.getSubInPort();
    }

    public void setSubOutPort(Port<T> subOutPort) {
        parseMultiple.setSubOutPort(subOutPort);
    }

    public ParseMultiple0<T> withSubOutPort(Port<T> subOutPort) {
        parseMultiple.setSubOutPort(subOutPort);
        return this;
    }

    @Override
    public Port<T> getSemInPort() {
        return parseMultiple.getSemInPort();
    }

    @Override
    public void setSemOutPort(Port<T> semOutPort) {
        parseMultiple.setSemOutPort(semOutPort);
    }

    public ParseMultiple0<T> withSemOutPort(Port<T> semOutPort) {
        parseMultiple.setSemOutPort(semOutPort);
        return this;
    }

    @Override
    public Port<T> getInPort() {
        return parseMultiple.getInPort();
    }

    @Override
    public void setOutPort(Port<T> outPort) {
        parseMultiple.setOutPort(outPort);
    }

    public ParseMultiple0<T> withOutPort(Port<T> outPort) {
        parseMultiple.setOutPort(outPort);
        return this;
    }

    @Override
    public Port<UseTextSemanticConfig> getConfigPort() {
        return configPort;
    }

    @Override
    public void setErrorPort(Port<Throwable> port) {
        parseMultiple.setErrorPort(port);
    }

    public ParseMultiple0<T> withErrorPort(Port<Throwable> port) {
        parseMultiple.setErrorPort(port);
        return this;
    }
}
