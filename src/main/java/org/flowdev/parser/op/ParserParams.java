package org.flowdev.parser.op;

import org.flowdev.base.Getter;
import org.flowdev.base.Setter;
import org.flowdev.parser.data.ParserData;

public class ParserParams<T> {
    public Getter<T, ParserData> getParserData;
    public Setter<ParserData, T, T> setParserData;
}
