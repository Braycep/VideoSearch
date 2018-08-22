/*
 * Created by JFormDesigner on Mon Aug 20 23:00:05 CST 2018
 */

package top.braycep.ui;

import top.braycep.bean.VideoDetails;
import top.braycep.utils.M3U8DownloadUtil;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.Identity;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;

/**
 * @author Braycep
 */
public class VideosList extends JFrame {
    private int focusedRowIndex;
    private JPopupMenu menu;
    private VideoDetails videoDetails;

    public VideosList(VideoDetails videoDetails, String title) {
        mainFrame = this;
        this.setTitle(title);
        this.videoDetails = videoDetails;
        initComponents();
        initEvents();
    }

    private void initEvents() {
        table1.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                createMouseMenu(e, table1);
            }
        });

        table2.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                createMouseMenu(e, table2);
            }
        });
    }

    private void createMouseMenu(MouseEvent e, JTable table) {
        focusedRowIndex = table.rowAtPoint(e.getPoint());
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (focusedRowIndex != -1) {
                table.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
                createMouseMenu(table);
                menu.show(table, e.getX(), e.getY());
            }
        }
    }

    private void createMouseMenu(JTable table) {
        menu = new JPopupMenu();
        if (table == table1) {
            Logger.getGlobal().info("第一页创建右键菜单");
            JMenuItem play = new JMenuItem();
            menu.add(initItem(table, play, "在线播放"));
        } else {
            Logger.getGlobal().info("第二页创建右键菜单");
            JMenuItem download = new JMenuItem();
            JMenuItem downloadVideo = new JMenuItem();
            menu.add(initItem(table, download, "下载 M3U8 文件"));
            menu.add(initItem(table, downloadVideo, "下载视频"));
        }
        JMenuItem copyMenu = new JMenuItem();
        menu.add(initItem(table, copyMenu, "复制链接"));
    }

    private JMenuItem initItem(JTable table, JMenuItem item, String text) {
        item.setText(text);
        switch (text) {
            case "在线播放":
                item.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String url = (String) table.getValueAt(focusedRowIndex, 2);
                        if (url != null) {
                            try {
                                Desktop.getDesktop().browse(new URI(url));
                            } catch (IOException | URISyntaxException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
                break;
            case "复制链接":
                item.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String url = (String) table.getValueAt(focusedRowIndex, 2);
                        if (url != null) {
                            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                            Transferable tText = new StringSelection(url);
                            clip.setContents(tText, null);
                            JOptionPane.showMessageDialog(null, "链接已复制", "提示", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                });
                break;
            case "下载 M3U8 文件":
                item.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String url = (String) table.getValueAt(focusedRowIndex, 2);
                        JFileChooser fc = new JFileChooser();
                        FileNameExtensionFilter filter = new FileNameExtensionFilter("流媒体文件(*.m3u8)", "m3u8");
                        fc.setFileFilter(filter);
                        int action = fc.showSaveDialog(mainFrame);
                        if (action == JFileChooser.APPROVE_OPTION) {
                            Logger.getGlobal().info("保存m3u8文件");
                            File file = fc.getSelectedFile();
                            if (!file.getName().endsWith(".m3u8")) {
                                file = new File(fc.getCurrentDirectory(), file.getName() + ".m3u8");
                            }
                            int result = -1;
                            if (file.exists()) {
                                result = JOptionPane.showConfirmDialog(mainFrame, "文件已存在，是否覆盖？", "警告", JOptionPane.OK_CANCEL_OPTION);
                            }
                            try {
                                if (result == 0) {
                                    boolean b = file.createNewFile();
                                    Logger.getGlobal().info("文件船创建" + (b ? "失败" : "成功"));
                                }
                                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
                                bw.write(M3U8DownloadUtil.getM3U8Contents(url));
                                bw.flush();
                                bw.close();
                                JOptionPane.showMessageDialog(mainFrame, "文件以保存", "提示", JOptionPane.INFORMATION_MESSAGE);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            Logger.getGlobal().info("文件已保存: " + file.getAbsolutePath());
                        }
                    }
                });
                break;
            case "下载视频":
                item.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String url = (String) table.getValueAt(focusedRowIndex, 2);

                    }
                });
                break;
            default:
                break;
        }
        return item;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        tabbedPane1 = new JTabbedPane();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== tabbedPane1 ========
        {

            //======== scrollPane1 ========
            {

                //---- table1 ----
                table1.setModel(model1 = new DefaultTableModel(
                        new Object[][]{},
                        titles
                ));
                table1.setToolTipText(videoDetails.getVideoInfo());
                {
                    TableColumnModel cm = table1.getColumnModel();
                    cm.getColumn(0).setMinWidth(15);
                    cm.getColumn(0).setPreferredWidth(15);
                    cm.getColumn(1).setMinWidth(15);
                    cm.getColumn(1).setPreferredWidth(15);
                    cm.getColumn(2).setPreferredWidth(200);
                }
                scrollPane1.setViewportView(table1);
            }
            tabbedPane1.addTab("在线播放", scrollPane1);

            //======== scrollPane2 ========
            {

                //---- table2 ----
                table2.setModel(model2 = new DefaultTableModel(
                        new Object[][]{},
                        titles
                ));
                table2.setToolTipText(videoDetails.getVideoInfo());
                {
                    TableColumnModel cm = table2.getColumnModel();
                    cm.getColumn(0).setPreferredWidth(15);
                    cm.getColumn(1).setPreferredWidth(15);
                    cm.getColumn(2).setPreferredWidth(200);
                }
                scrollPane2.setViewportView(table2);
            }
            tabbedPane1.addTab("M3U8", scrollPane2);
        }
        contentPane.add(tabbedPane1, BorderLayout.NORTH);
        pack();
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    void setTable1(Object[][] data) {
        model1.setNumRows(0);
        model1.setDataVector(data, titles);
        table1.setModel(model1);
        table1.updateUI();
    }

    void setTable2(Object[][] data) {
        model2.setNumRows(0);
        model2.setDataVector(data, titles);
        table2.setModel(model2);
        table2.updateUI();
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JFrame mainFrame;
    private JTabbedPane tabbedPane1;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JScrollPane scrollPane2;
    private JTable table2;
    private DefaultTableModel model1;
    private DefaultTableModel model2;
    private String[] titles = new String[]{"\u5e8f\u53f7", "\u5267\u96c6", "\u94fe\u63a5"};
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
