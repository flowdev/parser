package org.flowdev.parser.op;

import org.flowdev.base.data.NoConfig;
import org.flowdev.parser.data.ParserData;

import static org.flowdev.parser.util.ParserUtil.fillResultMatched;
import static org.flowdev.parser.util.ParserUtil.fillResultUnmatched;


public class ParseEof<T> extends ParseSimple<T, NoConfig> {
    public ParseEof(ParserParams<T> params) {
        super(params);
    }

    @Override
    public void parseSimple(String substring, NoConfig cfg, ParserData parserData) {
        if (substring.length() == 0) {
            fillResultMatched(parserData, 0);
        } else {
            fillResultUnmatched(parserData, 0, "Expecting end of input but got " + substring.length()
                    + " characters of input left.");
        }
    }

    @Override
    protected void defaultSemantics(ParserData data) {
        data.result().value("");
    }
}
