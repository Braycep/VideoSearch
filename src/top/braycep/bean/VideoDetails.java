package top.braycep.bean;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

/**
 * 剧集对象
 */
public class VideoDetails {
    private String[] urls1;
    private String[] urls2;
    private String videoInfo;
    private BufferedImage logo;

    public String[] getUrls1() {
        return urls1;
    }

    public void setUrls1(String[] urls1) {
        this.urls1 = urls1;
    }

    public String[] getUrls2() {
        return urls2;
    }

    public void setUrls2(String[] urls2) {
        this.urls2 = urls2;
    }

    public String getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(String videoInfo) {
        this.videoInfo = videoInfo;
    }

    public BufferedImage getLogo() {
        return logo;
    }

    public void setLogo(String url) throws IOException {
        this.logo = ImageIO.read(new URL(url));
    }

    @Override
    public String toString() {
        return "VideoDetails{" +
                "urls1=" + Arrays.toString(urls1) +
                ", urls2=" + Arrays.toString(urls2) +
                ", videoInfo='" + videoInfo + '\'' +
                ", logo=" + logo +
                '}';
    }
}
