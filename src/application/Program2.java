package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;
import model.entities.Seller;

public class Program2 {
    public static void main(String[] args) {

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("=== Test 1 department insert === ");
        Department department = new Department(null,"Music");
        departmentDao.insert(department);
        System.out.println("DEPARTMENT INSERTED");

        System.out.println();
        System.out.println("\n === Test 1 department insert === ");
    }
}
