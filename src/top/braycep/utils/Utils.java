package top.braycep.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.braycep.bean.Video;
import top.braycep.bean.VideoDetails;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * 工具类
 *
 * @author Braycep
 */
public class Utils {
    private static Video[] videos;

    /**
     * 根据关键词查找匹配的影视节目列表
     *
     * @param keywords 待搜索的关键词
     * @return 返回搜索结果数组
     * @throws IOException 抛出IO异常，可能由于网络原因导致连接失败
     */
    public static Video[] searchByKeyWords(String keywords) throws IOException {
        int index = 1;
        String encodeString = URLEncoder.encode(keywords, "UTF8");
        LogUtil.Log("正在搜索...");
        Document document = Jsoup.connect("http://api.iokzy.com//index.php?m=vod-search&wd=" + encodeString).get();
        Elements lis = document.getElementsByClass("xing_vb").get(0).getElementsByTag("li");
        LogUtil.Log("搜索已解析，共：" + (lis.size() - 2));

        //获取结果信息
        videos = new Video[lis.size() - 2];
        for (int i = 0; i < videos.length; i++) {
            videos[i] = new Video();
            Element a = lis.get(i + 1).getElementsByTag("a").get(0);
            videos[i].setId(index);
            videos[i].setUrl("http://api.iokzy.com/index.php" + a.attr("href"));
            videos[i].setName(a.text());
            videos[i].setType(lis.get(i + 1).getElementsByTag("span").get(2).text());
            videos[i].setDate(lis.get(i + 1).getElementsByTag("span").get(3).text());
            index++;
        }
        return videos;
    }


    /**
     * 获取对应影视的剧集
     *
     * @param url 影视链接，通过点击的行ID获取
     * @return 返回封装剧集链接的对象
     * @throws IOException 抛出IO异常，因为可能网页打不开
     */
    public static VideoDetails findVideoOders(String url) throws IOException {
        VideoDetails videoDetails = new VideoDetails();
        String[] urls;

        LogUtil.Log("连接: \t" + url);
        Document document = Jsoup.connect(url).get();

        //获取影视信息
        Element info = null;
        try {
            info = document.select("div.vodplayinfo > span").get(0);
        } catch (Exception e) {
            LogUtil.Log("未找到影视描述");
        }
        if (info != null) {
            videoDetails.setVideoInfo(info.text().substring(0, 40) + "...");
        } else {
            videoDetails.setVideoInfo("未找到影视描述");
        }

        //获取影视封面
        /*Element imgTag = document.select("img.lazy").get(0);
        String imgUrl = imgTag.attr("src");
        videoDetails.setLogo(imgUrl);*/

        //获取剧集，m3u8和其他播放源
        Elements eleUrls = document.getElementById("1").getElementsByTag("li");
        urls = new String[eleUrls.size()];
        for (int i = 0; i < eleUrls.size(); i++) {
            urls[i] = eleUrls.get(i).text();
        }
        if (urls.length > 0) {
            if (urls[0].contains("index.m3u8")) {
                videoDetails.setUrls2(urls);    //m3u8
            } else {
                videoDetails.setUrls1(urls);    //非m3u8
            }
        }

        eleUrls = document.getElementById("2").getElementsByTag("li");
        urls = new String[eleUrls.size()];
        for (int i = 0; i < eleUrls.size(); i++) {
            urls[i] = eleUrls.get(i).text();
        }
        if (urls.length > 0) {
            if (urls[0].contains("index.m3u8")) {
                videoDetails.setUrls2(urls);    //m3u8
            } else {
                videoDetails.setUrls1(urls);    //非m3u8
            }
        }
        return videoDetails;
    }

    /**
     * 将搜索结果转为表格数据形式
     *
     * @param videos 搜索结果数组
     * @return 表格数据
     */
    public static Object[][] produceTable(Video[] videos) {
        LogUtil.Log("生成表格数据");
        Object[][] objects = new Object[videos.length][4];
        for (int i = 0; i < videos.length; i++) {
            objects[i][0] = new Object();
            objects[i][1] = new Object();
            objects[i][2] = new Object();
            objects[i][3] = new Object();
            objects[i][0] = videos[i].getId();
            objects[i][1] = videos[i].getName();
            objects[i][2] = videos[i].getType();
            objects[i][3] = videos[i].getDate();
        }
        return objects;
    }

    /**
     * 生成表格数据
     *
     * @param index        第几页
     * @param videoDetails 视频详细信息
     * @return 返回表格数据
     */
    public static Object[][] produceTable(int index, VideoDetails videoDetails) {
        int len;
        String[] urls;
        if (index == 1) {
            len = videoDetails.getUrls1().length;
            urls = videoDetails.getUrls1();
        } else {
            len = videoDetails.getUrls2().length;
            urls = videoDetails.getUrls2();
        }
        return traverseUrls(index, len, urls);

    }

    /**
     * 拆解URL，并转为表格数据
     *
     * @param index 用于区分m3u8和在线播放 index = 2表示 m3u8
     * @param len   链接数量
     * @param urls  链接数组
     * @return 返回表格数据
     */
    private static Object[][] traverseUrls(int index, int len, String[] urls) {
        Object[][] objects = new Object[len][3];
        LogUtil.Log("转换链接，共:\t" + len);
        for (int i = 0; i < len; i++) {
            objects[i][0] = new Object();
            objects[i][1] = new Object();
            objects[i][2] = new Object();
            objects[i][0] = i;
            objects[i][1] = urls[i].split("\\$")[0];
            String url = urls[i].split("\\$")[1];
            //转换会导致播放器播放失败
           /* if (index == 2) {
                objects[i][2] = parseM3u8(url);
            } else {
                objects[i][2] = url;
            }*/
            objects[i][2] = url;
        }
        return objects;
    }

    /**
     * 通过鼠标点击的行的id获取该行的链接地址
     *
     * @param id 鼠标点击的行的id
     * @return 链接地址
     */
    public static String getUrlById(int id) {
        for (Video video : videos) {
            if (video.getId() == id) {
                return video.getUrl();
            }
        }
        return null;
    }

    /**
     * 转换m3u8地址
     *
     * @param orgM3u8Url 初始m3u8地址
     * @return 返回转换的m3u8链接
     */
    private static String parseM3u8(String orgM3u8Url) {
        return orgM3u8Url.replace("/index.m3u8", "/1000k/hls/index.m3u8");
    }

    /**
     * 通过鼠标点击的行的id获取该行的影片名
     *
     * @param id 鼠标点击的行的id
     * @return 影片名
     */
    public static String getNameById(Integer id) {
        for (Video video : videos) {
            if (video.getId() == id) {
                return video.getName();
            }
        }
        return null;
    }
}
