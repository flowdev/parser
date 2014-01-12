package org.flowdev.parser.data;

import org.flowdev.base.data.Feedback;

import java.util.List;


public class ParserData {
    public Feedback feedback;
    public SourceData source;
    public ParseResult result;
    public List<ParseResult> subResults;
    public List<ParserTempData> tempStack;
}
