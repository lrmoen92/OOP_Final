package com.astontech.dao;

import com.astontech.bo.File;

import java.util.List;

/**
 * Created by Logan.Moen on 6/13/2017.
 */
public interface FileDAO {
    public File getFileById(int fileId);
    public List<File> getFileList();

    public int insertFile(File file);
    public boolean updateFile(File file);
    public boolean deleteFile(int fileId);
}
