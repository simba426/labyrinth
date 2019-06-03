package labyrinth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Vector;

public class ResultDialog extends JDialog {
    private static JPanel panel = new JPanel();      //创建内容面板
    private static JButton close = new JButton("退出");   //设置关闭按钮
    Vector<Vector<String>> rowData = new Vector<Vector<String>>(); //存储结果数据

    public Object seirCopy(Object src) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            Object dest = in.readObject();
            return dest;
        } catch (Exception e) {
            //do some error handler
            return null;
        }
    }

    public ResultDialog() {
        //初始化窗口
        initBasic();

        //初始化表格
        initTable();

    }

    private void initBasic() {
        this.setTitle("游戏记录");
        this.setResizable(false);
        this.setLayout(null);
        setDefaultCloseOperation(0); //设置点击关闭时不做操作
    }

    private void initTable() {
        //先清空内部所有组件
        panel.removeAll();
        //设置确认按钮
        close.setBounds(10, 80, 80, 25);
        panel.add(close);
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 进行逻辑处理即可
                setVisible(false);
            }
        });

        // 表头（列名）
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("角色名");
        columnNames.add("步数");
        columnNames.add("红蘑菇");
        columnNames.add("蓝蘑菇");
        columnNames.add("得分");

        //从文件中读取数据保存到labyrinthData数组中
        String[] line = new String[100];
        int m = 0;
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("src/labyrinth/result.txt")));

            String line1;
            while ((line1 = br.readLine()) != null) {
                System.out.println(line1);//每行输出
                line[m] = line1;
                m++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(m);

        for (int i = 0; i < m; i++) {
            Vector<String> row0 = new Vector<String>();
            for (int j = 0; j < 5; j++) {
                row0.add(line[i].split(",")[j]);
            }
            rowData.add(row0);
        }
        /*
        for (int i = 0; i < m; i++){
            switch (i % 2){
                case 0:
                    Vector<String> row0 = new Vector<String>();
                    for (int j = 0; j < 5; j++){
                        row0.add(line[i].split(",")[j]);
                    }
                    rowData.add(row0);
                    break;
                case 1:
                    Vector<String> row1 = new Vector<String>();
                    for (int j = 0; j < 5; j++){
                        row1.add(line[i].split(",")[j]);
                    }
                    rowData.add(row1);
                    break;
            }
        }

        for (int i = 0; i < m; i++){
            switch (i % 10){
                case 0:
                    Vector<String> row0 = new Vector<String>();
                    for (int j = 0; j < 5; j++){
                        row0.add(line[i].split(",")[j]);
                    }
                    rowData.add(row0);
                    break;
                case 1:
                    Vector<String> row1 = new Vector<String>();
                    for (int j = 0; j < 5; j++){
                        row1.add(line[i].split(",")[j]);
                    }
                    rowData.add(row1);
                    break;
                case 2:
                    Vector<String> row2 = new Vector<String>();
                    for (int j = 0; j < 5; j++){
                        row2.add(line[i].split(",")[j]);
                    }
                    rowData.add(row2);
                    break;
                case 3:
                    Vector<String> row3 = new Vector<String>();
                    for (int j = 0; j < 5; j++){
                        row3.add(line[i].split(",")[j]);
                    }
                    rowData.add(row3);
                    break;
                case 4:
                    Vector<String> row4 = new Vector<String>();
                    for (int j = 0; j < 5; j++){
                        row4.add(line[i].split(",")[j]);
                    }
                    rowData.add(row4);
                    break;
                case 5:
                    Vector<String> row5 = new Vector<String>();
                    for (int j = 0; j < 5; j++){
                        row5.add(line[i].split(",")[j]);
                    }
                    rowData.add(row5);
                    break;
                case 6:
                    Vector<String> row6 = new Vector<String>();
                    for (int j = 0; j < 5; j++){
                        row6.add(line[i].split(",")[j]);
                    }
                    rowData.add(row6);
                    break;
                case 7:
                    Vector<String> row7 = new Vector<String>();
                    for (int j = 0; j < 5; j++){
                        row7.add(line[i].split(",")[j]);
                    }
                    rowData.add(row7);
                    break;
                case 8:
                    Vector<String> row8 = new Vector<String>();
                    for (int j = 0; j < 5; j++){
                        row8.add(line[i].split(",")[j]);
                    }
                    rowData.add(row8);
                    break;
                case 9:
                    Vector<String> row9 = new Vector<String>();
                    for (int j = 0; j < 5; j++){
                        row9.add(line[i].split(",")[j]);
                    }
                    rowData.add(row9);
                    break;
            }
        }
        */


        JTable table = new JTable(rowData, columnNames);
        // 设置表格内容颜色
        table.setForeground(Color.BLACK);                               // 字体颜色
        table.setFont(new Font(null, Font.PLAIN, 14));      // 字体样式
        table.setSelectionForeground(Color.DARK_GRAY);                  // 选中后字体颜色
        table.setSelectionBackground(Color.LIGHT_GRAY);                 // 选中后字体背景
        table.setGridColor(Color.GRAY);                                 // 网格颜色

        // 设置表头
        table.getTableHeader().setFont(new Font(null, Font.BOLD, 14));  // 设置表头名称字体样式
        table.getTableHeader().setForeground(Color.RED);                // 设置表头名称字体颜色
        table.getTableHeader().setResizingAllowed(false);               // 设置不允许手动改变列宽
        table.getTableHeader().setReorderingAllowed(false);             // 设置不允许拖动重新排序各列

        // 设置行高
        table.setRowHeight(30);

        // 第一列列宽设置为40
        table.getColumnModel().getColumn(0).setPreferredWidth(40);

        // 设置滚动面板视口大小（超过该大小的行数据，需要拖动滚动条才能看到）
        table.setPreferredScrollableViewportSize(new Dimension(400, 300));

        // 把 表格 放到 滚动面板 中（表头将自动添加到滚动面板顶部）
        JScrollPane scrollPane = new JScrollPane(table);

        // 添加 滚动面板 到 内容面板
        panel.add(scrollPane);

        // 设置 内容面板 到 窗口
        this.setContentPane(panel);

        this.pack();
        this.setLocationRelativeTo(null);

    }

}
