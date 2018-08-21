package top.braycep.ui;

import top.braycep.bean.Video;
import top.braycep.bean.VideoOrder;
import top.braycep.utils.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class MainFrame extends JFrame {
    private JPanel contentPane;
    private JTextField textField;
    private JTable table;

    private Object[][] values = {};
    private String[] titles = new String[]{"序号", "\u5F71\u7247\u540D\u79F0", "\u5F71\u7247\u7C7B\u578B",
            "\u66F4\u65B0\u65E5\u671F"};
    private DefaultTableModel model;

    private final JButton btn_about;
    private final JButton btn_stop;
    private final JButton btn_search;
    private int focusedRowIndex = -1;
    private JPopupMenu menu;
    private boolean searching;
    private Video[] videos;


    {
        model = new DefaultTableModel(values, titles) {
            boolean[] columnEditables = new boolean[]{false, false, false};

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        };
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(javax.swing.plaf.nimbus.NimbusLookAndFeel.class.getName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public MainFrame() {
        setTitle("影视搜搜");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 460);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.white);
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel label = new JLabel("影视名称：");
        label.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
        label.setBounds(20, 13, 70, 25);
        contentPane.add(label);

        textField = new JTextField();
        textField.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setBounds(88, 13, 280, 25);
        contentPane.add(textField);
        textField.setColumns(10);

        btn_search = new JButton("搜索");
        btn_search.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
        btn_search.setBounds(380, 13, 70, 25);
        contentPane.add(btn_search);

        btn_stop = new JButton("停止");
        btn_stop.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
        btn_stop.setBounds(460, 13, 70, 25);
        contentPane.add(btn_stop);

        btn_about = new JButton("关于");
        btn_about.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
        btn_about.setBounds(540, 13, 70, 25);
        contentPane.add(btn_about);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 55, 625, 370);
        contentPane.add(scrollPane);

        table = new JTable();
        table.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
        table.setModel(model);
        table.getColumnModel().getColumn(0).setResizable(false);
        table.getColumnModel().getColumn(1).setResizable(false);
        table.getColumnModel().getColumn(2).setResizable(false);
        scrollPane.setViewportView(table);

        initEvents();
    }

    private void initEvents() {
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    start();
                }
            }
        });

        btn_search.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                start();
            }
        });

        btn_about.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JOptionPane.showMessageDialog(null, "Design By Braycep\nBuild By Braycep\nMedia Source From The Internet", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    focusedRowIndex = table.rowAtPoint(e.getPoint());
                    if (focusedRowIndex != -1) {
                        table.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
                        createMouseMenu();
                        menu.show(table, e.getX(), e.getY());
                    }
                }
            }
        });
    }

    private void start() {
        searching = true;
        new Thread() {
            public void run() {
                String keywords = textField.getText().trim();
                if (keywords.equals("")) {
                    JOptionPane.showMessageDialog(null, "关键词为空，忽略搜索！", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        model.setNumRows(0);
                        videos = Utils.searchByKeyWords(keywords);
                        Object[][] objects = Utils.produceTable(videos);
                        searching = false;
                        model.setDataVector(objects, titles);
                        table.setModel(model);
                        table.updateUI();
                    } catch (IOException e1) {
                        searching = false;
                        JOptionPane.showMessageDialog(null, "网页打开失败，请检查网络连接", "警告", JOptionPane.WARNING_MESSAGE);
                        e1.printStackTrace();
                    }
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                btn_search.setEnabled(false);
                while (searching) {
                    try {
                        btn_search.setText("--");
                        Thread.sleep(100);
                        btn_search.setText("\\");
                        Thread.sleep(100);
                        btn_search.setText("|");
                        Thread.sleep(100);
                        btn_search.setText("/");
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                btn_search.setEnabled(true);
                btn_search.setText("搜索");
            }
        }.start();
    }

    private void createMouseMenu() {
        menu = new JPopupMenu();
        JMenuItem open = new JMenuItem();
        JMenuItem copyMenu = new JMenuItem();
        JMenuItem openInBrowser = new JMenuItem();
        menu.add(initItem(open, "打开"));
        menu.add(initItem(copyMenu, "复制链接"));
        menu.add(initItem(openInBrowser, "在浏览器中打开"));
    }

    private JMenuItem initItem(JMenuItem item, String text) {
        item.setText(text);
        switch (text) {
            case "打开":
                item.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Integer id = (Integer) table.getValueAt(focusedRowIndex, 0);
                        String url = Utils.getUrlById(id);
                        new Thread(){
                            @Override
                            public void run() {
                                VideosList videosList = new VideosList();
                                videosList.setVisible(true);
                                VideoOrder videoOders = null;
                                try {
                                     videoOders = Utils.findVideoOders(url);
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                if (videoOders != null && videoOders.getUrls1() != null){
                                    System.out.println(Arrays.toString(videoOders.getUrls1()));
                                    videosList.setTable1(Utils.produceTable1(videoOders));
                                }
                                if (videoOders != null && videoOders.getUrls2() != null){
                                    System.out.println(Arrays.toString(videoOders.getUrls2()));
                                    videosList.setTable2(Utils.produceTable2(videoOders));
                                }
                            }
                        }.start();
                    }
                });
                break;
            case "复制链接":
                item.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Integer id = (Integer) table.getValueAt(focusedRowIndex, 0);
                        String url = Utils.getUrlById(id);
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
                        Integer id = (Integer) table.getValueAt(focusedRowIndex, 0);
                        String url = Utils.getUrlById(id);
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
}
