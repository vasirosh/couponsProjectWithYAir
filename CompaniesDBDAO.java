package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.mysql.cj.exceptions.RSAException;

import beans.Company;
import utils.ConnectionPool;
import utils.CouponSystemException;

public class CompaniesDBDAO implements CompaniesDAO{

	@Override
	public boolean isCompanyExists(String email, String password) {
		boolean isExist = false;
		String sql = "SELECT * FROM companies WHERE EMAIL ="+ email+"AND PASSWORD ="+password;
		Connection con = ConnectionPool.getInstance().getConnection();
        try {
			Statement stmt = con.createStatement();
		     ResultSet rs = stmt.executeQuery(sql);
		     isExist = rs.next();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally {
			
		ConnectionPool.getInstance().returnConnection(con);
			
		}
        
        return isExist;
        
      
	}

	
	public int addCompany(Company company) throws CouponSystemException {
	
		String sql = "insert into companies(NAME,EMAIL,PASSWORD) values('"+company.getName()+"','"+company.getEmail()+"','"+company.getPassword()+"')";                      
		Connection con = ConnectionPool.getInstance().getConnection();
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
			System.out.println("company created");
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    throw new CouponSystemException("addCompany is failed!",e);
			
		}finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		
	}


	@Override
	public void updateCompany(Company company) throws CouponSystemException  {
	     	
		String sql = "UPDATE companies SET NAME =? , EMAIL =? , PASSWORD = ? WHERE ID = ?";
		Connection con = ConnectionPool.getInstance().getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, company.getName());
			pstmt.setString(2, company.getEmail());
			pstmt.setString(3, company.getPassword());
			pstmt.setInt(4, company.getId());
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CouponSystemException("update is failed",e);
		}finally {
			
			ConnectionPool.getInstance().returnConnection(con);
		}
				
	}

	public void delateCompany(int companyID) throws CouponSystemException {
		
		String sql = "DELETE FROM companies WHERE ID=" + companyID ;
		Connection con = ConnectionPool.getInstance().getConnection();
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			System.out.println("company deleted from db");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new CouponSystemException("deleteCompany failed!",e);
		}
		finally {
			ConnectionPool.getInstance().returnConnection(con);
		
		}
	}
	

	@Override
	public ArrayList<Company> getAllCompanies() throws CouponSystemException {
		
		
		String sql = "SELECT * FROM companies";
		ArrayList<Company> companyAL = new ArrayList<Company>();
		Connection con = ConnectionPool.getInstance().getConnection();
		try(Statement stmt = con.createStatement();){
			
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				companyAL.add(new Company(rs.getInt(0), rs.getString(1), rs.getString(2), rs.getString(3)));
			}
			return companyAL;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new CouponSystemException("getAllcompanies failed!",e);
		}
		finally {
			ConnectionPool.getInstance().returnConnection(con);

		}	
	}

	@Override
	public Company getOneCompany(int companyID) throws CouponSystemException {
		String sql ="SELECT * FROM companies WHERE ID = "+companyID;
		Connection con = ConnectionPool.getInstance().getConnection();
		try (Statement stmt = con.createStatement();){
		   ResultSet rs = stmt.executeQuery(sql);
		   Company company = new Company(rs.getInt(0),rs.getString(1),rs.getString(2),rs.getString(3));
			return company;
		} catch (SQLException e) {
			throw new CouponSystemException("getOneCompany failed",e);
		
		}finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
								
	
	}

}

















