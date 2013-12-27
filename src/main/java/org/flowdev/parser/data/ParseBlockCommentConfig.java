package org.flowdev.parser.data;


public class ParseBlockCommentConfig {
    public String commentStart;
    public String commentEnd;

    public ParseBlockCommentConfig(String commentStart, String commentEnd) {
        this.commentStart = commentStart;
        this.commentEnd = commentEnd;
    }
}
