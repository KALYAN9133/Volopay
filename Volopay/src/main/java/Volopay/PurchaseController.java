package Volopay;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PurchaseController {
    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/total_items")
    public ResponseEntity<Integer> getTotalItemsSold(
            @RequestParam("start_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("end_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam("department") String department
    ) {
        int totalItemsSold = purchaseService.getTotalItemsSoldByDepartment(startDate, endDate, department);
        return new ResponseEntity<>(totalItemsSold, HttpStatus.OK);
    }
    
    @GetMapping("/monthly_sales")
    public ResponseEntity<List<Double>> getMonthlySalesByProduct(
            @RequestParam("product") String product,
            @RequestParam("year") int year
    ) {
        List<Double> monthlySales = purchaseService.getMonthlySalesByProduct(product, year);
        return new ResponseEntity<>(monthlySales, HttpStatus.OK);
    }
    
    @GetMapping("/percentage_of_department_wise_sold_items")
    public ResponseEntity<Map<String, Double>> getPercentageOfSoldItemsByDepartment(
            @RequestParam("start_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("end_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
    ) {
        Map<String, Double> departmentPercentageMap = purchaseService.getPercentageOfSoldItemsByDepartment(startDate, endDate);
        return new ResponseEntity<>(departmentPercentageMap, HttpStatus.OK);
    }
    
    @GetMapping("/nth_most_total_item")
    public ResponseEntity<String> getNthMostSoldItem(
            @RequestParam("item_by") String itemBy,
            @RequestParam("start_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("end_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam("n") int n
    ) {
        String itemName;
        if (itemBy.equalsIgnoreCase("quantity")) {
            itemName = purchaseService.getNthMostSoldItemByQuantity(startDate, endDate, n);
        } else if (itemBy.equalsIgnoreCase("price")) {
            itemName = purchaseService.getNthMostSoldItemByPrice(startDate, endDate, n);
        } else {
            throw new IllegalArgumentException("Invalid value for 'item_by'");
        }

        return new ResponseEntity<>(itemName, HttpStatus.OK);
    }

    // Implement other API endpoints for remaining APIs
}

