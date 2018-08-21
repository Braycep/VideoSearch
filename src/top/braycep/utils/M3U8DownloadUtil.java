package top.braycep.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class M3U8DownloadUtil {

    /**
     * 下载m3u8文件
     *
     * @param m3u8Url m3u8链接
     * @return 返回下载的M3U8文件内容
     */
    public static String getM3U8Contents(String m3u8Url) throws IOException {
        URL url = new URL(m3u8Url);
        URLConnection connection = url.openConnection();
        InputStream is = connection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String line;
        String fileContent = "";
        while ((line = br.readLine()) != null) {
            fileContent += line + "\r\n";
        }
        br.close();
        return fileContent;
    }

    /**
     * 将m3u8文件内的播放列表提取出
     *
     * @param fileContent m3u8文件内容
     * @return 播放列表
     */
    public static ArrayList<String> parseM3U8ToList(String fileContent) {
        ArrayList<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile("[0-9a-z]{1,}.ts");
        Matcher matcher = pattern.matcher(fileContent);
        while (matcher.find()) {
            String group = matcher.group();
            list.add(group);
        }
        return list;
    }

    /**
     * 下载m3u8文件中的每个TS文件
     *
     * @param list TS文件列表
     * @return 下载结果
     */
    public static boolean downloadPieceInM3U8(ArrayList<String> list) {
        //download
        return false;
    }
}