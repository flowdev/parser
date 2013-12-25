package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseRegexConfig;
import org.flowdev.parser.data.ParserData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ParseRegex<T> extends ParseSimple<T, ParseRegexConfig> {
    private String regexStr;
    private Pattern regex;

    public ParseRegex(Params<T> params) {
        super(params);
    }

    @Override
    public int parseSimple(String substring, ParseRegexConfig cfg, ParserData parserData) {
        updateRegex(cfg.regex);
        Matcher matcher = regex.matcher(substring);
        if (matcher.lookingAt()) {
            return matcher.end();
        } else {
            return 0;
        }
    }

    private void updateRegex(String curRegex) {
        // intentionally NOT using String.equals()!
        if (curRegex != this.regexStr) {
            this.regex = Pattern.compile(curRegex);
            this.regexStr = curRegex;
        }
    }
}
