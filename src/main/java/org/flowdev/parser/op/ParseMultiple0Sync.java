package org.flowdev.parser.op;


import org.flowdev.base.Port;
import org.flowdev.parser.data.UseTextSemanticConfig;

import static org.flowdev.parser.op.ParseMultipleSync.ParseMultipleSyncConfig;

public class ParseMultiple0Sync<T> implements ParserOp<T, UseTextSemanticConfig> {
    private ParseMultipleSync<T> parseMultiple;
    private Port<UseTextSemanticConfig> configPort = (data) ->
            parseMultiple.getConfigPort().send(new ParseMultipleSyncConfig(0, Integer.MAX_VALUE, data.useTextSemantic()));

    public ParseMultiple0Sync(ParserParams<T> params) {
        parseMultiple = new ParseMultipleSync<>(params);
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