package org.flowdev.parser.op;


import org.flowdev.base.Port;
import org.flowdev.base.data.NoConfig;
import org.flowdev.base.op.Filter;
import org.flowdev.base.op.FilterOp;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.UseTextSemanticConfig;

import static org.flowdev.parser.op.ParseMultipleSync.ParseMultipleSyncConfig;

public class ParseMultiple1Sync<T> implements ParserOp<T, UseTextSemanticConfig> {
    private ParseMultipleSync<T> parseMultiple;
    private Port<UseTextSemanticConfig> configPort = (config) ->
            parseMultiple.getConfigPort().send(new ParseMultipleSyncConfig().min(1).max(Integer.MAX_VALUE).useTextSemantic(config.useTextSemantic()));

    public ParseMultiple1Sync(ParserParams<T> params) {
        parseMultiple = new ParseMultipleSync<>(params);
        Filter<T, NoConfig> semantics = new FilterOp<T, NoConfig>() {
            @Override
            protected void filter(T data) {
                ParserData parserData = params.getParserData.get(data);

                if (parserData.subResults() == null || parserData.subResults().isEmpty()) {
                    parserData.result().value(null);
                } else {
                    parserData.result().value(parserData.subResults().get(0).value());
                }

                outPort.send(params.setParserData.set(data, parserData));
            }
        };

        parseMultiple.setSemOutPort(semantics.getInPort());
        semantics.setOutPort(parseMultiple.getSemInPort());

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
