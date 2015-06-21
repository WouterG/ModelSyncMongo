package net.wouto.modelsync.mongo.query;

public enum RegexOption {
    CASE_INSENSITIVE(2),
    INCLUDE_ANCHORS(8),
    IGNORE_WHITESPACES(4),
    DOT_MATCHES_EVERY_CHARACTER(32);
    
    private int c;
    
    private RegexOption(int c) {
        this.c = c;
    }
    
    public int getFlag() {
        return this.c;
    }
}
