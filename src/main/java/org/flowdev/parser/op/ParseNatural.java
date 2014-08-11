package org.flowdev.parser.op;

import org.flowdev.parser.data.ParserData;

import static org.flowdev.parser.util.ParserUtil.fillResultMatched;
import static org.flowdev.parser.util.ParserUtil.fillResultUnmatched;


public class ParseNatural<T> extends ParseSimple<T, ParseNatural.ParseNaturalConfig> {
    public ParseNatural(ParserParams<T> params) {
        super(params);
        getConfigPort().send(new ParseNaturalConfig().radix(10));
    }

    @Override
    public void parseSimple(String substring, ParseNaturalConfig cfg, ParserData parserData) {
        int i;
        int radix = cfg.radix();

        //noinspection StatementWithEmptyBody
        for (i = 0; i < substring.length() && Character.digit(substring.charAt(i), radix) >= 0; i++) {
            // nothing to do!
        }
        if (i > 0) {
            try {
                parserData.result().value(Long.parseUnsignedLong(substring.substring(0, i), radix));
                fillResultMatched(parserData, i);
            } catch (NumberFormatException nfe) {
                fillResultUnmatched(parserData, 0, "NumberFormatException " + nfe.getMessage());
            }
        } else {
            fillResultUnmatched(parserData, 0, "Natural number expected.");
        }
    }

    @Override
    protected void defaultSemantics(ParserData data) {
        // intentionally empty: value has been set already by parseSimple!!!
    }

    public static class ParseNaturalConfig {
        private int radix;

        public ParseNaturalConfig radix(final int radix) {
            this.radix = radix;
            return this;
        }

        public int radix() {
            return radix;
        }
    }
}
