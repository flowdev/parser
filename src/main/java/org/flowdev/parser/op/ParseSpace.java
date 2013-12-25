package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseSpaceConfig;
import org.flowdev.parser.data.ParserData;


public class ParseSpace<T> extends ParseSimple<T, ParseSpaceConfig> {
    public ParseSpace(Params<T> params) {
        super(params);
    }

    @Override
    public int parseSimple(String substring, ParseSpaceConfig cfg, ParserData parserData) {
        int idx = 0;
        while (isSpace(substring.charAt(idx), cfg.includeNewline)) {
            idx++;
        }
        return idx;
    }

    private boolean isSpace(char c, boolean includeNewline) {
        return Character.isWhitespace(c) && (includeNewline || c != '\n');
    }
}
