package org.flowdev.parser.op;


import org.flowdev.base.Port;
import org.flowdev.base.data.NoConfig;
import org.flowdev.base.op.Filter;
import org.flowdev.base.op.FilterOp;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.UseTextSemanticConfig;

import static org.flowdev.parser.op.ParseMultiple.ParseMultipleConfig;

public class ParseOptional<T> implements ParserOp<T, UseTextSemanticConfig> {
    private UseTextSemanticConfig config;
    private ParseMultiple<T> parseMultiple;
    private Port<UseTextSemanticConfig> configPort = (config) -> {
        this.config = config;
        parseMultiple.getConfigPort().send(new ParseMultipleConfig().min(0).max(1).useTextSemantic(config.useTextSemantic()));
    };

    public ParseOptional(ParserParams<T> params) {
        parseMultiple = new ParseMultiple<>(params);

        Filter<T, NoConfig> semantics = new FilterOp<T, NoConfig>() {
            @Override
            protected void filter(T data) {
                ParserData parserData = params.getParserData.get(data);

                if (config.useTextSemantic()) {
                    parserData.result().value(parserData.result().text());
                } else {
                    if (parserData.subResults() == null || parserData.subResults().isEmpty()) {
                        parserData.result().value(null);
                    } else {
                        parserData.result().value(parserData.subResults().get(0).value());
                    }
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
