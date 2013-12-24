package org.flowdev.parser.op;

import org.flowdev.base.Getter;
import org.flowdev.base.op.Filter;
import org.flowdev.parser.data.ParseLiteralConfig;
import org.flowdev.parser.data.ParserData;

/**
 * Created by obulbuk on 24.12.13.
 */
public class ParseLiteral<T> extends Filter<T, ParseLiteralConfig> {
    public static class Params<T> {
        Getter<T, ParserData> getParserData;
    }

    @Override
    protected void filter(T data) {

    }
}
