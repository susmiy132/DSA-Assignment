package com.example.programmingassignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class Q2aCalculatorApp {
    private boolean isNewOperator = true;
    private JTextField etResult;
    private JButton btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    private JButton btnMultiply, btnDecimal, btnAdd, btnSubtract, btnDivide, btnEqual, btnReset;
    private JButton btnLeftParen, btnRightParen;

    public Q2aCalculatorApp() {
        JFrame frame = new JFrame("Calculator");
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);

        // Set the background color
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);

        etResult = new JTextField();
        etResult.setBounds(30, 40, 340, 50);
        etResult.setHorizontalAlignment(JTextField.RIGHT);
        etResult.setFont(new Font("Arial", Font.PLAIN, 24));
        frame.add(etResult);

        // Initialize buttons with white background
        btn0 = createButton("0");
        btn1 = createButton("1");
        btn2 = createButton("2");
        btn3 = createButton("3");
        btn4 = createButton("4");
        btn5 = createButton("5");
        btn6 = createButton("6");
        btn7 = createButton("7");
        btn8 = createButton("8");
        btn9 = createButton("9");
        btnDecimal = createButton(".");
        btnMultiply = createButton("*");
        btnAdd = createButton("+");
        btnSubtract = createButton("-");
        btnDivide = createButton("/");
        btnEqual = createButton("=");
        btnReset = createButton("C");
        btnLeftParen = createButton("(");
        btnRightParen = createButton(")");

        JButton[] buttons = {
                btnLeftParen, btnRightParen, btnDivide, btnMultiply,
                btn7, btn8, btn9, btnSubtract,
                btn4, btn5, btn6, btnAdd,
                btn1, btn2, btn3, btnEqual,
                btn0, btnDecimal, btnReset
        };

        int xPos = 30;
        int yPos = 100;
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBounds(xPos, yPos, 80, 80);
            frame.add(buttons[i]);
            xPos += 90;

            if ((i + 1) % 4 == 0) {
                xPos = 30;
                yPos += 90;
            }
        }

        // Set action listeners for buttons
        btn0.addActionListener(new ButtonClickListener());
        btn1.addActionListener(new ButtonClickListener());
        btn2.addActionListener(new ButtonClickListener());
        btn3.addActionListener(new ButtonClickListener());
        btn4.addActionListener(new ButtonClickListener());
        btn5.addActionListener(new ButtonClickListener());
        btn6.addActionListener(new ButtonClickListener());
        btn7.addActionListener(new ButtonClickListener());
        btn8.addActionListener(new ButtonClickListener());
        btn9.addActionListener(new ButtonClickListener());
        btnDecimal.addActionListener(new ButtonClickListener());

        btnMultiply.addActionListener(new OperatorClickListener());
        btnAdd.addActionListener(new OperatorClickListener());
        btnSubtract.addActionListener(new OperatorClickListener());
        btnDivide.addActionListener(new OperatorClickListener());

        btnEqual.addActionListener(new CalculateClickListener());
        btnReset.addActionListener(new ResetClickListener());
        btnLeftParen.addActionListener(new ParenthesisClickListener());
        btnRightParen.addActionListener(new ParenthesisClickListener());

        frame.setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.WHITE); // Set button background color to white
        return button;
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isNewOperator) {
                etResult.setText("");
            }
            isNewOperator = false;
            String btnText = ((JButton) e.getSource()).getText();
            etResult.setText(etResult.getText() + btnText);
        }
    }

    private class OperatorClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            isNewOperator = false;
            String btnText = ((JButton) e.getSource()).getText();
            String currentText = etResult.getText();

            if (!currentText.isEmpty()) {
                char lastChar = currentText.charAt(currentText.length() - 1);

                // Avoid multiple operators in a row
                if ("+-*/".indexOf(lastChar) != -1) {
                    currentText = currentText.substring(0, currentText.length() - 1);
                }

                etResult.setText(currentText + btnText);
            }
        }
    }

    private class ParenthesisClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String btnText = ((JButton) e.getSource()).getText();
            etResult.setText(etResult.getText() + btnText);
        }
    }

    private class CalculateClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String expression = etResult.getText();
            try {
                double result = evaluateExpression(expression);
                etResult.setText(String.valueOf(result));
            } catch (Exception ex) {
                etResult.setText("Error");
            }
        }

        private double evaluateExpression(String expression) {
            // Remove extra spaces and validate
            expression = expression.replaceAll(" ", "");
            if (!isValidExpression(expression)) {
                throw new IllegalArgumentException("Invalid expression");
            }
            return evaluatePostfix(infixToPostfix(expression));
        }

        private boolean isValidExpression(String expression) {
            // Simple validation for expression balance and format
            int balance = 0;
            for (char c : expression.toCharArray()) {
                if (c == '(') balance++;
                if (c == ')') balance--;
                if (balance < 0) return false;
            }
            return balance == 0;
        }

        private String infixToPostfix(String infix) {
            StringBuilder postfix = new StringBuilder();
            Stack<Character> stack = new Stack<>();

            for (char ch : infix.toCharArray()) {
                if (Character.isDigit(ch) || ch == '.') {
                    postfix.append(ch);
                } else if (ch == '(') {
                    stack.push(ch);
                } else if (ch == ')') {
                    while (stack.peek() != '(') {
                        postfix.append(' ').append(stack.pop());
                    }
                    stack.pop();
                } else {
                    postfix.append(' ');
                    while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(ch)) {
                        postfix.append(stack.pop()).append(' ');
                    }
                    stack.push(ch);
                }
            }

            while (!stack.isEmpty()) {
                postfix.append(' ').append(stack.pop());
            }

            return postfix.toString();
        }

        private int precedence(char operator) {
            switch (operator) {
                case '+':
                case '-':
                    return 1;
                case '*':
                case '/':
                    return 2;
                default:
                    return -1;
            }
        }

        private double evaluatePostfix(String postfix) {
            Stack<Double> stack = new Stack<>();
            for (String token : postfix.split(" ")) {
                if (token.isEmpty()) continue;
                if (Character.isDigit(token.charAt(0))) {
                    stack.push(Double.parseDouble(token));
                } else {
                    double b = stack.pop();
                    double a = stack.pop();
                    switch (token.charAt(0)) {
                        case '+':
                            stack.push(a + b);
                            break;
                        case '-':
                            stack.push(a - b);
                            break;
                        case '*':
                            stack.push(a * b);
                            break;
                        case '/':
                            stack.push(a / b);
                            break;
                    }
                }
            }
            return stack.pop();
        }
    }

    private class ResetClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            etResult.setText("");
            isNewOperator = true;
        }
    }

    public static void main(String[] args) {
        new Q2aCalculatorApp();
    }
}
