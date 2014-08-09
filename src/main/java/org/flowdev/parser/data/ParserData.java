package org.flowdev.parser.data;

import java.util.List;


public class ParserData {
    private SourceData source;
    private ParseResult result;
    private List<ParseResult> subResults;


    public ParserData source(final SourceData source) {
        this.source = source;
        return this;
    }

    public ParserData result(final ParseResult result) {
        this.result = result;
        return this;
    }

    public ParserData subResults(final List<ParseResult> subResults) {
        this.subResults = subResults;
        return this;
    }

    public SourceData source() {
        return source;
    }

    public ParseResult result() {
        return result;
    }

    public List<ParseResult> subResults() {
        return subResults;
    }
}
