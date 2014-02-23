package org.flowdev.parser.op;

import org.flowdev.base.data.EmptyConfig;
import org.flowdev.parser.data.ParserData;

import static org.flowdev.parser.util.ParserUtil.fillResultMatched;
import static org.flowdev.parser.util.ParserUtil.fillResultUnmatched;


public class ParseEof<T> extends ParseSimple<T, EmptyConfig> {
    public ParseEof(Params<T> params) {
        super(params);
    }

    @Override
    public void parseSimple(String substring, EmptyConfig cfg, ParserData parserData) {
        if (substring.length() == 0) {
            fillResultMatched(parserData, 0);
        } else {
            fillResultUnmatched(parserData, 0, "Expecting end of input but got " + substring.length()
                    + " characters of input left.");
        }
    }
}
