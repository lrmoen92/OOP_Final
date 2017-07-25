package com.astontech.bo;

/**
 * Created by Logan.Moen on 6/13/2017.
 */
public class Directory {

    private int DirId;
    private String DirName;
    private Float DirSize;
    private int NumberOfFiles;
    private String DirPath;

    public Directory(){}

    public Directory(int dirId, String dirName, Float dirSize, int numberOfFiles, String dirPath){
        this.setDirId(dirId);
        this.setDirName(dirName);
        this.setDirSize(dirSize);
        this.setNumberOfFiles(numberOfFiles);
        this.setDirPath(dirPath);
    }

    public int getDirId() {
        return DirId;
    }

    public void setDirId(int dirId) {
        DirId = dirId;
    }

    public String getDirName() {
        return DirName;
    }

    public void setDirName(String dirName) {
        DirName = dirName;
    }

    public Float getDirSize() {
        return DirSize;
    }

    public void setDirSize(Float dirSize) {
        DirSize = dirSize;
    }

    public int getNumberOfFiles() {
        return NumberOfFiles;
    }

    public void setNumberOfFiles(int numberOfFiles) {
        NumberOfFiles = numberOfFiles;
    }

    public String getDirPath() {
        return DirPath;
    }

    public void setDirPath(String dirPath) {
        DirPath = dirPath;
    }
}
