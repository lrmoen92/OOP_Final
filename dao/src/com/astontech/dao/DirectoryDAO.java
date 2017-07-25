package com.astontech.dao;

import com.astontech.bo.Directory;

import java.util.List;

/**
 * Created by Logan.Moen on 6/13/2017.
 */
public interface DirectoryDAO {
    public Directory getDirectoryById(int dirId);
    public List<Directory> getDirectoryList();

    public int insertDirectory(Directory directory);
    public boolean updateDirectory(Directory directory);
    public boolean deleteDirectory(int dirId);
}
