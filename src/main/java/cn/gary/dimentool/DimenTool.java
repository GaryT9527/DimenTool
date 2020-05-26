package cn.gary.dimentool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DimenTool {
    private static final String[] DIR_LIST = new String[]{
            "mdpi-1920x1080", "hdpi-1920x1080", "xhdpi-1920x1080",
            "mdpi-1280x720", "hdpi-1280x720", "xhdpi-1280x720",
            "mdpi-1280x800", "hdpi-1280x800", "xhdpi-1280x800"
    };
    private static final String ROOT = "./app/src/main/res";

    private static void gen() {
        createDir();
        File file = new File(ROOT + "/values/dimens.xml");
        File a = new File(ROOT + "/values");
        if(!a.exists()) a.mkdir();
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*
         *   1920*1080   1.0   m     ==values *1.5              ==values *1.5
         *   1920*1080   1.5   h     ==values                   ==values
         *   1920*1080   2.0   xh    ==values *1.5 /2           ==values *0.75
         *
         *   1280*720    1.0   m     == values *1.5/1.5         == values
         *   1280*720    1.5   h     == values *1.5/1.5/1.5     == values *2/3
         *   1280*720    2.0   xh    == values *1.5/1.5/2       == values /2
         *
         */
        BufferedReader reader = null;
        StringBuilder m1920 = new StringBuilder();
        StringBuilder h1920 = new StringBuilder();
        StringBuilder xh1920 = new StringBuilder();

        StringBuilder m1280 = new StringBuilder();
        StringBuilder h1280 = new StringBuilder();
        StringBuilder xh1280 = new StringBuilder();

        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                if (tempString.contains("</dimen>")) {
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    Double num = Double.parseDouble(tempString.substring(tempString.indexOf(">") + 1, tempString.indexOf("</dimen>") - 2));
                    m1920.append(start).append((double) Math.round(num * 1.5 * 100) / 100).append(end).append("\r\n");
                    h1920.append(start).append((double) Math.round(num * 100) / 100).append(end).append("\r\n");
                    xh1920.append(start).append((double) Math.round(num * 0.75 * 100) / 100).append(end).append("\r\n");

                    m1280.append(start).append((double) Math.round(num * 100) / 100).append(end).append("\r\n");
                    h1280.append(start).append((double) Math.round(num * 2 / 3 * 100) / 100).append(end).append("\r\n");
                    xh1280.append(start).append((double) Math.round(num / 2 * 100) / 100).append(end).append("\r\n");

                } else {
                    m1920.append(tempString).append("\r\n");
                    h1920.append(tempString).append("\r\n");
                    xh1920.append(tempString).append("\r\n");
                    m1280.append(tempString).append("\r\n");
                    h1280.append(tempString).append("\r\n");
                    xh1280.append(tempString).append("\r\n");
                }
            }
            reader.close();
            String m1920file = ROOT + "/values-mdpi-1920x1080/dimens.xml";
            String h1920file = ROOT + "/values-hdpi-1920x1080/dimens.xml";
            String xh1920file = ROOT + "/values-xhdpi-1920x1080/dimens.xml";
            String m1280file = ROOT + "/values-mdpi-1280x720/dimens.xml";
            String h1280file = ROOT + "/values-hdpi-1280x720/dimens.xml";
            String xh1280file = ROOT + "/values-xhdpi-1280x720/dimens.xml";
            String m1280file2 = ROOT + "/values-mdpi-1280x800/dimens.xml";
            String h1280file2 = ROOT + "/values-hdpi-1280x800/dimens.xml";
            String xh1280file2 = ROOT + "/values-xhdpi-1280x800/dimens.xml";
            writeFile(m1920file, m1920.toString());
            writeFile(h1920file, h1920.toString());
            writeFile(xh1920file, xh1920.toString());
            writeFile(m1280file, m1280.toString());
            writeFile(h1280file, h1280.toString());
            writeFile(xh1280file, xh1280.toString());
            writeFile(m1280file2, m1280.toString());
            writeFile(h1280file2, h1280.toString());
            writeFile(xh1280file2, xh1280.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private static void createDir() {
        String[] dirs = ROOT.split("/");
        String tmp = ".";
        for (String dir : dirs) {
            if (dir.equals(".")) {
                continue;
            } else {
                File file = new File(tmp + "/" + dir);
                if (!file.exists()) {
                    file.mkdir();
                    tmp = tmp + "/" + dir;
                }
            }
        }
        for (String flag : DIR_LIST) {
            String path = ROOT + "/values-" + flag;
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
        }
    }

    /**
     * 写入方法
     */
    public static void writeFile(String file, String text) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
    }

    public static void main(String[] args) {
        gen();
    }
}
