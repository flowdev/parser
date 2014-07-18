package org.flowdev.parser.op;

import org.flowdev.parser.data.ParserData;

import static org.flowdev.parser.util.ParserUtil.fillResultMatched;
import static org.flowdev.parser.util.ParserUtil.fillResultUnmatched;


public class ParseLiteral<T> extends ParseSimple<T, ParseLiteral.ParseLiteralConfig> {
    public ParseLiteral(Params<T> params) {
        super(params);
    }

    @Override
    public void parseSimple(String substring, ParseLiteralConfig cfg, ParserData parserData) {
        if (substring.startsWith(cfg.getLiteral())) {
            fillResultMatched(parserData, cfg.getLiteral().length());
        } else {
            fillResultUnmatched(parserData, 0, "Literal '" + cfg.getLiteral() + "' expected.");
        }
    }

    public static class ParseLiteralConfig {
        private final String literal;

        public ParseLiteralConfig(String literal) {
            this.literal = literal;
        }

        public String getLiteral() {
            return literal;
        }
    }
}
