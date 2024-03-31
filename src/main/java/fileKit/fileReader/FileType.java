package fileKit.fileReader;

import baseKit.StrKit;

public enum FileType {
    /**
     * docx etc.
     */
    DOCX,
    DOC,
    DOT,
    DBF,
    XLS,
    XLSX,
    PDF,
    CSV,
    TXT,
    ;
    public static FileType getBySuffix(String suffix){
        for (FileType value : values()) {
            if (StrKit.equalsIgnoreCase(value.name(),suffix)){
                return value;
            }
        }
        return null;
    }
}

