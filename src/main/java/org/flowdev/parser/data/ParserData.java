package org.flowdev.parser.data;

import java.util.List;


public class ParserData {
    private SourceData source;
    private ParseResult result;
    private List<ParseResult> subResults;

    public SourceData getSource() {
        return source;
    }

    public void setSource(SourceData source) {
        this.source = source;
    }

    public ParseResult getResult() {
        return result;
    }

    public void setResult(ParseResult result) {
        this.result = result;
    }

    public List<ParseResult> getSubResults() {
        return subResults;
    }

    public void setSubResults(List<ParseResult> subResults) {
        this.subResults = subResults;
    }
}
