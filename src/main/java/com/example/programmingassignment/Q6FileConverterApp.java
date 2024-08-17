package com.example.programmingassignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Q6FileConverterApp {

    private JFrame frame;
    private JButton selectFilesButton, startButton, cancelButton;
    private JProgressBar overallProgressBar;
    private JTextArea statusTextArea;
    private JFileChooser fileChooser;
    private ThreadPoolExecutor executor;
    private List<FileConversionWorker> workers;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Q6FileConverterApp window = new Q6FileConverterApp();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Q6FileConverterApp() {
        frame = new JFrame("File Converter");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setBackground(Color.LIGHT_GRAY);

        // File selection button
        selectFilesButton = new JButton("Select Files");
        selectFilesButton.setBackground(Color.LIGHT_GRAY);
        selectFilesButton.setOpaque(true);
        selectFilesButton.setBorderPainted(false);
        selectFilesButton.addActionListener(new FileSelectAction());

        // Start button
        startButton = new JButton("Start Conversion");
        startButton.setBackground(new Color(0, 100, 0)); // Dark green
        startButton.setOpaque(true);
        startButton.setBorderPainted(false);
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(new StartConversionAction());

        // Cancel button
        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.RED);
        cancelButton.setOpaque(true);
        cancelButton.setBorderPainted(false);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(new CancelAction());

        // Progress bar
        overallProgressBar = new JProgressBar();
        overallProgressBar.setStringPainted(true);

        // Status text area
        statusTextArea = new JTextArea();
        statusTextArea.setEditable(false);
        statusTextArea.setRows(10);
        statusTextArea.setLineWrap(true);
        statusTextArea.setWrapStyleWord(true);

        // Panels for layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(selectFilesButton);
        buttonPanel.add(startButton);
        buttonPanel.add(cancelButton);

        // Adding components to the frame
        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(statusTextArea), BorderLayout.CENTER);
        frame.getContentPane().add(overallProgressBar, BorderLayout.SOUTH);

        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4); // Thread pool for file conversion
        workers = new ArrayList<>();
    }

    private class FileSelectAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = fileChooser.getSelectedFiles();
                statusTextArea.append("Selected files:\n");
                for (File file : selectedFiles) {
                    statusTextArea.append(file.getAbsolutePath() + "\n");
                }
                workers.clear();
                overallProgressBar.setValue(0);
            }
        }
    }

    private class StartConversionAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (fileChooser.getSelectedFiles() == null || fileChooser.getSelectedFiles().length == 0) {
                JOptionPane.showMessageDialog(frame, "Please select files first.");
                return;
            }

            File[] filesToConvert = fileChooser.getSelectedFiles();
            overallProgressBar.setMaximum(filesToConvert.length);
            for (File file : filesToConvert) {
                FileConversionWorker worker = new FileConversionWorker(file);
                workers.add(worker);
                worker.execute();
            }
        }
    }

    private class CancelAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (FileConversionWorker worker : workers) {
                worker.cancel(true);
            }
            executor.shutdownNow();
            statusTextArea.append("Conversion process cancelled.\n");
        }
    }

    private class FileConversionWorker extends SwingWorker<Void, String> {
        private File file;

        public FileConversionWorker(File file) {
            this.file = file;
        }

        @Override
        protected Void doInBackground() throws Exception {
            setProgress(0);
            // Simulate file conversion process
            for (int i = 0; i < 100; i++) {
                if (isCancelled()) {
                    return null;
                }
                Thread.sleep(50); // Simulate processing time
                publish("Processing file: " + file.getName() + " - " + (i + 1) + "%");
                setProgress(i + 1);
            }
            return null;
        }

        @Override
        protected void process(List<String> chunks) {
            for (String message : chunks) {
                statusTextArea.append(message + "\n");
            }
        }

        @Override
        protected void done() {
            try {
                get();
                statusTextArea.append("Conversion completed for file: " + file.getName() + "\n");
            } catch (Exception e) {
                statusTextArea.append("Conversion failed for file: " + file.getName() + "\n");
            }
            overallProgressBar.setValue(overallProgressBar.getValue() + 1);
            if (overallProgressBar.getValue() >= overallProgressBar.getMaximum()) {
                statusTextArea.append("All conversions completed.\n");
            }
        }
    }
}
