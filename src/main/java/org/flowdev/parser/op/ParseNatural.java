package org.flowdev.parser.op;

import org.flowdev.parser.data.ParserData;

import static org.flowdev.parser.util.ParserUtil.fillResultMatched;
import static org.flowdev.parser.util.ParserUtil.fillResultUnmatched;


public class ParseNatural<T> extends ParseSimple<T, ParseNatural.ParseNaturalConfig> {
    public ParseNatural(ParserParams<T> params) {
        super(params);
    }

    @Override
    public void parseSimple(String substring, ParseNaturalConfig cfg, ParserData parserData) {
        int i;
        int radix = 10;
        if (cfg != null) {
            radix = cfg.getRadix();
        }

        //noinspection StatementWithEmptyBody
        for (i = 0; i < substring.length() && Character.digit(substring.charAt(i), radix) >= 0; i++) {
            // nothing to do!
        }
        if (i > 0) {
            try {
                parserData.getResult().value(Long.parseUnsignedLong(substring.substring(0, i), radix));
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
        private final int radix;

        public ParseNaturalConfig(int r) {
            this.radix = r;
        }

        public int getRadix() {
            return radix;
        }
    }
}
