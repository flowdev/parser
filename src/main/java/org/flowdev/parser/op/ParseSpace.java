package org.flowdev.parser.op;

import org.flowdev.base.Getter;
import org.flowdev.base.Setter;
import org.flowdev.base.op.Filter;
import org.flowdev.parser.data.ParseSpaceConfig;
import org.flowdev.parser.data.ParserData;

/**
 * Created by obulbuk on 24.12.13.
 */
public class ParseSpace<T> extends Filter<T, ParseSpaceConfig> {
    public static class Params<T> {
        public Getter<T, ParserData> getParserData;
        public Setter<ParserData, T, T> setParserData;
    }

    private Params<T> params;

    public ParseSpace(Params<T> params) {
        this.params = params;
    }

    @Override
    protected void filter(T data) {
        ParserData parserData = params.getParserData.get(data);
        boolean includeNewline = getVolatileConfig().includeNewline;
        int orgPos = parserData.source.pos;
        int newPos = orgPos + parseSpace(parserData.source.content.substring(orgPos), includeNewline);
        if (orgPos != newPos) {
            parserData.result.pos = orgPos;
            parserData.result.text = parserData.source.content.substring(orgPos, newPos);
            parserData.result.matched = true;
            parserData.source.pos += newPos;
        } else {
            parserData.result.matched = false;
        }
        params.setParserData.set(data, parserData);
        outPort.send(data);
    }

    public int parseSpace(String substring, boolean includeNewline) {
        int idx = 0;
        while (isSpace(substring.codePointAt(idx), includeNewline)) {
            idx++;
        }
        return idx;
    }

    private boolean isSpace(int cp, boolean includeNewline) {
        return Character.isWhitespace(cp) && (includeNewline || cp != '\n');
    }
}
