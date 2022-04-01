package com.play.myplaypc.playphone;

/**
 * Created by MY PlayPC on 27 Jan,2020 027.
 */


/**
 * Write a description of class test here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Connection
{
    public static boolean isReachableByPing() {
        try{
            String cmd = "";String host="www.google.com";
            if(System.getProperty("os.name").startsWith("Windows")) {
                // For Windows
                cmd = "ping -n 1 " + host;
            } else {
                // For Linux and OSX
                cmd = "ping -c 1 " + host;
            }

            Process myProcess = Runtime.getRuntime().exec(cmd);
            myProcess.waitFor();

            if(myProcess.exitValue() == 0) {

                return true;
            } else {

                return false;
            }

        } catch( Exception e ) {

            e.printStackTrace();
            return false;
        }
    }
}
