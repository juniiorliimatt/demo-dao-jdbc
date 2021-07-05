package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.List;

public class Program{
  public static void main(String[] args){
    SellerDao sellerDao = DaoFactory.createSellerDao();

    System.out.println("==== TESTE 1: seller findbyid ====");
    var seller = sellerDao.findById(3);
    System.out.println(seller);

    System.out.println("\n==== TESTE 2: seller findbyDepartment ====");
    var dep = new Department(4, null);
    List<Seller> sellers = sellerDao.findByDepartment(dep);
    sellers.forEach(System.out::println);

    System.out.println("\n==== TESTE 3: seller findAll ====");
    sellers = sellerDao.findAll();
    sellers.forEach(System.out::println);
  }
}
