package org.flowdev.parser.data;

public class UseTextSemanticConfig {
    private boolean useTextSemantic;

    public boolean useTextSemantic() {
        return useTextSemantic;
    }

    public UseTextSemanticConfig useTextSemantic(boolean useTextSemantic) {
        this.useTextSemantic = useTextSemantic;
        return this;
    }
}
