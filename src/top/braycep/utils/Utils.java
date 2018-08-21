package top.braycep.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.braycep.bean.Video;
import top.braycep.bean.VideoOrder;

import java.io.IOException;
import java.net.URLEncoder;

public class Utils {
    private static Video[] videos;
    //interface 1 http://api.iokzy.com
    //http://api.iokzy.com/index.php?m=vod-search&wd=keywords

    /**
     * 根据关键词查找匹配的影视节目列表
     *
     * @param keywords 待搜索的关键词
     * @return 返回影视对象数组
     * @throws IOException 抛出IO异常，因为可能打不开该网页
     */
    public static Video[] searchByKeyWords(String keywords) throws IOException {
        int index = 1;
        String encodeString = URLEncoder.encode(keywords);
        Document document = Jsoup.connect("http://api.iokzy.com//index.php?m=vod-search&wd=" + encodeString).get();
        Elements lis = document.getElementsByClass("xing_vb").get(0).getElementsByTag("li");
        int len = lis.size();

        //获取视频信息
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
     * @param url 影视链接，从searchByKeyWords中获取
     * @return 返回封装剧集链接的对象
     * @throws IOException 抛出IO异常，因为可能网页打不开
     */
    public static VideoOrder findVideoOders(String url) throws IOException {
        VideoOrder videoOrder = new VideoOrder();
        String[] urls;

        Document document = Jsoup.connect(url).get();

        //获取影视信息
        Elements elements = document.select("div.vodplayinfo > span");

        //获取影视封面
        Element imgTag = document.select("img.lazy").get(0);
        String imgUrl = imgTag.attr("src");
        videoOrder.setLogo(imgUrl);

        //获取剧集，m3u8和其他播放源
        Elements eleUrls = document.getElementById("1").getElementsByTag("li");
        urls = new String[eleUrls.size()];
        for (int i = 0; i < eleUrls.size(); i++) {
            urls[i] = eleUrls.get(i).text();
        }
        videoOrder.setUrls1(urls);

        eleUrls = document.getElementById("2").getElementsByTag("li");
        urls = new String[eleUrls.size()];
        for (int i = 0; i < eleUrls.size(); i++) {
            urls[i] = eleUrls.get(i).text();
        }
        videoOrder.setUrls2(urls);

        return videoOrder;
    }

    public static Object[][] produceTable(Video[] videos) {
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

    public static Object[][] produceTable1(VideoOrder videos) {
        int len = 0;
        if (videos != null && videos.getUrls1() != null){
            len = videos.getUrls1().length;
        }
        Object[][] objects = new Object[len][3];
        String[] urls = videos.getUrls1();
        traverseUrls(len, objects, urls);
        return objects;
    }

    public static Object[][] produceTable2(VideoOrder videos) {
        int len = 0;
        if (videos != null && videos.getUrls2() != null){
            len = videos.getUrls2().length;
        }
        Object[][] objects = new Object[len][3];
        String[] urls = videos.getUrls2();
        traverseUrls(len, objects, urls);
        return objects;
    }

    private static void traverseUrls(int len, Object[][] objects, String[] urls) {
        for (int i = 0; i < len; i++) {
            objects[i][0] = new Object();
            objects[i][1] = new Object();
            objects[i][2] = new Object();
            objects[i][0] = i;
            objects[i][1] = urls[i].split("\\$")[0];
            objects[i][2] = urls[i].split("\\$")[1];

        }
    }

    public static String getUrlById(int id){
        for (Video video : videos) {
            if (video.getId() == id){
                return video.getUrl();
            }
        }
        return null;
    }
}
