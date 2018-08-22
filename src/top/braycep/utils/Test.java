package top.braycep.utils;

/**
 * 测试类
 *
 * @author Braycep
 */
public class Test {
    public static void main(String[] args) throws Exception {
        String url = "http://api.iokzy.com/?m=vod-detail-id-12728.html";
        Utils.findVideoOders(url);
    }
}
