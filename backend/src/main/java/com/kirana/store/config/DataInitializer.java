package com.kirana.store.config;

import com.kirana.store.entity.*;
import com.kirana.store.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, CategoryRepository categoryRepository, ProductRepository productRepository, CustomerRepository customerRepository, SupplierRepository supplierRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.supplierRepository = supplierRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        Map<String, Category> categoryMap = seedCategories();
        seedProducts(categoryMap);
        seedCustomers();
        seedSuppliers();
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            logger.info("Seeding initial store users...");

            User admin = User.builder()
                    .username("admin")
                    .passwordHash(passwordEncoder.encode("Admin@1234"))
                    .fullName("Mihir Patel")
                    .email("admin@kiranastore.com")
                    .role(Role.ROLE_ADMIN)
                    .enabled(true)
                    .accountLocked(false)
                    .failedLoginAttempts(0)
                    .mustChangePassword(false)
                    .build();

            User cashier = User.builder()
                    .username("cashier")
                    .passwordHash(passwordEncoder.encode("Cashier@1234"))
                    .fullName("Kirana Cashier")
                    .email("cashier@kiranastore.com")
                    .role(Role.ROLE_CASHIER)
                    .enabled(true)
                    .accountLocked(false)
                    .failedLoginAttempts(0)
                    .mustChangePassword(false)
                    .build();

            userRepository.saveAll(Arrays.asList(admin, cashier));
            logger.info("Default users created successfully. [Admin: admin / Admin@1234, Cashier: cashier / Cashier@1234]");
        } else {
            userRepository.findByUsername("admin").ifPresent(admin -> {
                if (!"Mihir Patel".equals(admin.getFullName())) {
                    admin.setFullName("Mihir Patel");
                    userRepository.save(admin);
                }
            });
        }
    }

    private Map<String, Category> seedCategories() {
        Map<String, Category> categoryMap = new HashMap<>();

        String[][] categories = {
                {"Groceries & Staples", "Essential rice, pulses, flour, wheat, grains"},
                {"Dairy & Bakery", "Fresh milk, curd, butter, paneer, bread, cheese"},
                {"Beverages", "Tea, coffee, fruit juices, cold drinks, health drinks"},
                {"Snacks & Biscuits", "Namkeen, biscuits, chips, chocolates, instant noodles"},
                {"Personal Care", "Soaps, shampoos, toothpaste, creams, hair oil"},
                {"Household Care", "Detergents, dishwashing, cleaners, mosquito repellents"},
                {"Spices & Masala", "Turmeric, chili powder, coriander, garam masala, salt"},
                {"Edible Oils & Ghee", "Mustard oil, sunflower oil, groundnut oil, cow ghee"}
        };

        for (String[] cat : categories) {
            Optional<Category> existing = categoryRepository.findByName(cat[0]);
            Category category = existing.orElseGet(() -> categoryRepository.save(
                    Category.builder().name(cat[0]).description(cat[1]).build()
            ));
            categoryMap.put(cat[0], category);
        }

        return categoryMap;
    }

    private void seedProducts(Map<String, Category> categoryMap) {
        if (productRepository.count() < 100) {
            logger.info("Seeding Kirana inventory product dataset...");

            List<Product> seedList = new ArrayList<>();

            Object[][] productTemplates = {
                    {"Fortune Chakki Fresh Atta 5kg", "1001", "Groceries & Staples", 210.00, 245.00, 260.00, 0.00, "packet", "10060010", 45, 10},
                    {"Aashirvaad Shudh Chakki Atta 10kg", "1002", "Groceries & Staples", 410.00, 475.00, 499.00, 0.00, "packet", "10060010", 30, 8},
                    {"India Gate Basmati Rice Feast Rozzana 5kg", "1003", "Groceries & Staples", 340.00, 395.00, 425.00, 0.00, "packet", "10063010", 25, 5},
                    {"Tata Sampann Toor Dal 1kg", "1004", "Groceries & Staples", 135.00, 158.00, 170.00, 0.00, "packet", "07130010", 50, 10},
                    {"Tata Sampann Moong Dal Chilka 1kg", "1005", "Groceries & Staples", 115.00, 136.00, 145.00, 0.00, "packet", "07130020", 40, 8},
                    {"Tata Sampann Chana Dal 1kg", "1006", "Groceries & Staples", 78.00, 92.00, 100.00, 0.00, "packet", "07130030", 35, 10},
                    {"Fortune Basmati Rice Mogra 5kg", "1007", "Groceries & Staples", 280.00, 325.00, 350.00, 0.00, "packet", "10063010", 20, 5},
                    {"Rajma Chitra Organic 1kg", "1008", "Groceries & Staples", 125.00, 148.00, 160.00, 0.00, "packet", "07133300", 30, 5},
                    {"Kabuli Chana Large 1kg", "1009", "Groceries & Staples", 110.00, 130.00, 140.00, 0.00, "packet", "07132000", 25, 5},
                    {"Poha Thick (Flattened Rice) 1kg", "1010", "Groceries & Staples", 42.00, 52.00, 60.00, 0.00, "packet", "11041900", 60, 15},
                    {"Amul Taaza Toned Milk 500ml", "1011", "Dairy & Bakery", 26.00, 27.00, 27.00, 0.00, "packet", "04011000", 80, 20},
                    {"Amul Gold Full Cream Milk 500ml", "1012", "Dairy & Bakery", 31.00, 33.00, 33.00, 0.00, "packet", "04012000", 90, 25},
                    {"Amul Masti Dahi 400g Pouch", "1013", "Dairy & Bakery", 32.00, 35.00, 35.00, 0.00, "packet", "04031000", 40, 10},
                    {"Amul Butter Pasteurized 100g", "1014", "Dairy & Bakery", 52.00, 58.00, 58.00, 12.00, "pcs", "04051000", 35, 10},
                    {"Amul Malai Paneer Block 200g", "1015", "Dairy & Bakery", 80.00, 95.00, 95.00, 0.00, "pcs", "04061000", 20, 5},
                    {"Britannia Brown Bread 400g", "1016", "Dairy & Bakery", 40.00, 50.00, 50.00, 0.00, "packet", "19059010", 30, 8},
                    {"Amul Cheese Slices 200g (10 Slices)", "1017", "Dairy & Bakery", 125.00, 145.00, 150.00, 12.00, "packet", "04063000", 25, 5},
                    {"Red Label Tea 500g", "1018", "Beverages", 230.00, 270.00, 290.00, 5.00, "packet", "09023010", 40, 10},
                    {"Taj Mahal Tea 250g", "1019", "Beverages", 145.00, 175.00, 190.00, 5.00, "packet", "09023020", 30, 8},
                    {"Nescafe Classic Instant Coffee 50g Glass Jar", "1020", "Beverages", 155.00, 185.00, 195.00, 5.00, "pcs", "21011110", 25, 5},
                    {"Bru Instant Coffee 100g Pouch", "1021", "Beverages", 160.00, 190.00, 205.00, 5.00, "packet", "21011120", 30, 6},
                    {"Coca-Cola Original Taste 1.25L Bottle", "1022", "Beverages", 50.00, 65.00, 65.00, 28.00, "bottle", "22021010", 50, 12},
                    {"Sprite Lemon Lime Drink 750ml", "1023", "Beverages", 33.00, 40.00, 40.00, 28.00, "bottle", "22021020", 45, 10},
                    {"Real Fruit Power Juice Mango 1L", "1024", "Beverages", 88.00, 110.00, 120.00, 12.00, "packet", "20098900", 20, 5},
                    {"Parle-G Gold Biscuits 1kg Pack", "1025", "Snacks & Biscuits", 110.00, 135.00, 140.00, 18.00, "packet", "19053100", 40, 10},
                    {"Britannia Good Day Cashew Biscuits 600g", "1026", "Snacks & Biscuits", 115.00, 140.00, 150.00, 18.00, "packet", "19053100", 35, 10},
                    {"Maggi 2-Minute Masala Noodles 420g (6 Pack)", "1027", "Snacks & Biscuits", 76.00, 88.00, 96.00, 12.00, "packet", "19023010", 60, 15},
                    {"Lays Classic Salted Potato Chips 50g", "1028", "Snacks & Biscuits", 16.00, 20.00, 20.00, 12.00, "packet", "20052000", 100, 20},
                    {"Kurkure Masala Munch 85g", "1029", "Snacks & Biscuits", 16.00, 20.00, 20.00, 12.00, "packet", "20059900", 100, 20},
                    {"Haldiram Bhujia Sev 400g", "1030", "Snacks & Biscuits", 95.00, 115.00, 125.00, 12.00, "packet", "21069099", 30, 8},
                    {"Cadbury Dairy Milk Silk Chocolate 150g", "1031", "Snacks & Biscuits", 145.00, 175.00, 175.00, 18.00, "pcs", "18063200", 25, 5},
                    {"Dettol Original Bathing Soap 125g (Pack of 4)", "1032", "Personal Care", 165.00, 198.00, 210.00, 18.00, "packet", "34011110", 30, 8},
                    {"Dove Cream Beauty Bathing Bar 100g (Pack of 3)", "1033", "Personal Care", 140.00, 170.00, 180.00, 18.00, "packet", "34011120", 25, 5},
                    {"Colgate Strong Teeth Toothpaste 500g Combo", "1034", "Personal Care", 195.00, 235.00, 255.00, 18.00, "packet", "33061020", 40, 10},
                    {"Clinic Plus Strong & Long Shampoo 650ml", "1035", "Personal Care", 290.00, 360.00, 399.00, 18.00, "bottle", "33051090", 20, 5},
                    {"Parachute 100% Pure Coconut Oil 500ml", "1036", "Personal Care", 175.00, 205.00, 220.00, 5.00, "bottle", "33059010", 35, 8},
                    {"Surf Excel Easy Wash Detergent Powder 1kg", "1037", "Household Care", 118.00, 140.00, 150.00, 18.00, "packet", "34022010", 50, 12},
                    {"Vim Dishwash Liquid Gel Lemon 750ml", "1038", "Household Care", 145.00, 178.00, 190.00, 18.00, "bottle", "34022020", 30, 8},
                    {"Harpic Power Plus Toilet Cleaner 1L", "1039", "Household Care", 170.00, 205.00, 220.00, 18.00, "bottle", "34029090", 25, 5},
                    {"Tata Salt Vacuum Evaporated Iodised 1kg", "1040", "Spices & Masala", 23.00, 28.00, 28.00, 0.00, "packet", "25010010", 120, 30},
                    {"Everest Turmeric Powder 200g", "1041", "Spices & Masala", 42.00, 52.00, 58.00, 5.00, "packet", "09103020", 50, 12},
                    {"MDH Red Chilli Powder 200g", "1042", "Spices & Masala", 72.00, 88.00, 95.00, 5.00, "packet", "09042210", 45, 10},
                    {"Everest Garam Masala 100g", "1043", "Spices & Masala", 70.00, 85.00, 92.00, 5.00, "packet", "09109100", 30, 8},
                    {"Fortune Sunlite Refined Sunflower Oil 1L Pouch", "1044", "Edible Oils & Ghee", 115.00, 132.00, 145.00, 5.00, "packet", "15121910", 60, 15},
                    {"Amul Pure Cow Ghee 1L Tin", "1045", "Edible Oils & Ghee", 550.00, 625.00, 650.00, 12.00, "pcs", "04059020", 25, 5},
                    {"Fortune Kachi Ghani Mustard Oil 1L Bottle", "1046", "Edible Oils & Ghee", 125.00, 145.00, 160.00, 5.00, "bottle", "15149120", 40, 10}
            };

            for (Object[] t : productTemplates) {
                String name = (String) t[0];
                Category cat = categoryMap.get((String) t[2]);
                BigDecimal cost = BigDecimal.valueOf((Double) t[3]).setScale(2, RoundingMode.HALF_UP);
                BigDecimal selling = BigDecimal.valueOf((Double) t[4]).setScale(2, RoundingMode.HALF_UP);
                BigDecimal mrp = BigDecimal.valueOf((Double) t[5]).setScale(2, RoundingMode.HALF_UP);
                BigDecimal gst = BigDecimal.valueOf((Double) t[6]).setScale(2, RoundingMode.HALF_UP);
                String unit = (String) t[7];
                String hsn = (String) t[8];
                int stock = (Integer) t[9];
                int minAlert = (Integer) t[10];

                Product p = Product.builder()
                        .name(name)
                        .category(cat)
                        .costPrice(cost)
                        .sellingPrice(selling)
                        .mrp(mrp)
                        .gstRate(gst)
                        .unit(unit)
                        .hsnCode(hsn)
                        .stockQuantity(stock)
                        .minStockAlert(minAlert)
                        .active(true)
                        .build();

                seedList.add(p);
            }

            List<Category> allCatList = new ArrayList<>(categoryMap.values());
            Random random = new Random(42);

            String[] brandPrefixes = {"Tata", "Fortune", "Nestle", "Britannia", "Parle", "Amul", "Dabur", "Bikaji", "Everest", "Catch", "Godrej", "Patanjali"};
            String[] itemTypes = {"Biscuits", "Namkeen", "Wafers", "Chocolates", "Soap", "Shampoo", "Detergent", "Pulses", "Rice", "Spices", "Oil", "Tea", "Juice"};
            String[] sizes = {"100g", "250g", "500g", "1kg", "2kg", "5kg", "200ml", "500ml", "1L"};

            long currentCount = seedList.size();
            for (int i = (int) currentCount + 1; i <= 1050; i++) {
                String brand = brandPrefixes[random.nextInt(brandPrefixes.length)];
                String type = itemTypes[random.nextInt(itemTypes.length)];
                String size = sizes[random.nextInt(sizes.length)];
                String pName = brand + " Premium " + type + " (" + size + ")";

                Category category = allCatList.get(random.nextInt(allCatList.size()));

                double cost = 20.0 + (random.nextDouble() * 250.0);
                double selling = cost * (1.12 + (random.nextDouble() * 0.15));
                double mrp = selling * (1.05 + (random.nextDouble() * 0.10));
                double[] gstRates = {0.0, 5.0, 12.0, 18.0};
                double gst = gstRates[random.nextInt(gstRates.length)];

                int stock = 5 + random.nextInt(120);
                int alert = 5 + random.nextInt(10);

                Product extraP = Product.builder()
                        .name(pName)
                        .category(category)
                        .costPrice(BigDecimal.valueOf(cost).setScale(2, RoundingMode.HALF_UP))
                        .sellingPrice(BigDecimal.valueOf(selling).setScale(2, RoundingMode.HALF_UP))
                        .mrp(BigDecimal.valueOf(mrp).setScale(2, RoundingMode.HALF_UP))
                        .gstRate(BigDecimal.valueOf(gst).setScale(2, RoundingMode.HALF_UP))
                        .unit(size.contains("L") || size.contains("ml") ? "bottle" : "packet")
                        .hsnCode("2106" + String.format("%04d", i % 1000))
                        .stockQuantity(stock)
                        .minStockAlert(alert)
                        .active(true)
                        .build();

                seedList.add(extraP);
            }

            productRepository.saveAll(seedList);
            logger.info("Successfully seeded {} Kirana inventory products into database!", seedList.size());
        }
    }

    private void seedCustomers() {
        if (customerRepository.count() == 0) {
            logger.info("Seeding initial customers...");

            Customer c1 = Customer.builder()
                    .name("Ramesh Patel")
                    .phone("9825012345")
                    .email("ramesh.patel@gmail.com")
                    .address("101, Shanti Niketan Society, Sector 12")
                    .totalPurchases(BigDecimal.valueOf(4500.00))
                    .creditBalance(BigDecimal.valueOf(350.00))
                    .build();

            Customer c2 = Customer.builder()
                    .name("Priya Sharma")
                    .phone("9898012345")
                    .email("priya.sharma@yahoo.com")
                    .address("402, Green Avenue, Main Road")
                    .totalPurchases(BigDecimal.valueOf(2800.00))
                    .creditBalance(BigDecimal.ZERO)
                    .build();

            Customer c3 = Customer.builder()
                    .name("Sanjay Gupta")
                    .phone("9712098765")
                    .email("sanjay.gupta@outlook.com")
                    .address("12/B, Kirana Bazaar Society")
                    .totalPurchases(BigDecimal.valueOf(8900.00))
                    .creditBalance(BigDecimal.valueOf(1250.00))
                    .build();

            customerRepository.saveAll(Arrays.asList(c1, c2, c3));
        }
    }

    private void seedSuppliers() {
        if (supplierRepository.count() == 0) {
            logger.info("Seeding initial suppliers...");

            Supplier s1 = Supplier.builder()
                    .name("Gujarat Wholesale Grocery Distributors")
                    .contactPerson("Maheshbhai Shah")
                    .phone("9824054321")
                    .email("orders@gujaratwholesale.com")
                    .gstNumber("24AABCG1234F1Z1")
                    .address("APMC Market, Gate No 3, City")
                    .balanceAmount(BigDecimal.valueOf(15400.00))
                    .build();

            Supplier s2 = Supplier.builder()
                    .name("Amul Dairy Sales Agency")
                    .contactPerson("Vikram Singh")
                    .phone("9879011223")
                    .email("supply@amuldairy.org")
                    .gstNumber("24AAACA5678G2Z5")
                    .address("Dairy Circle, Industrial Area")
                    .balanceAmount(BigDecimal.valueOf(3200.00))
                    .build();

            supplierRepository.saveAll(Arrays.asList(s1, s2));
        }
    }
}
