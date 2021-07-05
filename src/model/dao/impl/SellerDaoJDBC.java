package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao{
  private final Connection connection;

  public SellerDaoJDBC(Connection connection){
    this.connection = connection;
  }

  @Override
  public void insert(Seller seller){
    PreparedStatement st = null;
    try{
      st = connection.prepareStatement("INSERT INTO seller "+
                                           "(Name, Email, BirthDate, BaseSalary, DepartmentId) "+
                                           "VALUES "+
                                           "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

      st.setString(1, seller.getName());
      st.setString(2, seller.getEmail());
      st.setDate(3, new Date(seller.getBirthDate().getTime()));
      st.setDouble(4, seller.getBaseSalary());
      st.setInt(5, seller.getDepartment().getId());

      var rows = st.executeUpdate();

      if(rows>0){
        ResultSet rs = st.getGeneratedKeys();
        if(rs.next()){
          var id = rs.getInt(1);
          seller.setId(id);
        }
        DB.closeResultSet(rs);
      }else{
        throw new DbException("Unexpected error! No rows affected!");
      }
    }catch(SQLException e){
      throw new DbException(e.getMessage());
    }finally{
      closeConnection(st);
    }
  }

  @Override
  public void update(Seller seller){
    PreparedStatement st = null;
    try{
      st = connection
          .prepareStatement("UPDATE seller SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "+
                                "WHERE Id = ?");

      st.setString(1, seller.getName());
      st.setString(2, seller.getEmail());
      st.setDate(3, new Date(seller.getBirthDate().getTime()));
      st.setDouble(4, seller.getBaseSalary());
      st.setInt(5, seller.getDepartment().getId());
      st.setInt(6, seller.getId());

      st.executeUpdate();

    }catch(SQLException e){
      throw new DbException(e.getMessage());
    }finally{
      closeConnection(st);
    }
  }

  @Override
  public void deleteById(Integer id){
    PreparedStatement st = null;
    try{
      st = connection.prepareStatement("DELETE FROM seller WHERE Id = ?");
      st.setInt(1, id);
      st.executeUpdate();
    }catch(SQLException e){
      throw new DbException(e.getMessage());
    }finally{
      closeConnection(st);
    }
  }

  @Override
  public Seller findById(Integer id){
    PreparedStatement st = null;
    ResultSet rs = null;
    try{
      st = connection.prepareStatement("SELECT seller.*,department.Name as DepName FROM seller INNER JOIN department "+
                                           "ON seller.DepartmentId = department.Id WHERE seller.Id = ?");
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

  @Override
  public List<Seller> findByDepartment(Department department){
    PreparedStatement st = null;
    ResultSet rs = null;
    try{
      st = connection.prepareStatement("SELECT seller.*,department.Name as DepName FROM seller INNER JOIN "+
                                           "department ON seller.DepartmentId = department.Id WHERE DepartmentId = ? "+
                                           "ORDER BY Name");
      st.setInt(1, department.getId());
      rs = st.executeQuery();
      List<Seller> sellers = new ArrayList<>();
      Map<Integer,Department> departments = new HashMap<>();
      while(rs.next()){
        var dep = departments.get(rs.getInt("DepartmentId"));
        if(dep==null){
          dep = instantiateDepartment(rs);
          departments.put(rs.getInt("DepartmentId"), dep);
        }

        var seller = instantiateSeller(rs, dep);
        sellers.add(seller);
      }
      return sellers;
    }catch(SQLException e){
      throw new DbException(e.getMessage());
    }finally{
      closeConnections(rs, st);
    }
  }

  @Override
  public List<Seller> findAll(){
    PreparedStatement st = null;
    ResultSet rs = null;
    try{
      st = connection.prepareStatement("SELECT seller.*,department.Name as DepName FROM seller INNER JOIN "+
                                           "department ON seller.DepartmentId = department.Id ORDER BY Name");
      rs = st.executeQuery();

      List<Seller> sellers = new ArrayList<>();
      Map<Integer,Department> departments = new HashMap<>();
      while(rs.next()){
        var dep = departments.get(rs.getInt("DepartmentId"));
        if(dep==null){
          dep = instantiateDepartment(rs);
          departments.put(rs.getInt("DepartmentId"), dep);
        }

        var seller = instantiateSeller(rs, dep);
        sellers.add(seller);
      }
      return sellers;
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

  private void closeConnection(Statement st){
    DB.closeStatement(st);
  }

  private void closeConnections(ResultSet rs, Statement st){
    DB.closeResultSet(rs);
    DB.closeStatement(st);
  }
}
