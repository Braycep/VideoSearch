package top.braycep.utils;

public class Test {
    public static void main(String[] args) {
        String str = "第01集$http://youku.cdn-iqiyi.com/20180423/9613_415b75f5/index.m3u8";
        String s1 = str.split("\\$")[0];
        System.out.println(s1);
    }
}
