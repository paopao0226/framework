package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.Account;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMapper {
    Account getAccountByUsername(String username);
    Account getAccountByUsernameAndPassword(Account account);
    //用户管理
    List<Account> getAllAccount();
    List<Account> searchAccount(String keyword);
    void deleteAccount(String username);
    void deleteProfile(String username);
    void deleteSignon(String username);
    void insertAccount(Account account);
    void insertProfile(Account account);
    void insertSignon(Account account);
    void updateAccount(Account account);
    void updateProfile(Account account);
    void updateSignon(Account account);
}
