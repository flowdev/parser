package org.flowdev.parser.op;

import org.flowdev.parser.data.ParserData;

import static org.flowdev.parser.util.ParserUtil.fillResultMatched;
import static org.flowdev.parser.util.ParserUtil.fillResultUnmatched;


public class ParseSpace<T> extends ParseSimple<T, ParseSpace.ParseSpaceConfig> {
    public ParseSpace(Params<T> params) {
        super(params);
    }

    @Override
    public void parseSimple(String substring, ParseSpaceConfig cfg, ParserData parserData) {
        int idx = 0;
        while (substring.length() > idx && isSpace(substring.charAt(idx), cfg.includeNewline)) {
            idx++;
        }
        if (idx > 0) {
            fillResultMatched(parserData, idx);
        } else {
            fillResultUnmatched(parserData, idx, "Space expected.");
        }
    }

    private boolean isSpace(char c, boolean includeNewline) {
        return Character.isWhitespace(c) && (includeNewline || c != '\n');
    }

    public static class ParseSpaceConfig {
        public boolean includeNewline;

        public ParseSpaceConfig(boolean includeNewline) {
            this.includeNewline = includeNewline;
        }
    }
}
