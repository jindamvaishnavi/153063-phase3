package com.cg.mypaytmapp.repo;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;




import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import com.cg.mypaytmapp.beans.Customer;
import com.cg.mypaytmapp.beans.Wallet;
import com.cg.mypaytmapp.exception.InvalidInputException;


public class WalletRepoImpl implements WalletRepo{
	
	private Map<String, Customer> data=new HashMap<>();
	
	public Map<String,Customer>getData(){
		return data;
	}
	 public void setData(Map<String,Customer> data) {
		 this.data=data;
	 }
	 
	 Customer cust=new Customer();
	public WalletRepoImpl(Map<String, Customer> data) {
		super();
		this.data = data;
	}
	public WalletRepoImpl()
	{
		
	}
	EntityManagerFactory emf=Persistence.createEntityManagerFactory("Paytm applicationusingJPA");
	EntityManager em=emf.createEntityManager();
	EntityTransaction tx=em.getTransaction();
	
		public boolean save(Customer customer) 
		{
		
		tx.begin();
		em.persist(customer);
		tx.commit();
		
		return true;
	}

	public Customer findOne(String mobileNo) {
		
		tx.begin();
		Customer cust=em.find(Customer.class, mobileNo);
		tx.commit();
		
		return cust;
		
	}
	
	@Override
	public void update(Customer customer) {
		
		tx.begin();
		em.persist(em.merge(customer));
		tx.commit();
	}
	
	
}
