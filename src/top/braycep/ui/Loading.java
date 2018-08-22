package top.braycep.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Loading extends JFrame {

    private int index = 1;
    private boolean load = true;

    public Loading() {
        setSize(200, 15);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setOpacity(0.9F);
        setAlwaysOnTop(true);
        setVisible(true);
    }

    private void playLoadingImage(Graphics g) {
        File file;
        while (load) {
            file = new File(System.getProperty("user.dir") + "/image/loading (" + index + ").JPG");
            index++;
            try {
                g.drawImage(ImageIO.read(file), 0, 0, 200, 15, null);
                Thread.sleep(17);
                if (index == 31) {
                    index = 1;
                }
                repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        playLoadingImage(g);
    }

    public boolean isLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }

    public static void main(String[] args) {
        Loading loading = new Loading();
        try {
            Thread.sleep(2000);
            System.out.println("dispose");
            loading.load = false;
            loading.dispose();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
