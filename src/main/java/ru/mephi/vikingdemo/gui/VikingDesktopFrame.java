package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;
import ru.mephi.vikingdemo.service.VikingServiceAnalyzer;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;


public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingServiceAnalyzer analyzer;
    private final VikingTableModel tableModel = new VikingTableModel();

    public VikingDesktopFrame(VikingService vikingService, VikingServiceAnalyzer analyzer) {
        this.vikingService = vikingService;
        this.analyzer = analyzer;

        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 420));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Viking Demo", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        JTable vikingTable = new JTable(tableModel);
        vikingTable.setRowHeight(28);
        add(new JScrollPane(vikingTable), BorderLayout.CENTER);

        JButton createButton = new JButton("Create random viking");
        createButton.addActionListener(event -> onCreateViking());

        JButton createBtn = new JButton("Create many viking");
        createBtn.addActionListener(e -> {
            List<Viking> army = vikingService.generateManyRandomVikings(100);
            army.forEach(tableModel::addViking);
        });

        JButton statsBtn = new JButton("Statistics");
        statsBtn.addActionListener(e -> showStatisticsDialog());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(createButton);
        add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(createBtn);
        bottomPanel.add(statsBtn);
        onInit();
    }

    private void onCreateViking() {
        Viking viking = vikingService.createRandomViking();
        tableModel.addViking(viking);
    }

    public void addNewViking(Viking viking){
        tableModel.addViking(viking);
    }

    private void onInit() {
        List<Viking> all = vikingService.findAll();
        if (!all.isEmpty()){
            for (Viking viking : all) {
                tableModel.addViking(viking);
            }
        }
    }

    private void showStatisticsDialog() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        ButtonGroup group = new ButtonGroup();
        JRadioButton opt1 = new JRadioButton("Легендарное снаряжение", true);
        JRadioButton opt2 = new JRadioButton("Максимальный ID");
        JRadioButton opt3 = new JRadioButton("Случайный великан (>180см)");
        JRadioButton opt4 = new JRadioButton("Рыжие с длинной бородой");
        JRadioButton opt5 = new JRadioButton("Имеют 1 топор");
        JRadioButton opt6 = new JRadioButton("Имеют 2 топора");
        JRadioButton opt7 = new JRadioButton("Рыжебородые сортировка по возрасту");
        JRadioButton opt8 = new JRadioButton("Условиям по возрасту больше 20");
        JRadioButton opt9 = new JRadioButton("Условиям по возрасту меньше 20");
        JRadioButton opt10 = new JRadioButton("Условиям по возрасту в диапазоне от 20 до 40");
        JRadioButton opt11 = new JRadioButton("Условиям по возрасту вне диапазона от 20 до 40");

        group.add(opt1);
        group.add(opt2);
        group.add(opt3);
        group.add(opt4);
        group.add(opt5);
        group.add(opt6);
        group.add(opt7);
        group.add(opt8);
        group.add(opt9);
        group.add(opt10);
        group.add(opt11);


        panel.add(new JLabel("Выберите тип анализа:"));
        panel.add(opt1);
        panel.add(opt2);
        panel.add(opt3);
        panel.add(opt4);
        panel.add(opt5);
        panel.add(opt6);
        panel.add(opt7);
        panel.add(opt8);
        panel.add(opt9);
        panel.add(opt10);
        panel.add(opt11);

        int result = JOptionPane.showConfirmDialog(this, panel, "Аналитика поселения", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String output = "";
            if (opt1.isSelected()) {
                output = "Викинги с легендаркой: " + analyzer.getVikingsWithLegendaryEquipment().stream().map(Viking::name).toList();
            } else if (opt2.isSelected()) {
                output = "Максимальный ID: " + analyzer.getMaxId().orElse(0);
            } else if (opt3.isSelected()) {
                output = analyzer.getRandomTallViking().map(v -> "Наш гигант: " + v.name()).orElse("Не найдены");
            }
            else if (opt4.isSelected()) {
                long count = analyzer.countByBeardAndHair(ru.mephi.vikingdemo.model.BeardStyle.LONG, HairColor.Red);
                output = "Рыжих с длинной бородой: " + count;
            } else if (opt5.isSelected()) {
                output = "Викингов с 1 топором: " + analyzer.countByAxeQuantity(1);
            } else if (opt6.isSelected()) {
                output = "Викингов с 2 топорами: " + analyzer.countByAxeQuantity(2);
            } else if (opt8.isSelected()) {
                output = "Количество викингов старше 20 лет: " + analyzer.countVikingsOlderThan(20);

            } else if (opt9.isSelected()) {
                output = "Количество викингов младше 20 лет: " + analyzer.countVikingsYoungerThan(20);

            } else if (opt10.isSelected()) {
                output = "Количество викингов в диапазоне 20-40: " + analyzer.countVikingsAgeBetween(20, 40);

            } else if (opt11.isSelected()) {
                output = "Количество викингов вне диапазона 20-40: " + analyzer.countVikingsAgeOutside(20, 40);
            }
            JOptionPane.showMessageDialog(this, output, "Результат анализа", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
