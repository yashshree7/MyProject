package mainapp;
import java.sql.*;
import java.util.Scanner;

import connection.databaseConnection;

public class amazoninventoryapp {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nAmazon Inventory Management System");
            System.out.println("1. Add Product");
            System.out.println("2. View All Products");
            System.out.println("3. View Specific Product");
            System.out.println("4. Update Product");
            System.out.println("5. Delete Product");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> addProduct(scanner);
                case 2 -> viewAllProducts();
                case 3 -> viewSpecificProduct(scanner);
                case 4 -> updateProduct(scanner);
                case 5 -> deleteProduct(scanner);
                case 6 -> {
                    running = false;
                    System.out.println("Exiting... Thank you!");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void addProduct(Scanner scanner) throws Exception {
        try (Connection connection = databaseConnection.getConnection()) {
            System.out.print("Enter product name: ");
            String name = scanner.next();
            System.out.print("Enter product price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter product quantity: ");
            int quantity = scanner.nextInt();

            String sql = "INSERT INTO products (name, price, quantity) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setInt(3, quantity);

            int rows = statement.executeUpdate();
            if (rows > 0) {
                System.out.println("Product added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    private static void viewAllProducts() throws Exception {
        try (Connection connection = databaseConnection.getConnection()) {
            String sql = "SELECT * FROM products";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.printf("\n%-5s | %-20s | %-10s | %-10s\n", "ID", "Name", "Price", "Quantity");
            System.out.println("----------------------------------------------------------");

            while (resultSet.next()) {
                System.out.printf("%-5d | %-20s | %-10.2f | %-10d\n",
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("quantity"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving products: " + e.getMessage());
        }
    }


    private static void viewSpecificProduct(Scanner scanner) throws Exception {
        try (Connection connection = databaseConnection.getConnection()) {
            System.out.print("Enter product ID: ");
            int id = scanner.nextInt();

            String sql = "SELECT * FROM products WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("\nProduct Details:");
                System.out.printf("%-15s: %d\n", "ID", resultSet.getInt("id"));
                System.out.printf("%-15s: %s\n", "Name", resultSet.getString("name"));
                System.out.printf("%-15s: %.2f\n", "Price", resultSet.getDouble("price"));
                System.out.printf("%-15s: %d\n", "Quantity", resultSet.getInt("quantity"));
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving product: " + e.getMessage());
        }
    }


    private static void updateProduct(Scanner scanner) throws Exception {
        try (Connection connection = databaseConnection.getConnection()) {
            System.out.print("Enter new product ID: ");
            int id = scanner.nextInt();
            System.out.print("Enter new product name: ");
            String name = scanner.next();
            System.out.print("Enter new product price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter new product quantity: ");
            int quantity = scanner.nextInt();

            String sql = "UPDATE products SET name = ?, price = ?, quantity = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setInt(3, quantity);
            statement.setInt(4, id);

            int rows = statement.executeUpdate();
            if (rows > 0) {
                System.out.println("Product updated successfully!");
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    private static void deleteProduct(Scanner scanner) throws Exception {
        try (Connection connection = databaseConnection.getConnection()) {
            System.out.print("Enter product ID: ");
            int id = scanner.nextInt();

            String sql = "DELETE FROM products WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            int rows = statement.executeUpdate();
            if (rows > 0) {
                System.out.println("Product deleted successfully!");
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }
}

