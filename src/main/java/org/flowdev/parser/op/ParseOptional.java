package org.flowdev.parser.op;


import org.flowdev.base.data.NoConfig;
import org.flowdev.base.op.Filter;
import org.flowdev.base.op.FilterOp;
import org.flowdev.parser.data.ParserData;

public class ParseOptional<T> extends ParseMultipleSync<T> {
    public ParseOptional(ParserParams<T> params) {
        super(params);

        Filter<T, NoConfig> semantics = new FilterOp<T, NoConfig>() {
            @Override
            protected void filter(T data) {
                ParserData parserData = params.getParserData.get(data);

                if (parserData.getSubResults() == null || parserData.getSubResults().isEmpty()) {
                    parserData.getResult().setValue(null);
                } else {
                    parserData.getResult().setValue(parserData.getSubResults().get(0).getValue());
                }

                outPort.send(params.setParserData.set(data, parserData));
            }
        };

        this.setSemOutPort(semantics.getInPort());
        semantics.setOutPort(this.getSemInPort());

        this.getConfigPort().send(new ParseMultipleSyncConfig(0, 1, false));
    }
}
