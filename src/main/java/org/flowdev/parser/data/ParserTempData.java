package org.flowdev.parser.data;

import java.util.ArrayList;
import java.util.List;

public class ParserTempData {
    public int orgSrcPos;
    public List<ParseResult> subResults;

    public ParserTempData(int orgSrcPos) {
        this.orgSrcPos = orgSrcPos;
        this.subResults = new ArrayList<>(128);
    }
}
