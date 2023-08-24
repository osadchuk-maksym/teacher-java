package com.jk.teacher.java;

import org.mariuszgromada.math.mxparser.Expression;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MathEquationValidator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введіть математичне рівняння:");
        String equation = scanner.nextLine();

        if (isValidEquation(equation)) {
            System.out.println("Рівняння коректне.");
            int numberCount = countNumbersInEquation(equation);
            System.out.println("Кількість чисел у рівнянні: " + numberCount);
            saveValidEquationToDatabase(equation);
        } else {
            System.out.println("Рівняння некоректне.");
        }
    }

    public static boolean isValidEquation(String equation) {
        if (!equation.contains("=")) {
            return false; // Рівняння не містить =
        }

        String[] sides = equation.split("=");
        if (sides.length != 2) {
            return false; // Рівняння має більше одного =
        }

        Expression expression = new Expression(sides[1]);

        if (!expression.checkSyntax() || !sides[0].contains("x")) {
            return false; // Вираз містить недопустимий вираз або відсутній x
        }

        return true;
    }
    public static int countNumbersInEquation(String equation) {
        int count = 0;
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(equation);

        while (matcher.find()) {
            count++;
        }

        return count;
    }
    public static void saveValidEquationToDatabase(String equation) {
        String url = "jdbc:mysql://localhost:3306/exemple";
        String user = "root";
        String password = "5623";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String insertQuery = "INSERT INTO equations (equation) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, equation);
                preparedStatement.executeUpdate();
                System.out.println("Рівняння збережено до бази даних.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Помилка при збереженні до бази даних.");
        }
    }
}