package com.tz.test;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
* 寻找指定路径下jar包中含特定字符串的文件   */

public class FindStrInJar {

    public String condition;  //查询的条件

     public ArrayList<String> jarFiles = new ArrayList<String>();

     public FindStrInJar(String condition) {
         this.condition = condition;

      }

     public List<String> find(String dir, boolean recurse) {
         searchDir(dir, recurse);
         return this.jarFiles;
     }

     public List<String> getFilenames() {
          return this.jarFiles;
     }

     protected String getClassName(ZipEntry entry) {
         StringBuffer className = new StringBuffer(entry.getName().replace("/", "."));
         return className.toString();
     }

      @SuppressWarnings("unchecked")
     protected void searchDir(String dir, boolean recurse) {
         try {
              File d = new File(dir);
             if (!d.isDirectory()) {
                 return;
             }
             File[] files = d.listFiles();
             for (int i = 0; i < files.length; i++) {
                 if (recurse && files[i].isDirectory()) {
                     searchDir(files[i].getAbsolutePath(), true);
                 } else {
                     String filename = files[i].getAbsolutePath();
                     if (filename.endsWith(".jar")||filename.endsWith(".zip")) {
                         ZipFile zip = new ZipFile(filename);
                         Enumeration entries = zip.entries();
                         while (entries.hasMoreElements()) {
                             ZipEntry entry = (ZipEntry) entries.nextElement();
                             String thisClassName = getClassName(entry);

                             //不搜索扩展名为.class的文件
 //                            if(thisClassName.lastIndexOf(".class")==-1){
                                 BufferedReader r = new BufferedReader(new InputStreamReader(zip.getInputStream(entry)));
                                 while(r.read()!=-1){
                                     String tempStr = r.readLine();
                                     if(null!=tempStr && tempStr.toLowerCase().contains(condition.toLowerCase())){    //不区分大小写
//                                     if(null!=tempStr && tempStr.contains(condition)){  //区分大小写
                                         this.jarFiles.add(filename + "  --->  " + thisClassName);
                                         break;
                                     }
                                 }
 //                            }

                         }
                     }
                 }
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

    private boolean containsIgnoreCase(String tempStr, String condition) {
         return true;
    }

    public static void main(String args[]) {
         //输入想要搜索的字符串
         String search_keywords = "VPN";

         //设置搜索jar的路径
         String dt_Dir = "D:\\Android\\nixiang\\dt2-jar";
         String tu_Dir = "D:\\Android\\nixiang\\tu-jar";

         int i = 0;
         FindStrInJar findInJar = new FindStrInJar(search_keywords);
         System.out.println("Searched Keywords ===> " + search_keywords + "\nResult：");
         List<String> jarFiles = findInJar.find(dt_Dir, true);
         if (jarFiles.size() == 0) {
             System.out.println("========= Not Found!!! ========= \n");
         } else {
             for (i = 0; i < jarFiles.size(); i++) {
                 System.out.println(jarFiles.get(i));

             }
             System.out.println(i+1);
         }
         System.out.println("Finish!");
     }

 }