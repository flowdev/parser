package org.flowdev.parser.op;


import org.flowdev.base.Port;
import org.flowdev.parser.data.UseTextSemanticConfig;

import static org.flowdev.parser.op.ParseMultiple.ParseMultipleSyncConfig;

public class ParseOptional<T> implements ParserOp<T, UseTextSemanticConfig> {
    private ParseMultiple<T> parseMultiple;
    private Port<UseTextSemanticConfig> configPort = (config) ->
            parseMultiple.getConfigPort().send(new ParseMultipleSyncConfig().min(0).max(1).useTextSemantic(config.useTextSemantic()));

    public ParseOptional(ParserParams<T> params) {
        parseMultiple = new ParseMultiple<>(params);

        this.getConfigPort().send(new UseTextSemanticConfig().useTextSemantic(false));
    }

    public Port<T> getSubInPort() {
        return parseMultiple.getSubInPort();
    }

    public void setSubOutPort(Port<T> subOutPort) {
        parseMultiple.setSubOutPort(subOutPort);
    }

    @Override
    public Port<T> getSemInPort() {
        return parseMultiple.getSemInPort();
    }

    @Override
    public void setSemOutPort(Port<T> semOutPort) {
        parseMultiple.setSemOutPort(semOutPort);
    }

    @Override
    public Port<T> getInPort() {
        return parseMultiple.getInPort();
    }

    @Override
    public void setOutPort(Port<T> outPort) {
        parseMultiple.setOutPort(outPort);
    }

    @Override
    public Port<UseTextSemanticConfig> getConfigPort() {
        return configPort;
    }

    @Override
    public void setErrorPort(Port<Throwable> port) {
        parseMultiple.setErrorPort(port);
    }
}
