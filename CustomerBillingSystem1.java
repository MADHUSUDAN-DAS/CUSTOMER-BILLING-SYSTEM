import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

class Product implements Serializable {
    int productNo;
    String productName;
    int quantity;
    int price;

    public Product(int productNo, String productName, int quantity, int price) {
        this.productNo = productNo;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }
}

public class CustomerBillingSystem1 extends JFrame implements ActionListener {
    private ArrayList<Product> products = new ArrayList<>();
    private JTextArea displayArea;
    private JTextField productNoField, productNameField, quantityField, priceField, searchField;
    private JButton addButton, deleteButton, displayButton, invoiceButton, saveButton, loadButton, searchButton;

    public CustomerBillingSystem1() {
        setTitle("Customer Billing System");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        getContentPane().setBackground(Color.LIGHT_GRAY); // Enhanced UI

        displayArea = new JTextArea(15, 50);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font style
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane);

        productNoField = new JTextField(10);
        productNameField = new JTextField(10);
        quantityField = new JTextField(10);
        priceField = new JTextField(10);
        searchField = new JTextField(10);

        add(new JLabel("Product No:"));
        add(productNoField);
        add(new JLabel("Product Name:"));
        add(productNameField);
        add(new JLabel("Quantity:"));
        add(quantityField);
        add(new JLabel("Price:"));
        add(priceField);
        add(new JLabel("Search Product:"));
        add(searchField);

        addButton = new JButton("Add Product");
        deleteButton = new JButton("Delete Product");
        displayButton = new JButton("Display Products");
        invoiceButton = new JButton("Generate Invoice");
        saveButton = new JButton("Save Records");
        loadButton = new JButton("Load Records");
        searchButton = new JButton("Search Product");

        addButton.addActionListener(this);
        deleteButton.addActionListener(this);
        displayButton.addActionListener(this);
        invoiceButton.addActionListener(this);
        saveButton.addActionListener(this);
        loadButton.addActionListener(this);
        searchButton.addActionListener(this);

        add(addButton);
        add(deleteButton);
        add(displayButton);
        add(invoiceButton);
        add(saveButton);
        add(loadButton);
        add(searchButton);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addProduct();
        } else if (e.getSource() == deleteButton) {
            deleteProduct();
        } else if (e.getSource() == displayButton) {
            displayProducts();
        } else if (e.getSource() == invoiceButton) {
            generateInvoice();
        } else if (e.getSource() == saveButton) {
            saveRecords();
        } else if (e.getSource() == loadButton) {
            loadRecords();
        } else if (e.getSource() == searchButton) {
            searchProduct();
        }
    }

    private void addProduct() {
        try {
            int productNo = Integer.parseInt(productNoField.getText());
            String productName = productNameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            int price = Integer.parseInt(priceField.getText());

            // Data validation for positive values
            if (quantity <= 0 || price <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity and Price must be positive values.");
                return;
            }

            products.add(new Product(productNo, productName, quantity, price));
            displayArea.append("Product Added: " + productName + "\n");
            clearFields(); // Clear fields after adding
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for Product No, Quantity, and Price.");
        }
    }

    private void deleteProduct() {
        try {
            int productNo = Integer.parseInt(productNoField.getText());
            products.removeIf(product -> product.productNo == productNo);
            displayArea.append("Product Deleted: " + productNo + "\n");
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Product No.");
        }
    }

    private void displayProducts() {
        displayArea.setText("");
        for (Product product : products) {
            displayArea.append("Product No: " + product.productNo + ", Name: " + product.productName +
                    ", Quantity: " + product.quantity + ", Price: " + product.price + "\n");
        }
    }

    private void generateInvoice() {
        StringBuilder invoice = new StringBuilder("Invoice:\n");
        int totalAmount = 0;
        for (Product product : products) {
            invoice.append("Product No: ").append(product.productNo)
                    .append(", Name: ").append(product.productName)
                    .append(", Quantity: ").append(product.quantity)
                    .append(", Price: ").append(product.price)
                    .append(", Amount: ").append(product.quantity * product.price).append("\n");
            totalAmount += product.quantity * product.price;
        }
        invoice.append("Total Amount: ").append(totalAmount).append("\n");
        displayArea.setText(invoice.toString());
    }

    private void saveRecords() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("products.dat"))) {
            oos.writeObject(products);
            JOptionPane.showMessageDialog(this, "Records saved successfully.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving records: " + ex.getMessage());
        }
    }

    private void loadRecords() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("products.dat"))) {
            products = (ArrayList<Product>) ois.readObject();
            JOptionPane.showMessageDialog(this, "Records loaded successfully.");
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error loading records: " + ex.getMessage());
        }
    }

    private void searchProduct() {
        String searchTerm = searchField.getText().toLowerCase();
        displayArea.setText("");
        boolean found = false;

        for (Product product : products) {
            if (product.productName.toLowerCase().contains(searchTerm) || 
                String.valueOf(product.productNo).equals(searchTerm)) {
                displayArea.append("Product No: " + product.productNo + ", Name: " + product.productName +
                        ", Quantity: " + product.quantity + ", Price: " + product.price + "\n");
                found = true;
            }
        }

        if (!found) {
            displayArea.append("No products found matching: " + searchTerm + "\n");
        }
    }

    private void clearFields() {
        productNoField.setText("");
        productNameField.setText("");
        quantityField.setText("");
        priceField.setText("");
        searchField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CustomerBillingSystem frame = new CustomerBillingSystem();
            frame.setVisible(true);
        });
    }
}