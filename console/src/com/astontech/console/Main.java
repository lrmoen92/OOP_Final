package com.astontech.console;

import com.astontech.bo.Directory;
import com.astontech.dao.DirectoryDAO;
import com.astontech.dao.FileDAO;
import com.astontech.dao.mysql.DirectoryDAOImpl;
import com.astontech.dao.mysql.FileDAOImpl;
import org.apache.log4j.Logger;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Logan.Moen on 6/13/2017.
 */
public class Main {

    final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            DBApp();
        } catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    private static void DBApp() throws Exception{
        //region APPLICATION
        Scanner reader = new Scanner(System.in);
        System.out.println("Please enter a file directory: ");
        String input = reader.nextLine();
        try {
            CollectFiles(new File(input));
        } catch (Exception ex) {
            throw new Exception("Inputted String Is Not A Valid File Directory!", ex);
        }
        Menu();
        //endregion
    }

    private static int FileCount(File dir){
        //region FILE COUNTER
        int count = 0;
        File[] files = dir.listFiles();
        for(File file : files){
            if(file.isFile()){
                count++;
            }
        }
        return count;
        //endregion
    }

    private static float FileSizeCount(File dir){
        //region DIRECTORY SIZE
        float size = 0;
        File[] files = dir.listFiles();
        for(File file : files){
            if(file.isDirectory()){
                size = FileSizeCount(file) + size;
            } else
                size = file.length() + size;
        }
        return size;
        //endregion
    }

    private static String PullFileExtension(File dir){
        //region PULL FILE EX
        String name = dir.getAbsoluteFile().toString();
        return name.substring(name.lastIndexOf("."));
        //endregion
    }

    private static void CollectFiles(File dir) {
        //region POPULATE DB
        DirectoryDAO directoryDAO = new DirectoryDAOImpl();
        FileDAO fileDAO = new FileDAOImpl();
        try {
            Directory directory = new Directory();
            //populate new directory object
            directory.setDirName(dir.getName());
            directory.setDirSize((FileSizeCount(new File(dir.getCanonicalPath())) / 1048576));
            directory.setNumberOfFiles(FileCount(new File(dir.getCanonicalPath())));
            directory.setDirPath(dir.getCanonicalPath());
            //insert directory object into DB, and get the dirId
            int currentDirId = directoryDAO.insertDirectory(directory);
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    CollectFiles(file);
                } else {
                    //populate new bo.file object
                    com.astontech.bo.File boFile = new com.astontech.bo.File();
                    boFile.setFileName(file.getName());
                    boFile.setFileType(PullFileExtension(file));
                    boFile.setFileSize(file.length());
                    boFile.setFilePath(file.getCanonicalPath());
                    boFile.setDirId(currentDirId);
                    //insert bo.file object into DB
                    fileDAO.insertFile(boFile);
                }
            }
        } catch (IOException ioEx) {
            logger.error(ioEx);
        }
        //endregion
    }

    private static void Menu(){
        //region Instantiate DAO
        DirectoryDAO directoryDAO = new DirectoryDAOImpl();
        FileDAO fileDAO = new FileDAOImpl();
        //endregion

        //region MENU
        System.out.println("========= MENU =========");
        System.out.println("Please Choose An Operation:");
        System.out.println("1) Display Directory With The Most Files");
        System.out.println("2) Display Directory With The Largest Size");
        System.out.println("3) Display The Five Largest Files");
        System.out.println("4) Display All Files Of Chosen Type");
        System.out.println("5) Clear Data And Choose New Directory");
        System.out.println("6) Exit");
        System.out.println("========================");
        Scanner reader = new Scanner(System.in);
        int menuSelect = reader.nextInt();
        //endregion

        if(menuSelect == 1){
            //region GET MOST FILES
            List<Directory> directoryList = directoryDAO.getDirectoryList();
                int maxFiles = 0;
                List<Integer> maxFilesIdList = new ArrayList<Integer>();
                for (Directory d : directoryList)
                    if(d.getNumberOfFiles() > maxFiles) {
                        maxFiles = d.getNumberOfFiles();
                    }
                for (Directory d : directoryList)
                    if(d.getNumberOfFiles() == maxFiles){
                        maxFilesIdList.add(d.getDirId());
                    }
                int s = maxFilesIdList.size();
                for (int i = 0; i < s; i++)
                System.out.println(directoryDAO.getDirectoryById(maxFilesIdList.get(i)).getDirName() + ": Has " + maxFiles + " Files.");
            //endregion
            Menu();

        }else if(menuSelect == 2){
            //region GET LARGEST DIR
            List<Directory> directoryList = directoryDAO.getDirectoryList();
            Float maxSize = 0f;
            List<Integer> maxSizeIdList = new ArrayList<Integer>();
            for (Directory d : directoryList)
                if(d.getDirSize() > maxSize) {
                    maxSize = d.getDirSize();
                }
            for (Directory d : directoryList)
                if(d.getDirSize().equals(maxSize)){
                    maxSizeIdList.add(d.getDirId());
                }
            int s = maxSizeIdList.size();
            for (int i = 0; i < s; i++)
                System.out.println(directoryDAO.getDirectoryById(maxSizeIdList.get(i)).getDirName() + ": Is " + maxSize + " MB Large.");
            //endregion
            Menu();

        }else if(menuSelect == 3){
            //region GET FIVE LARGEST FILES
            List<com.astontech.bo.File> fileList = fileDAO.getFileList();
            List<Long> maxSizes = new ArrayList<Long>();
            maxSizes.add(0l);maxSizes.add(0l);maxSizes.add(0l);maxSizes.add(0l);maxSizes.add(0l);
            List<Integer> maxSizeIdList = new ArrayList<Integer>();
            maxSizeIdList.add(0);maxSizeIdList.add(0);maxSizeIdList.add(0);maxSizeIdList.add(0);maxSizeIdList.add(0);

            for (com.astontech.bo.File f : fileList)
                      if(f.getFileSize() > maxSizes.get(0)){
                    maxSizes.add(1, maxSizes.get(0));
                    maxSizes.remove(0);
                    maxSizes.add(0, f.getFileSize());
                    maxSizeIdList.add(1, maxSizeIdList.get(0));
                    maxSizeIdList.remove(0);
                    maxSizeIdList.add(0, f.getFileId());
                }else if(f.getFileSize() > maxSizes.get(1)){
                    maxSizes.add(2, maxSizes.get(1));
                    maxSizes.remove(1);
                    maxSizes.add(1, f.getFileSize());
                    maxSizeIdList.add(2, maxSizeIdList.get(1));
                    maxSizeIdList.remove(1);
                    maxSizeIdList.add(1, f.getFileId());
                }else if(f.getFileSize() > maxSizes.get(2)){
                    maxSizes.add(3, maxSizes.get(2));
                    maxSizes.remove(2);
                    maxSizes.add(2, f.getFileSize());
                    maxSizeIdList.add(3, maxSizeIdList.get(2));
                    maxSizeIdList.remove(2);
                    maxSizeIdList.add(2, f.getFileId());
                }else if(f.getFileSize() > maxSizes.get(3)){
                    maxSizes.add(4, maxSizes.get(2));
                    maxSizes.remove(3);
                    maxSizes.add(3, f.getFileSize());
                    maxSizeIdList.add(4, maxSizeIdList.get(3));
                    maxSizeIdList.remove(3);
                    maxSizeIdList.add(3, f.getFileId());
                }else if(f.getFileSize() > maxSizes.get(4)){
                    maxSizes.remove(4);
                    maxSizes.add(4, f.getFileSize());
                    maxSizeIdList.remove(4);
                    maxSizeIdList.add(4, f.getFileId());
                }
            for (int i = 0; i < 5; i++)
                System.out.println(fileDAO.getFileById(maxSizeIdList.get(i)).getFileName() + ": Is " + maxSizes.get(i) + " Bytes Large.");
            //endregion
            Menu();

        }else if(menuSelect == 4){
            //region GET CHOSEN FILES BY TYPE
            List<com.astontech.bo.File> fileList = fileDAO.getFileList();
            List<Integer> fileTypeList = new ArrayList<Integer>();
            System.out.println("Enter a file type: (.xxx)");
            Scanner reader2 = new Scanner(System.in);
            String fileType = reader2.nextLine();

            for(com.astontech.bo.File f : fileList)
                if(f.getFileType().equals(fileType))
                    fileTypeList.add(f.getFileId());
            int l = fileTypeList.size();
            if (l == 0)
                 System.out.println("No " + fileType + " Files Found!");
            else
                for (int i = 0; i < l; i++)
                    System.out.println(fileDAO.getFileById(fileTypeList.get(i)).getFileName());
            System.out.println();
            //endregion
            Menu();

        }else if(menuSelect == 5){
            //region CLEAR DATA
            List<com.astontech.bo.File> fileList = fileDAO.getFileList();
            List<Directory> directoryList = directoryDAO.getDirectoryList();
            List<Integer> fileIdList = new ArrayList<Integer>();
            List<Integer> directoryIdList = new ArrayList<Integer>();
            for(com.astontech.bo.File f : fileList)
                fileIdList.add(f.getFileId());
            for(int i = 0; i < fileIdList.size(); i++)
                fileDAO.deleteFile(fileIdList.get(i));
            for(Directory d : directoryList)
                directoryIdList.add(d.getDirId());
            for(int i = 0; i < directoryIdList.size(); i++)
                directoryDAO.deleteDirectory(directoryIdList.get(i));
            //endregion
            try {
                DBApp();
            } catch(Exception ex){
                System.out.println(ex.toString());
            }

        }else if(menuSelect == 6){

            //region CLEAR DATA
            List<com.astontech.bo.File> fileList = fileDAO.getFileList();
            List<Directory> directoryList = directoryDAO.getDirectoryList();
            List<Integer> fileIdList = new ArrayList<Integer>();
            List<Integer> directoryIdList = new ArrayList<Integer>();
            for(com.astontech.bo.File f : fileList)
                fileIdList.add(f.getFileId());
            for(int i = 0; i < fileIdList.size(); i++)
                fileDAO.deleteFile(fileIdList.get(i));
            for(Directory d : directoryList)
                directoryIdList.add(d.getDirId());
            for(int i = 0; i < directoryIdList.size(); i++)
                directoryDAO.deleteDirectory(directoryIdList.get(i));
            //endregion

            System.out.println("Closing Application...");

        }else {
            System.out.println("Invalid Selection, Please Choose A Number 1-6");
            Menu();
        }
    }
}
