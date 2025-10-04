package com.main.midterm;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.Stack;

public class CalcFrag extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button zero, one, two, three, four, five, six, seven, eight, nine,
            plus, minus, mult, div, delete, deci, submit, leftBracket, rightBracket, clear;
    private TextView operation, result;
    private String mParam1;
    private String mParam2;

    public CalcFrag() {
        // Required empty public constructor
    }

    public static CalcFrag newInstance(String param1, String param2) {
        CalcFrag fragment = new CalcFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calc, container, false);

        // Initialize views
        initializeViews(view);

        // Set up number button listeners
        setupNumberButtons();

        // Set up operator button listeners
        setupOperatorButtons();

        // Set up function button listeners
        setupFunctionButtons();

        return view;
    }

    private void initializeViews(View view) {
        zero = view.findViewById(R.id.zero);
        one = view.findViewById(R.id.one);
        two = view.findViewById(R.id.two);
        three = view.findViewById(R.id.three);
        four = view.findViewById(R.id.four);
        five = view.findViewById(R.id.five);
        six = view.findViewById(R.id.six);
        seven = view.findViewById(R.id.seven);
        eight = view.findViewById(R.id.eight);
        nine = view.findViewById(R.id.nine);
        plus = view.findViewById(R.id.plus);
        minus = view.findViewById(R.id.subtract);
        mult = view.findViewById(R.id.mult);
        div = view.findViewById(R.id.divide);
        delete = view.findViewById(R.id.delete);
        clear = view.findViewById(R.id.clear);
        deci = view.findViewById(R.id.decipoint);
        submit = view.findViewById(R.id.equals);
        leftBracket = view.findViewById(R.id.leftbracket);
        rightBracket = view.findViewById(R.id.rightbracket);
        operation = view.findViewById(R.id.operation);
        result = view.findViewById(R.id.Result);
    }

    private void setupNumberButtons() {
        View.OnClickListener numberListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button) v;
                operation.setText(operation.getText().toString() + btn.getText().toString());
            }
        };

        zero.setOnClickListener(numberListener);
        one.setOnClickListener(numberListener);
        two.setOnClickListener(numberListener);
        three.setOnClickListener(numberListener);
        four.setOnClickListener(numberListener);
        five.setOnClickListener(numberListener);
        six.setOnClickListener(numberListener);
        seven.setOnClickListener(numberListener);
        eight.setOnClickListener(numberListener);
        nine.setOnClickListener(numberListener);
        deci.setOnClickListener(numberListener);
    }

    private void setupOperatorButtons() {
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation.setText(operation.getText().toString() + "+");
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation.setText(operation.getText().toString() + "-");
            }
        });

        mult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation.setText(operation.getText().toString() + "×");
            }
        });

        div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation.setText(operation.getText().toString() + "/");
            }
        });

        leftBracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation.setText(operation.getText().toString() + "(");
            }
        });

        rightBracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation.setText(operation.getText().toString() + ")");
            }
        });
    }

    private void setupFunctionButtons() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String erasedValue = operation.getText().toString();;
                if (!erasedValue.isEmpty()){
                    erasedValue = erasedValue.substring(0, erasedValue.length()-1);
                    operation.setText(erasedValue);
                }

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation.setText("");
                result.setText("RESULT");
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expression = operation.getText().toString();
                if (!expression.isEmpty()) {
                    try {
                        double calculatedResult = evaluateExpression(expression);
                        result.setText(String.valueOf(calculatedResult));
                    } catch (Exception e) {
                        result.setText("Error");
                    }
                }
            }
        });
    }

    // PEMDAS Expression Evaluator
    private double evaluateExpression(String expression) {
        // Replace × with *
        expression = expression.replace("×", "*");

        // Insert zero before unary minus
        expression = normalizeUnaryMinus(expression);

        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (c == ' ') continue;

            if (Character.isDigit(c) || c == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() &&
                        (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                i--;
                numbers.push(Double.parseDouble(sb.toString()));
            }
            else if (c == '(') {
                operators.push(c);
            }
            else if (c == ')') {
                while (operators.peek() != '(') {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.pop();
            }
            else if (isOperator(c)) {
                while (!operators.empty() && hasPrecedence(c, operators.peek())) {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
            }
        }

        while (!operators.empty()) {
            numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    /**Deals with negativity*/
    private String normalizeUnaryMinus(String expr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (c == '-') {
                //if at start or after '(' or another operator
                if (i == 0 || expr.charAt(i - 1) == '(' || isOperator(expr.charAt(i - 1))) {
                    sb.append("0");
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }


    /**Check if the character is operator*/
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
    /**Check if the character is operator*/
    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) return false;
        return true;
    }

    /**All in one operation application*/
    private double applyOperation(char operator, double b, double a) {
        switch (operator) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("Division by zero");
                return a / b;
        }
        return 0;
    }
}