import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBDimension;
import entity.Repository;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ui.home.HomeTableModel;
import ui.home.IconColumn;
import utils.Utils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeDialog extends DialogWrapper implements DocumentListener, MouseListener {

    private JPanel contentPanel;
    private JTextField searchTextField;
    private JPanel tablePanel;

    private final AnActionEvent event;
    private final List<Repository> repositoryArrayList = new ArrayList<>();
    private List<Repository> originalData = new ArrayList<>();
    private JBTable table;

    public HomeDialog(AnActionEvent event) {
        super(true);
        init();
        this.event = event;
        setTitle("Auto Gradle");
        contentPanel.setPreferredSize(new JBDimension(800, 450));
        contentPanel.setMinimumSize(new JBDimension(800, 450));
        createTable();
        getAll();
        searchTextField.getDocument().addDocumentListener(this);
    }

    @Override
    protected @Nullable
    JComponent createCenterPanel() {
        return contentPanel;
    }

    @Override
    protected @NotNull
    DialogStyle getStyle() {
        return DialogStyle.COMPACT;
    }

    private void createTable() {
        table = new JBTable(new HomeTableModel(repositoryArrayList));
        table.getColumnModel().getColumn(0).setMinWidth(50);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setMinWidth(200);
        table.getColumnModel().getColumn(2).setMinWidth(100);
        table.getColumnModel().getColumn(3).setMinWidth(100);
        table.setPreferredScrollableViewportSize(new Dimension(800, 300));
        table.setRowHeight(36);
        table.setFillsViewportHeight(true);
        table.addMouseListener(this);

        new IconColumn(table, 4);

        JBScrollPane scrollPane = new JBScrollPane(table);
        tablePanel.setLayout(new GridLayout(1, 0));
        tablePanel.add(scrollPane);

    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
        insetStringAfterOffset(event);
    }

    private void getAll() {
        new OkHttpClient().newCall(new Request.Builder().url("https://95factory.com/v1/autogradle/repository").get().build())
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        originalData = new Gson().fromJson(
                                response.body().string(), new TypeToken<List<Repository>>() {
                                }.getType()
                        );
                        repositoryArrayList.clear();
                        repositoryArrayList.addAll(originalData);
                        table.updateUI();
                    }
                });
    }

    /**
     * 输入框搜索Repository
     *
     * @param documentEvent 输入框内容
     */
    private void search(DocumentEvent documentEvent) {
        Document document = documentEvent.getDocument();
        try {
            String input = document.getText(0, document.getLength()).toLowerCase();
            repositoryArrayList.clear();
            if (input.isEmpty()) {
                repositoryArrayList.addAll(originalData);
            } else {
                for (int i = 0; i < originalData.size(); i++) {
                    if (originalData.get(i).getNickname().toLowerCase().contains(input)) {
                        repositoryArrayList.add(originalData.get(i));
                    }
                }
            }
            table.updateUI();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void insetStringAfterOffset(AnActionEvent e) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        CaretModel caretModel = editor.getCaretModel();
        int offset = caretModel.getOffset();
        WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
            for (Repository resp : repositoryArrayList) {
                if (resp.isChoose()) {
                    editor.getDocument().insertString(offset, Utils.inset(resp));
                }
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        int row = table.rowAtPoint(mouseEvent.getPoint());
        int col = table.columnAtPoint(mouseEvent.getPoint());
        switch (col) {
            case 4: // 跳转Github
                Utils.startUri(repositoryArrayList.get(row));
                break;
            default:
                break;
        }

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void insertUpdate(DocumentEvent documentEvent) {
        search(documentEvent);
    }

    @Override
    public void removeUpdate(DocumentEvent documentEvent) {
        search(documentEvent);
    }

    @Override
    public void changedUpdate(DocumentEvent documentEvent) {

    }

}
