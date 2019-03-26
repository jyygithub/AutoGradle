package com.jiangyy.ui;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import com.jiangyy.utils.ParserUtils;
import okhttp3.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class HomeDialog extends JFrame {

    private boolean DEBUG = false;

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

    private List<Repository> ALL_DATA;
    private List<Repository> ALL_DATA_NO;

    public HomeDialog(AnActionEvent event) {

        this.event = event;
        initData();
        setContentPane(contentPane);
//        setModal(true);
        setTitle("Auto Gradle");
        getRootPane().setDefaultButton(buttonOK);

        JBTable table = new JBTable(new MyTableModel(ALL_DATA));
        table.setPreferredScrollableViewportSize(new Dimension(800, 300));
        table.setFillsViewportHeight(true);
//        table.getTableHeader().setDefaultRenderer(new CheckHeaderCellRenderer(table));
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (col == 3) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        URI uri = new URI("http://www.github.com/" + ALL_DATA.get(row).getUser() + "/" + ALL_DATA.get(row).getName());
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

    private void onRefresh(JBTable table, List<Repository> data) {

        for (int index = 0; index < data.size(); index++) {
            if (data.get(index).isChoose()) {
                getLastestTag(index, table);
            }
        }
    }

    private void onReset(JBTable table, List<Repository> data) {
        for (int index = 0; index < data.size(); index++) {
            table.setValueAt(data.get(index).isChoose(), index, 0);
            table.setValueAt(data.get(index).getVersion(), index, 2);
        }
    }

    private void onOK() {
        insetStringAfterOffset(event, ALL_DATA);
    }

    private void onCancel() {
        dispose();
    }

    private void insetStringAfterOffset(AnActionEvent e, List<Repository> data) {
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
        Repository repository = ALL_DATA.get(row);
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
                table.setValueAt(bean.getName(), row, 4);
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


    private void initData() {
        String path = getClass().getClassLoader().getResource("alldata.json").getPath();
        String s = ParserUtils.readJsonFile(path);
        ALL_DATA = JSONArray.parseArray(s, Repository.class);
        ALL_DATA_NO = JSONArray.parseArray(s, Repository.class);
    }

}
