package com.jiangyy.ui;

import com.alibaba.fastjson.JSON;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.jiangyy.entity.Repository;
import com.jiangyy.entity.Resp;
import okhttp3.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;

public class HomeDialog extends JDialog {

    private boolean DEBUG = false;

    private Repository[] ALL_DATA = {
            new Repository(
                    false,
                    "AndPermission",
                    "yanzhenjie",
                    "2.0.1",
                    "com.yanzhenjie.permission:support:",
                    "",
                    "JCenter"
            ),
            new Repository(
                    false,
                    "BaseRecyclerViewAdapterHelper",
                    "CymChad",
                    "2.9.46",
                    "com.github.CymChad:BaseRecyclerViewAdapterHelper:",
                    "",
                    "JitPack"
            ),
            new Repository(
                    false,
                    "glide",
                    "bumptech",
                    "4.9.0",
                    "com.github.bumptech.glide:glide:",
                    "com.github.bumptech.glide:compiler:",
                    "MavenCentral"
            ),
            new Repository(
                    false,
                    "okhttp",
                    "square",
                    "3.13.1",
                    "com.squareup.okhttp3:okhttp:",
                    "",
                    "JCenter"
            ),
            new Repository(
                    false,
                    "retrofit",
                    "square",
                    "2.5.0",
                    "com.squareup.retrofit2:retrofit:",
                    "",
                    "JCenter"
            ),
            new Repository(
                    false,
                    "retrofit-adapter-rxjava2",
                    "square",
                    "2.5.0",
                    "com.squareup.retrofit2:adapter-rxjava2:",
                    "",
                    "JCenter"
            ),
            new Repository(
                    false,
                    "retrofit-converter-gson",
                    "square",
                    "2.5.0",
                    "com.squareup.retrofit2:converter-gson:",
                    "",
                    "JCenter"
            ),
            new Repository(
                    false,
                    "RxAndroid",
                    "ReactiveX",
                    "2.1.1",
                    "io.reactivex.rxjava2:rxandroid:",
                    "",
                    "JCenter"
            ),
            new Repository(
                    false,
                    "SmartRefreshLayout",
                    "scwang90",
                    "1.1.0-alpha-20",
                    "com.scwang.smartrefresh:SmartRefreshLayout:",
                    "",
                    "JCenter"
            ),
            new Repository(
                    false,
                    "smart-show",
                    "the-pig-of-jungle",
                    "2.6.3",
                    "com.github.the-pig-of-jungle.smart-show:toast:",
                    "",
                    "JitPack"
            ),
            new Repository(
                    false,
                    "XPopup",
                    "li-xiaojun",
                    "1.4.4",
                    "com.lxj:xpopup:",
                    "",
                    "JCenter"
            )
    };

    private Repository[] ALL_DATA_NO = {
            new Repository(
                    false,
                    "AndPermission",
                    "yanzhenjie",
                    "2.0.1",
                    "com.yanzhenjie.permission:support:",
                    "",
                    "JCenter"
            ),
            new Repository(
                    false,
                    "BaseRecyclerViewAdapterHelper",
                    "CymChad",
                    "2.9.46",
                    "com.github.CymChad:BaseRecyclerViewAdapterHelper:",
                    "",
                    "JitPack"
            ),
            new Repository(
                    false,
                    "glide",
                    "bumptech",
                    "4.9.0",
                    "com.github.bumptech.glide:glide:",
                    "com.github.bumptech.glide:compiler:",
                    "MavenCentral"
            ),
            new Repository(
                    false,
                    "okhttp",
                    "square",
                    "3.13.1",
                    "com.squareup.okhttp3:okhttp:",
                    "",
                    "JCenter"
            ),
            new Repository(
                    false,
                    "retrofit",
                    "square",
                    "2.5.0",
                    "com.squareup.retrofit2:retrofit:",
                    "",
                    "JCenter"
            ),
            new Repository(
                    false,
                    "retrofit-adapter-rxjava2",
                    "square",
                    "2.5.0",
                    "com.squareup.retrofit2:adapter-rxjava2:",
                    "",
                    "JCenter"
            ),
            new Repository(
                    false,
                    "retrofit-converter-gson",
                    "square",
                    "2.5.0",
                    "com.squareup.retrofit2:converter-gson:",
                    "",
                    "JCenter"
            ),
            new Repository(
                    false,
                    "RxAndroid",
                    "ReactiveX",
                    "2.1.1",
                    "io.reactivex.rxjava2:rxandroid:",
                    "",
                    "JCenter"
            ),
            new Repository(
                    false,
                    "SmartRefreshLayout",
                    "scwang90",
                    "1.1.0-alpha-20",
                    "com.scwang.smartrefresh:SmartRefreshLayout:",
                    "",
                    "JCenter"
            ),
            new Repository(
                    false,
                    "smart-show",
                    "the-pig-of-jungle",
                    "2.6.3",
                    "com.github.the-pig-of-jungle.smart-show:toast:",
                    "",
                    "JitPack"
            ),
            new Repository(
                    false,
                    "XPopup",
                    "li-xiaojun",
                    "1.4.4",
                    "com.lxj:xpopup:",
                    "",
                    "JCenter"
            )
    };

    private AnActionEvent event;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton buttonRefresh;
    private JButton buttonReset;
    private JCheckBox kotlinCheckBox;
    private JPanel buttonPane;
    private JPanel bottomPane;
    private JPanel tablePane;

    public HomeDialog(AnActionEvent event) {

        this.event = event;

        setContentPane(contentPane);
        setModal(true);
        setTitle("Auto Gradle");
        getRootPane().setDefaultButton(buttonOK);

        JBTable table = new JBTable(new MyTableModel());
        table.setPreferredScrollableViewportSize(new Dimension(800, 300));
        table.setFillsViewportHeight(true);
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (col == 3) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        URI uri = new URI("http://www.github.com/" + ALL_DATA[row].getUser() + "/" + ALL_DATA[row].getName());
                        desktop.browse(uri);
                    } catch (Exception ex) {
                        // do nothing
                        ex.printStackTrace();
                    }

                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        JBScrollPane scrollPane = new JBScrollPane(table);
        tablePane.setLayout(new GridLayout(1, 0));
        tablePane.add(scrollPane);

        buttonRefresh.addActionListener(e1 -> onRefresh(table, ALL_DATA));

        buttonReset.addActionListener(e1 -> onReset(table, ALL_DATA_NO));

        buttonOK.addActionListener(e1 -> onOK());

        buttonCancel.addActionListener(e1 -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e1) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e1 -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onRefresh(JBTable table, Repository[] data) {

        for (int index = 0; index < data.length; index++) {
            if (data[index].isChoose()) {
                getLastestTag(index, table);
            }
        }
    }

    private void onReset(JBTable table, Repository[] data) {
        for (int index = 0; index < data.length; index++) {
            table.setValueAt(data[index].isChoose(), index, 0);
            table.setValueAt(data[index].getVersion(), index, 2);
        }
    }

    private void onOK() {
        insetStringAfterOffset(event, ALL_DATA);
    }

    private void onCancel() {
        dispose();
    }

    private void insetStringAfterOffset(AnActionEvent e, Repository[] data) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        CaretModel caretModel = editor.getCaretModel();
        int offset = caretModel.getOffset();
        WriteCommandAction.runWriteCommandAction(e.getProject(), new Runnable() {
            @Override
            public void run() {
                for (Repository resp : data) {
                    if (resp.isChoose()) {
                        String impl = "\n    // " + resp.getName() + "\n    implementation \'" + resp.getGradle() + resp.getVersion() + "\'";
                        if (!resp.getProcessor().isEmpty()) {
                            if (kotlinCheckBox.isSelected()) {
                                impl += "\n    kapt \'" + resp.getProcessor() + resp.getVersion() + "\'";
                            } else {
                                impl += "\n    annotationProcessor \'" + resp.getProcessor() + resp.getVersion() + "\'";
                            }
                        }
                        editor.getDocument().insertString(offset, impl);
                    }
                }
            }
        });
        showNotification("implementation", "Auto Gradle", "implementation success");

    }

    private void getLastestTag(int row, JBTable table) {
        Repository repository = ALL_DATA[row];
        String url = "https://api.github.com/repos/" + repository.getUser() + "/" + repository.getName() + "/releases/latest";
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showNotification(repository.getName(), "错误提示", repository.getName() + " 版本查询失败");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Resp bean = JSON.parseObject(response.body().string(), Resp.class);
                table.setValueAt(bean.getTag_name(), row, 2);
                showNotification(repository.getName(), "更新成功", repository.getName() + " 更新完成：" + bean.getTag_name());


            }
        });

    }

    private void showNotification(String displayId, String title, String message) {
        NotificationGroup noti = new NotificationGroup(displayId, NotificationDisplayType.BALLOON, true);
        noti.createNotification(
                title,
                message,
                NotificationType.INFORMATION,
                null
        ).notify(event.getProject());
    }

    private class MyTableModel extends AbstractTableModel {

        private String[] columnNames = {"", "Repository", "Version", "Address"};

        @Override
        public int getRowCount() {
            return ALL_DATA.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return getValueAt(0, columnIndex).getClass();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 0 || columnIndex == 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Object o;
            switch (columnIndex) {
                case 0: {
                    o = ALL_DATA[rowIndex].isChoose();
                    break;
                }
                case 1: {
                    o = ALL_DATA[rowIndex].getName();
                    break;
                }
                case 2: {
                    o = ALL_DATA[rowIndex].getVersion();
                    break;
                }
                case 3: {
                    o = "Github";
                    break;
                }
                default: {
                    o = "";
                    break;
                }
            }
            return o;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//            if (DEBUG) {
//               println("Old value of data:" + JSON.toJSONString(ALL_DATA[rowIndex]));
//            }
            switch (columnIndex) {
                case 0:
                    ALL_DATA[rowIndex].setChoose((boolean) aValue);
                    break;
                case 2:
                    ALL_DATA[rowIndex].setVersion((String) aValue);
                    break;
                default:
                    break;
            }
            fireTableCellUpdated(rowIndex, columnIndex);

//            if (DEBUG) {
//                println("New value of data:" + JSON.toJSONString(ALL_DATA[rowIndex]));
//            }
        }
    }

}
