package application;

import model.entities.Department;

public class Program{
  public static void main(String[] args){
    var dep = new Department(1,"Books");
    System.out.println(dep);
  }
}
