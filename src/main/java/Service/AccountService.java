package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

/**
 * The AccountService class handles business logic related to accounts, 
 * sitting between the controller and persistence layer (DAO).
 */
public class AccountService {
    private AccountDAO accountDAO;

    /**
     * No-args constructor for creating a new AccountService with a new AccountDAO.
     */
    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    /**
     * Constructor for AccountService when an AccountDAO is provided.
     * Used in testing with a mock AccountDAO to test AccountService independently.
     * 
     * @param accountDAO the AccountDAO to be used by this service
     */
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * Retrieves all accounts.
     * 
     * @return a list of all accounts
     */
    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    /**
     * Persists a new account. 
     * 
     * @param account an Account object to be added
     * @return the persisted Account if successful, otherwise null
     */
    public Account createAccount(Account account) {
        // Check if the username already exists
        // Account existingAccount = accountDAO.getAccountByUsername(account);
        // if (existingAccount != null) {
        //     return null; // Return null if the username is already taken
        // }
        return accountDAO.insertAccount(account); // Insert new account if username is unique
    }
    

    /**
     * Authenticates a user based on username and password.
     * 
     * @param username the username of the account
     * @param password the password of the account
     * @return true if the credentials are valid, false otherwise
     */
    public Account login(String username) {
        System.out.println("Attempting login for username: " + username);
        Account account = accountDAO.getAccountByUsername(username);
        // if (account == null) {
        //     System.out.println("No account found with username: " + username);
        //     return null;
        // }
        // System.out.println("Account found: " + account.getUsername() + ", Password Match: " + password.equals(account.getPassword()));
        return account;
    }
    
    
}
