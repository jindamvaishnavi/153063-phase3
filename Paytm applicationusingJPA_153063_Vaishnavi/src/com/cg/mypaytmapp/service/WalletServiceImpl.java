package com.cg.mypaytmapp.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Scanner;

import com.cg.mypaytmapp.beans.Customer;
import com.cg.mypaytmapp.beans.Wallet;
import com.cg.mypaytmapp.exception.InvalidInputException;
import com.cg.mypaytmapp.repo.WalletRepo;
import com.cg.mypaytmapp.repo.WalletRepoImpl;

public class WalletServiceImpl implements WalletService{

private WalletRepo repo;
	
	public WalletServiceImpl(Map<String, Customer> data){
		repo= new WalletRepoImpl(data);
	}
	public WalletServiceImpl(WalletRepo repo) {
		super();
		this.repo = repo;
	}

	public WalletServiceImpl() {
		repo=new WalletRepoImpl();
}
	
public boolean validatephone(String phoneno) {
		
		String pattern1="[7-9]?[0-9]{10}";
		if(phoneno.matches(pattern1))
		{
			return true;
		}else {
			return false;
		}
	}
	public boolean validateName(String pName) {
		String pattern="[A-Z][a-zA-Z]*";
		if(pName.matches(pattern))
		{
			return true;
		}else {
			return false;
		}
	}
	
	WalletRepoImpl obj=new WalletRepoImpl();
	public Customer createAccount(String name, String mobileNo, BigDecimal amount)
	{
		
		Customer cust=new Customer(name,mobileNo,new Wallet(amount));
		acceptCustomerDetails(cust);
		boolean result=repo.save(cust);
		repo.update(cust);
		if(result==true)
			return cust;
		else
			return null;
		//create an object of customer and call dao save layer
				
		}

	public Customer showBalance(String mobileNo) {
		Customer customer=repo.findOne(mobileNo);
		if(customer!=null)
			return customer;
		else
			throw new InvalidInputException("Invalid mobile no ");
	}

	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) {
		Customer scust=new Customer();
		Customer tcust=new Customer();
		Wallet sw=new Wallet();
		Wallet tw=new Wallet();
		scust=repo.findOne(sourceMobileNo);
		tcust=repo.findOne(targetMobileNo);
		
		if(scust!=null && tcust!=null)
		{
			BigDecimal amtsub=scust.getWallet().getBalance();
			BigDecimal diff=amtsub.subtract(amount);
			sw.setBalance(diff);
			scust.setWallet(sw);
			repo.update(scust);
			
			BigDecimal amtAdd=tcust.getWallet().getBalance();
			BigDecimal sum=amtAdd.add(amount);
			tw.setBalance(sum);
			tcust.setWallet(tw);
			repo.update(tcust);
			obj.getData().put(targetMobileNo, tcust);
			obj.getData().put(sourceMobileNo, scust);
		}
		else
		{
			
		}
		return tcust;
	}

	public Customer depositAmount(String mobileNo, BigDecimal amount) {
		
		Customer cust=new Customer();
		Wallet wallet=new Wallet();
		
		cust=repo.findOne(mobileNo);
		if(cust!=null)
		{
			BigDecimal amtAdd=cust.getWallet().getBalance().add(amount);
			wallet.setBalance(amtAdd);
			cust.setWallet(wallet);
			obj.getData().put(mobileNo, cust);
			repo.update(cust);
			
		}
		
		return cust;
	}

	public Customer withdrawAmount(String mobileNo, BigDecimal amount) {
		Customer cust=new Customer();
		Wallet wallet=new Wallet();
		
		cust=repo.findOne(mobileNo);
		if(cust!=null)
		{
			BigDecimal amtSub=cust.getWallet().getBalance().subtract(amount);
			wallet.setBalance(amtSub);
			cust.setWallet(wallet);
			obj.getData().put(mobileNo, cust);
			repo.update(cust);
		}
		return cust;
		
}
	public void acceptCustomerDetails(Customer cust)  {
	Scanner sc=new Scanner(System.in);
	while (true) {
		String str=cust.getMobileNo();
		if(validatephone(str))//method validate name
		{
			
			break;
		}
		else
		{
			System.err.println("Wrong Phone number!!\n Please Start with 9 ");
			System.out.println("Enter Phone number Again eg:9876543210");
			cust.setMobileNo(sc.next());
		}
	}
	while (true) {
		String str1=cust.getName();
		if(validateName(str1))//method validate name
		{
			break;
		}
		else
		{
			System.err.println("Wrong  Name!!\n Please Start with Capital letter ");
			System.out.println("Enter  Name Again eg:Name");
			cust.setName(sc.next());
		}
	}
}
	
}
