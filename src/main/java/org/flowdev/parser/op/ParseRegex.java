package org.flowdev.parser.op;

import org.flowdev.base.Getter;
import org.flowdev.base.Setter;
import org.flowdev.base.op.Filter;
import org.flowdev.parser.data.ParseRegexConfig;
import org.flowdev.parser.data.ParserData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by obulbuk on 24.12.13.
 */
public class ParseRegex<T> extends Filter<T, ParseRegexConfig> {
    public static class Params<T> {
        public Getter<T, ParserData> getParserData;
        public Setter<ParserData, T, T> setParserData;
    }

    private Params<T> params;
    private String regexStr;
    private Pattern regex;

    public ParseRegex(Params<T> params) {
        this.params = params;
    }

    @Override
    protected void filter(T data) {
        ParserData parserData = params.getParserData.get(data);
        updateRegex(getVolatileConfig().regex);
        int orgPos = parserData.source.pos;
        String substr = parserData.source.content.substring(orgPos);
        Matcher matcher = regex.matcher(substr);
        if (matcher.lookingAt()) {
            parserData.result.pos = orgPos;
            int start = matcher.start();
            int end = matcher.end();
            parserData.result.text = substr.substring(start, end);
            parserData.result.matched = true;
            parserData.source.pos += parserData.result.text.length();
        } else {
            parserData.result.matched = false;
        }
        params.setParserData.set(data, parserData);
        outPort.send(data);
    }

    private void updateRegex(String curRegex) {
        if (curRegex != this.regexStr) {
            this.regex = Pattern.compile(curRegex);
            this.regexStr = curRegex;
        }
    }
}
