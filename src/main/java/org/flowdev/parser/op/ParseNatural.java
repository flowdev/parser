package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseNaturalConfig;
import org.flowdev.parser.data.ParserData;

import static org.flowdev.parser.util.ParserUtil.fillResultMatched;
import static org.flowdev.parser.util.ParserUtil.fillResultUnmatched;


public class ParseNatural<T> extends ParseSimple<T, ParseNaturalConfig> {
    public ParseNatural(Params<T> params) {
        super(params);
    }

    @Override
    public void parseSimple(String substring, ParseNaturalConfig cfg, ParserData parserData) {
        int i;
        for (i = 0; i < substring.length() && Character.digit(substring.charAt(i), cfg.radix) >= 0; i++) {
            // nothing to do!
        }
        if (i > 0) {
            try {
                parserData.result.value = Long.parseUnsignedLong(substring.substring(0, i), cfg.radix);
                fillResultMatched(parserData, i);
            } catch (NumberFormatException nfe) {
                fillResultUnmatched(parserData, 0, "NumberFormatException " + nfe.getMessage());
            }
        } else {
            fillResultUnmatched(parserData, 0, "Natural number expected.");
        }
    }
}
