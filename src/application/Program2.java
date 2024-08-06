package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.List;


public class Program2 {
    public static void main(String[] args) {

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
        Department department = new Department();

//        System.out.println("=== Test 1 department insert === ");
//        Department department = new Department(null,"Music");
//        departmentDao.insert(department);
//        System.out.println("DEPARTMENT INSERTED");

        System.out.println();
        System.out.println("\n === Test 2 department findById=== ");
        Department deparmentFindById = departmentDao.findById(1);
        System.out.println(deparmentFindById.toString());

        System.out.println();
        System.out.println("\n === Test 3 department findAll=== ");
        List<Department> departmentFindAll = departmentDao.findAll();
        departmentFindAll.forEach(System.out::println);
    }
}
