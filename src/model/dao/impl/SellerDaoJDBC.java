package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.List;

public class SellerDaoJDBC implements SellerDao{
  private final String findById = "SELECT seller.*,department.Name as DepName FROM seller INNER JOIN department "+
      "ON seller.DepartmentId = department.Id WHERE seller.Id = ?";

  private Connection connection;

  public SellerDaoJDBC(Connection connection){
    this.connection = connection;
  }

  @Override
  public void insert(Seller seller){

  }

  @Override
  public void update(Seller seller){

  }

  @Override
  public void deleteById(Integer id){

  }

  @Override
  public Seller findById(Integer id){
    PreparedStatement st = null;
    ResultSet rs = null;
    try{
      st = connection.prepareStatement(findById);
      st.setInt(1, id);
      rs = st.executeQuery();
      if(rs.next()){
        return instantiateSeller(rs, instantiateDepartment(rs));
      }
      return null;
    }catch(SQLException e){
      throw new DbException(e.getMessage());
    }finally{
      closeConnections(rs, st);
    }
  }

  private Department instantiateDepartment(ResultSet rs) throws SQLException{
    var department = new Department();
    department.setId(rs.getInt("DepartmentId"));
    department.setName(rs.getString("DepName"));
    return department;
  }

  private Seller instantiateSeller(ResultSet rs, Department department) throws SQLException{
    var seller = new Seller();
    seller.setId(rs.getInt("Id"));
    seller.setName(rs.getString("Name"));
    seller.setEmail(rs.getString("Email"));
    seller.setBirthDate(rs.getDate("BirthDate"));
    seller.setBaseSalary(rs.getDouble("BaseSalary"));
    seller.setDepartment(department);
    return seller;
  }

  private void closeConnections(ResultSet rs, Statement st){
    DB.closeResultSet(rs);
    DB.closeStatement(st);
  }

  @Override
  public List<Seller> findAll(){
    return null;
  }

}
