package org.flowdev.parser.data;

import org.flowdev.base.data.Feedback;

import java.util.ArrayList;
import java.util.List;


public class ParserData {
    private Feedback feedback;
    private SourceData source;
    private ParseResult result;
    private List<ParseResult> subResults;
    private List<ParserTempData> tempStack = new ArrayList<>(128);


    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

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

    public List<ParserTempData> getTempStack() {
        return tempStack;
    }

    public void setTempStack(List<ParserTempData> tempStack) {
        this.tempStack = tempStack;
    }
}
