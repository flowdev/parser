package org.flowdev.parser.op;

import org.flowdev.parser.data.ParserData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.flowdev.parser.util.ParserUtil.fillResultMatched;
import static org.flowdev.parser.util.ParserUtil.fillResultUnmatched;


public class ParseRegex<T> extends ParseSimple<T, ParseRegex.ParseRegexConfig> {
    private String regexStr;
    private Pattern regex;

    public ParseRegex(ParserParams<T> params) {
        super(params);
    }

    @Override
    public void parseSimple(String substring, ParseRegexConfig cfg, ParserData parserData) {
        updateRegex(cfg.getRegex());
        Matcher matcher = regex.matcher(substring);
        if (matcher.lookingAt()) {
            fillResultMatched(parserData, matcher.end());
        } else {
            fillResultUnmatched(parserData, 0, "Regex '" + cfg.getRegex() + "' doesn't match.");
        }
    }

    private void updateRegex(String curRegex) {
        // intentionally NOT using String.equals()!
        if (curRegex != this.regexStr) {
            this.regex = Pattern.compile(curRegex);
            this.regexStr = curRegex;
        }
    }

    public static class ParseRegexConfig {
        private final String regex;

        public ParseRegexConfig(String regex) {
            this.regex = regex;
        }

        public String getRegex() {
            return regex;
        }
    }
}
