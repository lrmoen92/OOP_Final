package com.astontech.bo;

/**
 * Created by Logan.Moen on 6/13/2017.
 */
public class File {
    private int FileId;
    private String FileName;
    private String FileType;
    private Long FileSize;
    private String FilePath;
    private int DirId;

    public File(){}

    public File(int fileId, String fileName, String fileType, Long fileSize, String filePath, int dirId){
        this.setFileId(fileId);
        this.setFileName(fileName);
        this.setFileType(fileType);
        this.setFileSize(fileSize);
        this.setFilePath(filePath);
        this.setDirId(dirId);
    }


    public int getFileId() {
        return FileId;
    }

    public void setFileId(int fileId) {
        FileId = fileId;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileType() {
        return FileType;
    }

    public void setFileType(String fileType) {
        FileType = fileType;
    }

    public Long getFileSize() {
        return FileSize;
    }

    public void setFileSize(Long fileSize) {
        FileSize = fileSize;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public int getDirId() {
        return DirId;
    }

    public void setDirId(int dirId) {
        DirId = dirId;
    }
}
