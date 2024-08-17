package com.example.programmingassignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class DeliveryOptimizationApp {

    private JFrame frame;
    private JTextArea deliveryListArea;
    private JTextField vehicleCapacityField;
    private JTextField maxDistanceField;
    private JComboBox<String> algorithmComboBox;
    private JButton optimizeButton;
    private JPanel mapPanel;
    private JTextArea statusArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DeliveryOptimizationApp window = new DeliveryOptimizationApp();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public DeliveryOptimizationApp() {
        // Initialize the frame
        frame = new JFrame("Delivery Route Optimization");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create and add components
        createComponents();
        arrangeComponents();

        frame.setVisible(true);
    }

    private void createComponents() {
        // Delivery List Text Area
        deliveryListArea = new JTextArea(5, 30);
        deliveryListArea.setBorder(BorderFactory.createTitledBorder("Delivery List (Address, Priority)"));
        deliveryListArea.setLineWrap(true);
        deliveryListArea.setWrapStyleWord(true);

        // Vehicle Capacity Field
        vehicleCapacityField = new JTextField(10);
        vehicleCapacityField.setBorder(BorderFactory.createTitledBorder("Vehicle Capacity"));

        // Max Distance Field
        maxDistanceField = new JTextField(10);
        maxDistanceField.setBorder(BorderFactory.createTitledBorder("Max Distance"));

        // Algorithm ComboBox
        String[] algorithms = {"Dijkstra", "A* Search", "Genetic Algorithm"};
        algorithmComboBox = new JComboBox<>(algorithms);
        algorithmComboBox.setBorder(BorderFactory.createTitledBorder("Choose Algorithm"));

        // Optimize Button
        optimizeButton = new JButton("Optimize Route");
        optimizeButton.setBackground(new Color(0, 100, 0)); // Dark green
        optimizeButton.setForeground(Color.WHITE);
        optimizeButton.setOpaque(true);
        optimizeButton.setBorderPainted(false);
        optimizeButton.addActionListener(new OptimizeAction());
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setBackground(Color.LIGHT_GRAY);

        // Status Area
        statusArea = new JTextArea(5, 30);
        statusArea.setEditable(false);
        statusArea.setBorder(BorderFactory.createTitledBorder("Status"));

        // Map Panel for Route Visualization
        mapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Visualization code here
            }
        };
        mapPanel.setBorder(BorderFactory.createTitledBorder("Route Visualization"));
        mapPanel.setPreferredSize(new Dimension(400, 400));
        mapPanel.setBackground(Color.WHITE);
    }

    private void arrangeComponents() {
        // Create a panel for input fields and buttons
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));
        inputPanel.add(vehicleCapacityField);
        inputPanel.add(maxDistanceField);
        inputPanel.add(algorithmComboBox);
        inputPanel.add(optimizeButton);

        // Add components to the frame
        frame.add(new JScrollPane(deliveryListArea), BorderLayout.NORTH);
        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(new JScrollPane(statusArea), BorderLayout.SOUTH);
        frame.add(mapPanel, BorderLayout.EAST);
    }

    private class OptimizeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String deliveryList = deliveryListArea.getText();
            String vehicleCapacityText = vehicleCapacityField.getText();
            String maxDistanceText = maxDistanceField.getText();

            // Validate vehicle capacity
            int vehicleCapacity;
            try {
                vehicleCapacity = Integer.parseInt(vehicleCapacityText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid vehicle capacity. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate max distance
            int maxDistance;
            try {
                maxDistance = Integer.parseInt(maxDistanceText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid max distance. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();

            // Placeholder for optimization logic
            statusArea.setText("Starting optimization using " + selectedAlgorithm + "...\n");
            // Here you would implement the chosen algorithm and update mapPanel with the route visualization

            // Example status update (mocked result)
            statusArea.append("Optimization completed. Route is displayed on the map.\n");
        }
    }
}
