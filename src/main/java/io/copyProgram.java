package io;

import java.io.*;

public class copyProgram {
    private static String path = "/Users/lihang/allDemo/src/main/java";
    public static void main(String[] args){
        try {
//            fileCopyByReader();
            fileCopyByStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fileCopyByReader() throws IOException{
        FileReader reader = null;
        FileWriter writer = null;
        try {
            reader = new FileReader("./a.txt");
            writer = new FileWriter("./aa.txt");

            int a;
            while ((a = reader.read()) != -1) {
                writer.write(a);
            }
        } finally {
            reader.close();
            writer.close();
        }
    }

    public static void fileCopyByStream() throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(path+"/a.txt");
            out = new FileOutputStream(path+"/aa.txt");

            int a;
            while ((a = in.read()) != -1) {
                out.write(a);
            }
        } finally {
            in.close();
            out.close();
        }
    }
}