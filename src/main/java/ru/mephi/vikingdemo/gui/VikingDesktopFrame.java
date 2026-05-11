package ru.mephi.vikingdemo.gui;

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
        JRadioButton opt3 = new JRadioButton("Четные ID");
        JRadioButton opt4 = new JRadioButton("Случайный великан (>180см)");
        JRadioButton opt5 = new JRadioButton("Кол-во викингов старше 40");

        group.add(opt1);
        group.add(opt2);
        group.add(opt3);
        group.add(opt4);
        group.add(opt5);

        panel.add(new JLabel("Выберите тип анализа:"));
        panel.add(opt1); panel.add(opt2); panel.add(opt3); panel.add(opt4); panel.add(opt5);

        int result = JOptionPane.showConfirmDialog(this, panel, "Аналитика поселения", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String output = "";
            if (opt1.isSelected()) {
                output = "Викинги с легендарной техники: " + analyzer.getVikingsWithLegendaryEquipment().stream().map(Viking::name).toList();
            } else if (opt2.isSelected()) {
                output = "Максимальный ID: " + analyzer.getMaxId().orElse(0);
            } else if (opt3.isSelected()) {
                output = "Список четных ID: " + analyzer.getEvenIds();
            } else if (opt4.isSelected()) {
                output = analyzer.getRandomTallViking().map(v -> "Наш викинг: " + v.name() + " (" + v.heightCm() + " см)").orElse("Викинги не найдены");
            } else if (opt5.isSelected()) {
                output = "Викингов старше 40 лет: " + analyzer.countVikingsOlderThan(40);
            }
            JOptionPane.showMessageDialog(this, output, "Результат анализа", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
