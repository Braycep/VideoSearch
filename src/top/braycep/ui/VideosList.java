/*
 * Created by JFormDesigner on Mon Aug 20 23:00:05 CST 2018
 */

package top.braycep.ui;

import top.braycep.bean.VideoOrder;
import top.braycep.utils.Utils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.table.*;

/**
 * @author Braycep
 */
public class VideosList extends JFrame {
    private int focusedRowIndex;
    private JPopupMenu menu;

    public VideosList() {
        initComponents();
        initEvents();
    }

    private void initEvents() {
        table1.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    focusedRowIndex = table1.rowAtPoint(e.getPoint());
                    if (focusedRowIndex != -1) {
                        table1.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
                        createMouseMenu(table1);
                        menu.show(table1, e.getX(), e.getY());
                    }
                }
            }
        });

        table2.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    focusedRowIndex = table2.rowAtPoint(e.getPoint());
                    if (focusedRowIndex != -1) {
                        table2.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
                        createMouseMenu(table2);
                        menu.show(table2, e.getX(), e.getY());
                    }
                }
            }
        });
    }

    private void createMouseMenu(JTable table1) {
        menu = new JPopupMenu();
        JMenuItem open = new JMenuItem();
        JMenuItem copyMenu = new JMenuItem();
        JMenuItem openInBrowser = new JMenuItem();
        menu.add(initItem(table1, open, "打开"));
        menu.add(initItem(table1, copyMenu, "复制链接"));
        menu.add(initItem(table1, openInBrowser, "在浏览器中打开"));
    }

    private JMenuItem initItem(JTable table, JMenuItem item, String text) {
        item.setText(text);
        switch (text) {
            case "打开":
                item.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //download m3u8
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
            case "在浏览器中打开":
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
            tabbedPane1.addTab("\u6570\u636e\u6e90 - 1", scrollPane1);

            //======== scrollPane2 ========
            {

                //---- table2 ----
                table2.setModel(model2 = new DefaultTableModel(
                        new Object[][]{},
                        titles
                ));
                {
                    TableColumnModel cm = table2.getColumnModel();
                    cm.getColumn(0).setPreferredWidth(15);
                    cm.getColumn(1).setPreferredWidth(15);
                    cm.getColumn(2).setPreferredWidth(200);
                }
                scrollPane2.setViewportView(table2);
            }
            tabbedPane1.addTab("\u6570\u636e\u6e90 - 2", scrollPane2);
        }
        contentPane.add(tabbedPane1, BorderLayout.NORTH);
        pack();
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public void setTable1(Object[][] data){
        String url = (String)data[0][2];
        if (url.contains(".m3u8")){
            tabbedPane1.setTitleAt(0,"M3U8文件");
        }else{
            tabbedPane1.setTitleAt(0,"在线播放");
        }
        model1.setNumRows(0);
        model1.setDataVector(data, titles);
        table1.setModel(model1);
        table1.updateUI();
    }

    public void setTable2(Object[][] data){
        String url = (String)data[0][2];
        if (url.contains(".m3u8")){
            tabbedPane1.setTitleAt(1,"M3U8文件");
        }else{
            tabbedPane1.setTitleAt(1,"在线播放");
        }
        model2.setNumRows(0);
        model2.setDataVector(data, titles);
        table2.setModel(model2);
        table2.updateUI();
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
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
