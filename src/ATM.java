import java.util.Scanner;

public class ATM {
	
	public static void main(String[] srgs){
		
		Scanner sc = new Scanner(System.in);
		
		Bank theBank = new Bank("Bank of Drausin");
		
		User aUser = theBank.addUser("John", "Doe", "1234");
		
		Account newAccount = new Account("Checking",aUser,theBank);
		aUser.addAccount(newAccount);
		theBank.addAccount(newAccount);
		
		User curUser;
		while(true){
			curUser = ATM.mainMenuPrompt(theBank,sc);
			
			ATM.printUserMenu(curUser,sc);
		}
	}

	public static User mainMenuPrompt(Bank theBank, Scanner sc){
		String userID;
		String pin;
		User authUser;
		
		do{
			System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
		    System.out.print("Enter user ID: " );
		    userID = sc.nextLine();
		    System.out.print("Enter pin: ");
		    pin = sc.nextLine();
		    
		    authUser = theBank.userLogin(userID, pin);
		    
		    if(authUser == null){
		    	System.out.println("Incorrect user ID/pin combination. " + "Please try again");
		    }
		}while(authUser == null);
		
		return authUser;
	}

	public static void printUserMenu(User theUser,Scanner sc){
		
		theUser.printAccountsSummary();
		//init
		int choice;
		
		do{
			System.out.printf("Welcome %s, what would you like to do?",theUser.getFirstName());
		    System.out.println();
			System.out.println(" 1) Show account transaction history");
		    System.out.println(" 2) Withdraw");
		    System.out.println(" 3) Deposit");
		    System.out.println(" 4) Transfer");
		    System.out.println(" 5) Quit");
		    System.out.println();
		    System.out.println("Enter choice: ");
		    choice = sc.nextInt();
		    if(choice <1 || choice >5){
		    	System.out.println("Invalid choice. Please choode 1-5");
		    }
		    
		}while(choice <1 || choice >5);
		
		switch(choice){
		case 1:
			ATM.showTransHistory(theUser, sc);
			break;
		case 2:
			ATM.withDrawFunds(theUser, sc);
			break;	
		case 3:
			ATM.depositFunds(theUser, sc);
			break;
		case 4:	
			ATM.transferFunds(theUser, sc);
			break;
		}
		
		if(choice != 5){
			ATM.printUserMenu(theUser, sc);
		}
	}
	public static void showTransHistory(User theUser, Scanner sc){
		int theAcct;
		
		do{
			System.out.printf("Enter the number (1-%d) of the account\n" + "whose transactions you want to see: ",theUser.numAccounts());
			
			theAcct = sc.nextInt()-1;
			if(theAcct < 0 || theAcct >= theUser.numAccounts()){
				System.out.println("Invalid account. Please try again.");
			}
		}while(theAcct < 0 || theAcct <= theUser.numAccounts());
		
		theUser.printTransHistory(theAcct);
	}

	public static void transferFunds(User theUser, Scanner sc){
		int fromAcct;
		int toAcct;
		double amount;
		double acctBal;
		
		// get account to transfer from
		do {
			System.out.printf("Enter the number (1-%d) of the account to " + 
					"transfer from: ", theUser.numAccounts());
			fromAcct = sc.nextInt()-1;
			if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(fromAcct);
		
		// get account to transfer to
		do {
			System.out.printf("Enter the number (1-%d) of the account to " + 
					"transfer to: ", theUser.numAccounts());
			toAcct = sc.nextInt()-1;
			if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while (toAcct < 0 || toAcct >= theUser.numAccounts());
		
		// get amount to transfer
		do {
			System.out.printf("Enter the amount to transfer (max $%.02f): $", 
					acctBal);
			amount = sc.nextDouble();
			if (amount < 0) {
				System.out.println("Amount must be greater than zero.");
			} else if (amount > acctBal) {
				System.out.printf("Amount must not be greater than balance " +
						"of $%.02f.\n", acctBal);
			}
		} while (amount < 0 || amount > acctBal);
		
		// finally, do the transfer 
		theUser.addAccountTransaction(fromAcct, -1*amount, String.format(
				"Transfer to account %s", theUser.getAcctUUID(toAcct)));
		theUser.addAccountTransaction(toAcct, amount, String.format(
         "Transfer from account %s", theUser.getAcctUUID(fromAcct)));
	}
	
	
	
	public static void withDrawFunds(User theUser,Scanner sc){
		int fromAcct;
		double amount;
		double acctBal;
		String memo;
		
		// get account to withdraw from
		do {
			System.out.printf("Enter the number (1-%d) of the account to " + 
					"withdraw from: ", theUser.numAccounts());
			fromAcct = sc.nextInt()-1;
			if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(fromAcct);
		
		// get amount to transfer
		do {
			System.out.printf("Enter the amount to withdraw (max $%.02f): $", 
					acctBal);
			amount = sc.nextDouble();
			if (amount < 0) {
				System.out.println("Amount must be greater than zero.");
			} else if (amount > acctBal) {
				System.out.printf("Amount must not be greater than balance " +
						"of $%.02f.\n", acctBal);
			}
		} while (amount < 0 || amount > acctBal);
		
		// gobble up rest of previous input
		sc.nextLine();
		
		// get a memo
		System.out.print("Enter a memo: ");
		memo = sc.nextLine();
		
		// do the withdrwal
       theUser.addAccountTransaction(fromAcct, -1*amount, memo);
	}
public static void depositFunds(User theUser, Scanner sc) {
		
		int toAcct;
		double amount;
		String memo;
		
		// get account to withdraw from
		do {
			System.out.printf("Enter the number (1-%d) of the account to " + 
					"deposit to: ", theUser.numAccounts());
			toAcct = sc.nextInt()-1;
			if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while (toAcct < 0 || toAcct >= theUser.numAccounts());
		
		// get amount to transfer
		do {
			System.out.printf("Enter the amount to deposit: $");
			amount = sc.nextDouble();
			if (amount < 0) {
				System.out.println("Amount must be greater than zero.");
			} 
		} while (amount < 0);
		
		// gobble up rest of previous input
		sc.nextLine();
		
		// get a memo
		System.out.print("Enter a memo: ");
		memo = sc.nextLine();
		
		// do the deposit
		theUser.addAccountTransaction(toAcct, amount, memo);
		
}
	
	
	
}
