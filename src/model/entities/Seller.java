package model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Seller implements Serializable{
  private Integer id;
  private String name;
  private String email;
  private Date birthDate;
  private Double baseSalary;
  private Department department;

  public Seller(){
  }

  public Seller(Integer id,String name,String email,Date birthDate,Double baseSalary,Department department){
    this.id = id;
    this.name = name;
    this.email = email;
    this.birthDate = birthDate;
    this.baseSalary = baseSalary;
    this.department = department;
  }

  public Integer getId(){
    return id;
  }

  public String getName(){
    return name;
  }

  public String getEmail(){
    return email;
  }

  public Date getBirthDate(){
    return birthDate;
  }

  public Double getBaseSalary(){
    return baseSalary;
  }

  public Department getDepartment(){
    return department;
  }

  @Override
  public boolean equals(Object o){
    if(this==o) return true;
    if(o==null||getClass()!=o.getClass()) return false;
    var seller = (Seller)o;
    return Objects.equals(getId(),seller.getId());
  }

  @Override
  public int hashCode(){
    return Objects.hash(getId());
  }

  @Override
  public String toString(){
    return "Seller [id="+id+", name="+name+", email="+email+", birthDate="+birthDate+", baseSalary="+baseSalary+", "+
        "department="+department+"]";
  }
}
