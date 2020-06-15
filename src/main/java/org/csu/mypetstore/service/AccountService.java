package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Role;
import org.csu.mypetstore.persistence.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("accountService")
public class AccountService {
    @Autowired
    private AccountMapper accountMapper;

    public Account getAccount(String username){
        return accountMapper.getAccountByUsername(username);
    }
    public Account getAccount(String username,String password){
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        System.out.println(username + " " + password);
        return accountMapper.getAccountByUsernameAndPassword(account);
    }
    public List<Account> getAllAccount(){
        return accountMapper.getAllAccount();
    }
    public List<Account> searchAccount(String keyword) {
        return accountMapper.searchAccount(keyword);
    }
    public void deleteAccount(String username){
        accountMapper.deleteAccount(username);
        accountMapper.deleteProfile(username);
        accountMapper.deleteSignon(username);
        accountMapper.deleteRole(username);
    }
    public void insertAccount(Account account){
        accountMapper.insertAccount(account);
        accountMapper.insertSignon(account);
        accountMapper.insertProfile(account);
        accountMapper.insertRole(account);
    }
    public void updateAccount(Account account){
        accountMapper.updateAccount(account);
        accountMapper.updateProfile(account);
        if(account.getPassword() != null && account.getPassword().length() > 0){
            accountMapper.updateSignon(account);
        }
        accountMapper.updateRole(account);
    }
    public boolean usernameIsExist(String username){
        Account account = this.getAccount(username);
        if(account != null)return true;
        else return false;
    }
    //用户鉴权用
    public List<Role> findRoleByUsername(String username){
        List<Role> role = accountMapper.findRoleByUsername(username);
        return role;
    }
}
