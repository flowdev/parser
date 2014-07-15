package org.flowdev.parser.data;

import java.util.ArrayList;
import java.util.List;

public class ParserTempData {
    private int orgSrcPos;
    private List<ParseResult> subResults;

    public ParserTempData(int orgSrcPos) {
        this.orgSrcPos = orgSrcPos;
        this.subResults = new ArrayList<>(128);
    }


    public int getOrgSrcPos() {
        return orgSrcPos;
    }

    public void setOrgSrcPos(int orgSrcPos) {
        this.orgSrcPos = orgSrcPos;
    }

    public List<ParseResult> getSubResults() {
        return subResults;
    }

    public void setSubResults(List<ParseResult> subResults) {
        this.subResults = subResults;
    }
}
