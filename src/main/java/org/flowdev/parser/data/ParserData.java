package org.flowdev.parser.data;

import org.flowdev.base.data.Feedback;

import java.util.List;

/**
 * Created by obulbuk on 24.12.13.
 */
public class ParserData {
    public Feedback feedback;
    public SourceData source;
    public ParseResult result;
    public List<ParseResult> subResults;
}
