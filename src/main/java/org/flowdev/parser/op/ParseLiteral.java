package org.flowdev.parser.op;

import org.flowdev.parser.data.ParserData;

import static org.flowdev.parser.util.ParserUtil.fillResultMatched;
import static org.flowdev.parser.util.ParserUtil.fillResultUnmatched;


public class ParseLiteral<T> extends ParseSimple<T, ParseLiteral.ParseLiteralConfig> {
    public ParseLiteral(ParserParams<T> params) {
        super(params);
    }

    @Override
    public void parseSimple(String substring, ParseLiteralConfig cfg, ParserData parserData) {
        String literal = cfg.literal();
        if (substring.startsWith(literal)) {
            fillResultMatched(parserData, literal.length());
        } else {
            fillResultUnmatched(parserData, 0, "Literal '" + literal + "' expected.");
        }
    }

    public static class ParseLiteralConfig {
        private String literal;

        public ParseLiteralConfig literal(final String literal) {
            this.literal = literal;
            return this;
        }

        public String literal() {
            return literal;
        }
    }
}
