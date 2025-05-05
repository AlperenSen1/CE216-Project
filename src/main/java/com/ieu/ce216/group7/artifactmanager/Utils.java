package com.ieu.ce216.group7.artifactmanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.regex.Pattern;

public class Utils {

    public static Properties properties;
    public static File dbFile;

    public static String getPropsFile(){
        String proppath=System.getProperty("user.home")+"/.ArtifactManager/";
        proppath=proppath.replaceAll(Pattern.quote("\\"),"/");
        System.out.println(proppath);
        if(!new File(proppath).exists()){
            new File(proppath).mkdirs();
        }
        File pf=new File(proppath+"application.properties");
        try {
            pf.createNewFile();
            System.out.println(pf.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Path propFile = Paths.get(ArtifactManagerApplication.class.getResource("/").getFile()+"/application.properties");

        return proppath+"application.properties";
    }

    public static boolean checkContextPath(){
        boolean result = true;
        properties = new Properties();
        //Path propFile = Paths.get(ArtifactManagerApplication.class.getResource("application.properties").getPath().substring(1));

        Path propFile = Paths.get(Utils.getPropsFile());
        try {
            properties.load(Files.newBufferedReader(propFile));
        } catch (IOException e) {
            properties=null;
        }
        if(properties!=null && properties.getProperty("context.path")!=null) {
            //indexLbl.setText(properties.getProperty("context.path"));
            dbFile=new File(properties.getProperty("context.path")+"/ArtifactManagerDB.json");
        }else{

            result = false;
        }
        return result;
    }
}
