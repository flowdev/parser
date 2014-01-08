package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseNaturalConfig;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.util.ParserUtil;


public class ParseNatural<T> extends ParseSimple<T, ParseNaturalConfig> {
    public ParseNatural(Params<T> params) {
        super(params);
    }

    @Override
    public int parseSimple(String substring, ParseNaturalConfig cfg, ParserData parserData) {
        int i;
        for (i = 0; i < substring.length() && Character.digit(substring.charAt(i), cfg.radix) >= 0; i++) {
            // nothing to do!
        }
        if (i > 0) {
            try {
                parserData.result.value = Long.parseUnsignedLong(substring.substring(0, i), cfg.radix);
            } catch (NumberFormatException nfe) {
                ParserUtil.addError(parserData, parserData.source.pos, nfe.getMessage());
                i = 0;
            }
        }
        return i;
    }
}
