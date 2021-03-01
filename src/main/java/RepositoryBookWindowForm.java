import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import entity.Repository;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import ui.book.BookTableModel;
import ui.book.RepositoryColumn;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RepositoryBookWindowForm extends SimpleToolWindowPanel {

    private JPanel rootPanel;
    private JTextField textField1;
    private JPanel tablePanel;

    public RepositoryBookWindowForm() {
        super(true, true);
        createTable();
        getAll();
        setContent(rootPanel);
    }

    private final List<Repository> repositoryArrayList = new ArrayList<>();
    private List<Repository> originalData = new ArrayList<>();
    private JBTable table;

    private void createTable() {
        table = new JBTable(new BookTableModel(repositoryArrayList));
        table.setPreferredScrollableViewportSize(new Dimension(800, 300));
        table.setFillsViewportHeight(true);

        new RepositoryColumn(table, 0);

        JBScrollPane scrollPane = new JBScrollPane(table);
        tablePanel.setLayout(new GridLayout(1, 0));
        tablePanel.add(scrollPane);

    }

    private void getAll() {
        new OkHttpClient().newCall(new Request.Builder().url("http://jiangyy.cn:4903/v1/autogradle/repository").get().build())
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

}
