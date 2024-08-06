package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class Program {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== Test 1: seller findById ===");
        Seller seller = sellerDao.findById(3);
        System.out.printf(seller.toString());

        System.out.println();
        System.out.println("\n === Test 2: seller findByDepartment ===");
        Department department = new Department(2, null);

        List<Seller> list = sellerDao.findByDepartment(department);

        list.forEach(System.out::println);

        System.out.println();
        System.out.println("\n === Test 3: seller findAll ===");
        list = sellerDao.findAll();

        list.forEach(System.out::println);


        System.out.println();
        System.out.println("\n === Test 4: seller insert ===");
        Seller newSeller = new Seller(null,"Samuel","samuel@gmail.com",new Date(),4000.0,department);
        sellerDao.insert(newSeller);
        System.out.println("Iserted successfully, New id: " + newSeller.getId());


        System.out.println();
        System.out.println("\n === Test 5: seller update ===");
        seller = sellerDao.findById(1);
        seller.setName("Marta Wine");
        sellerDao.update(seller);
        System.out.println("Updated successfully");

        System.out.println();
        System.out.println("\n === Test 6: seller delete ===");
        System.out.println("Enter id for delete test: ");
        int id = scanner.nextInt();

        sellerDao.deleteById(id);

        System.out.println("Deleted successfully");
    }
}
