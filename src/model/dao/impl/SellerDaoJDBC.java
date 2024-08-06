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

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                    "INSERT INTO seller "
                    + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                    + "VALUES "
                    + "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1,seller.getName());
            st.setString(2,seller.getEmail());
            st.setDate(3,new java.sql.Date(seller.getBirthDate().getTime()));
            st.setDouble(4,seller.getBaseSalary());
            st.setInt(5,seller.getDepartment().getId());

            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    seller.setId(id);
                }
                DB.closeResultSet(rs);
            }
            else {
                throw new DbException("Unexpected error! No rows affected!");
            }

        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                    "UPDATE seller "
                    + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                    + "WHERE Id = ? ");


            st.setString(1,seller.getName());
            st.setString(2,seller.getEmail());
            st.setDate(3,new java.sql.Date(seller.getBirthDate().getTime()));
            st.setDouble(4,seller.getBaseSalary());
            st.setInt(5,seller.getDepartment().getId());
            st.setInt(6,seller.getId());

            st.executeUpdate();
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public void deleteById(int id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM seller WHERE Id = ? ");

            st.setInt(1,id);
            st.executeUpdate();

            int rowsAffected = st.executeUpdate();
            if(rowsAffected == 0){
                throw new DbException("Unexpected error! This item does not exist!");
            }
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Seller findById(int id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE seller.Id = ? ");

            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Department department = instantiateDepartament(rs);
                Seller obj = istantiateSeller(rs, department);
                return obj;
            }
            return null;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Seller istantiateSeller(ResultSet rs, Department department) throws SQLException {
        Seller obj = new Seller();
        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Name"));
        obj.setEmail(rs.getString("Email"));
        obj.setBaseSalary(rs.getDouble("BaseSalary"));
        obj.setBirthDate(rs.getDate("BirthDate"));
        obj.setDepartment(department);
        return obj;
    }

    private Department instantiateDepartament(ResultSet rs) throws SQLException {
        Department department = new  Department();
        department.setId(rs.getInt("DepartmentId"));
        department.setName(rs.getString("DepName"));
        return department;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "ORDER BY Name ");

            rs = st.executeQuery();
            List<Seller> sellerList = new ArrayList<>();
            Map<Integer,Department> departmentMap = new HashMap<>();

            while (rs.next()) {
                Department dep = departmentMap.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartament(rs);
                    departmentMap.put(rs.getInt("DepartmentId"), dep);
                }

                Seller obj = istantiateSeller(rs, dep);
                sellerList.add(obj);
            }
            return sellerList;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                   "SELECT seller.*,department.Name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE DepartmentId = ? "
                    + "ORDER BY Name ");

            st.setInt(1, department.getId());
            rs = st.executeQuery();
            List<Seller> sellerList = new ArrayList<>();
            Map<Integer,Department> departmentMap = new HashMap<>();

            while (rs.next()) {
                Department dep = departmentMap.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartament(rs);
                    departmentMap.put(rs.getInt("DepartmentId"), dep);
                }

                Seller obj = istantiateSeller(rs, dep);
                sellerList.add(obj);
            }
            return sellerList;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
