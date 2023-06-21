package Volopay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public int getTotalItemsSoldByDepartment(Date startDate, Date endDate, String department) {
        List<Purchase> purchases = purchaseRepository.findByDepartmentAndPurchaseDateBetween(department, startDate, endDate);
        int totalItemsSold = 0;
        for (Purchase purchase : purchases) {
            totalItemsSold += purchase.getQuantity();
        }
        return totalItemsSold;
    }
    
    public String getNthMostSoldItemByQuantity(Date startDate, Date endDate, int n) {
        List<Purchase> purchases = purchaseRepository.findByPurchaseDateBetween(startDate, endDate);
        purchases.sort((p1, p2) -> p2.getQuantity() - p1.getQuantity());

        if (n >= 1 && n <= purchases.size()) {
            return purchases.get(n - 1).getItem();
        } else {
            throw new IllegalArgumentException("Invalid value for 'n'");
        }
    }

    public String getNthMostSoldItemByPrice(Date startDate, Date endDate, int n) {
        List<Purchase> purchases = purchaseRepository.findByPurchaseDateBetween(startDate, endDate);
        purchases.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));

        if (n >= 1 && n <= purchases.size()) {
            return purchases.get(n - 1).getItem();
        } else {
            throw new IllegalArgumentException("Invalid value for 'n'");
        }
    }
    

    public List<Double> getMonthlySalesByProduct(String product, int year) {
        List<Double> monthlySales = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0, 0.0, 0.0, 0.0, 0.0, 0.0));

        List<Purchase> purchases = purchaseRepository.findByDepartmentAndPurchaseDateBetween(product,
                getStartDate(year), getEndDate(year));

        for (Purchase purchase : purchases) {
            int month = purchase.getPurchaseDate().getMonth(); // Month is zero-based
            double totalPrice = purchase.getPrice() * purchase.getQuantity();
            monthlySales.set(month, monthlySales.get(month) + totalPrice);
        }

        return monthlySales;
    }

    private Date getStartDate(int year) {
        // Assuming the start date is the first day of the year
        return new Date(year - 1900, 0, 1);
    }

    private Date getEndDate(int year) {
        // Assuming the end date is the last day of the year
        return new Date(year - 1900, 11, 31);
    }
    
    public Map<String, Double> getPercentageOfSoldItemsByDepartment(Date startDate, Date endDate) {
        List<Purchase> purchases = purchaseRepository.findByPurchaseDateBetween(startDate, endDate);

        // Calculate the total quantity sold for each department
        Map<String, Integer> departmentQuantityMap = new HashMap<>();
        for (Purchase purchase : purchases) {
            String department = purchase.getDepartment();
            int quantity = purchase.getQuantity();
            departmentQuantityMap.put(department, departmentQuantityMap.getOrDefault(department, 0) + quantity);
        }

        // Calculate the percentage of sold items by department
        Map<String, Double> departmentPercentageMap = new HashMap<>();
        int totalQuantitySold = purchases.stream().mapToInt(Purchase::getQuantity).sum();
        for (Map.Entry<String, Integer> entry : departmentQuantityMap.entrySet()) {
            String department = entry.getKey();
            int quantity = entry.getValue();
            double percentage = (quantity * 100.0) / totalQuantitySold;
            departmentPercentageMap.put(department, percentage);
        }

        return departmentPercentageMap;
    }
    
    

    // Implement other methods for remaining APIs
}

