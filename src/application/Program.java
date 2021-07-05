package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;

public class Program{
  public static void main(String[] args){

    SellerDao sellerDao = DaoFactory.createSellerDao();
    var seller = sellerDao.findById(18);
    System.out.println(seller);
  }
}
