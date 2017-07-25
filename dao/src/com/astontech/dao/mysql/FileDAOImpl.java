package com.astontech.dao.mysql;

import com.astontech.bo.File;
import com.astontech.dao.FileDAO;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Logan.Moen on 6/13/2017.
 */
public class FileDAOImpl extends MySQL implements FileDAO {
    @Override
    public File getFileById(int fileId) {
        File file = null;
        Connect();

        try{
            String sp = "{Call GetFile(?,?)}";
            CallableStatement cStmt = connection.prepareCall(sp);
            cStmt.setInt(1, GET_BY_ID);
            cStmt.setInt(2, fileId);
            ResultSet rs = cStmt.executeQuery();

            if(rs.next()){
                file = HydrateObject(rs);
            }

        } catch (SQLException sqlEx){
            logger.error(sqlEx);
        }

        return file;
    }

    @Override
    public List<File> getFileList() {
        List<File> fileList = new ArrayList<File>();
        Connect();

        try{
            String sp = "{Call GetFile(?,?)}";
            CallableStatement cStmt = connection.prepareCall(sp);
            cStmt.setInt(1, GET_COLLECTION);
            cStmt.setInt(2, 0);
            ResultSet rs = cStmt.executeQuery();

            while(rs.next()){
                fileList.add(HydrateObject(rs));
            }

        } catch (SQLException sqlEx){
            logger.error(sqlEx);
        }

        return fileList;
    }

    @Override
    public int insertFile(File file) {
        int id = 0;
        Connect();

        try{
            String sp = "{Call ExecFile(?,?,?,?,?,?,?)}";
            CallableStatement cStmt = connection.prepareCall(sp);
            cStmt.setInt(1, INSERT);
            cStmt.setInt(2, 0);
            cStmt.setString(3, file.getFileName());
            cStmt.setString(4, file.getFileType());
            cStmt.setLong(5, file.getFileSize());
            cStmt.setString(6, file.getFilePath());
            cStmt.setInt(7, file.getDirId());
            ResultSet rs = cStmt.executeQuery();

            if(rs.next()){
                id = rs.getInt(1);
            }

        }catch(SQLException sqlEx){
            logger.error(sqlEx);
        }

        return id;
    }

    @Override
    public boolean updateFile(File file) {
        int id = 0;
        Connect();

        try{
            String sp = "{Call ExecFile(?,?,?,?,?,?,?)}";
            CallableStatement cStmt = connection.prepareCall(sp);
            cStmt.setInt(1, UPDATE);
            cStmt.setInt(2, file.getFileId());
            cStmt.setString(3, file.getFileName());
            cStmt.setString(4, file.getFileType());
            cStmt.setLong(5, file.getFileSize());
            cStmt.setString(6, file.getFilePath());
            cStmt.setInt(7, file.getDirId());
            ResultSet rs = cStmt.executeQuery();

            if(rs.next()){
                id = rs.getInt(1);
            }

        }catch(SQLException sqlEx){
            logger.error(sqlEx);
        }

        return id > 0;
    }

    @Override
    public boolean deleteFile(int fileId) {
        int id = 0;
        Connect();

        try{
            String sp = "{Call ExecFile(?,?,?,?,?,?,?)}";
            CallableStatement cStmt = connection.prepareCall(sp);
            cStmt.setInt(1, DELETE);
            cStmt.setInt(2, fileId);
            cStmt.setString(3, null);
            cStmt.setString(4, null);
            cStmt.setLong(5, 0);
            cStmt.setString(6, null);
            cStmt.setInt(7, 0);
            ResultSet rs = cStmt.executeQuery();

            if(rs.next()){
                id = rs.getInt(1);
            }

        }catch(SQLException sqlEx){
            logger.error(sqlEx);
        }

        return id > 0;
    }

    private static File HydrateObject(ResultSet rs)throws SQLException{
        File file = new File();

        file.setFileId(rs.getInt(1));
        file.setFileName(rs.getString(2));
        file.setFileType(rs.getString(3));
        file.setFileSize(rs.getLong(4));
        file.setFilePath(rs.getString(5));
        file.setDirId(rs.getInt(6));

        return file;
    }
}
