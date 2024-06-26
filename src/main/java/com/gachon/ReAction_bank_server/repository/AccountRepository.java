package com.gachon.ReAction_bank_server.repository;

import com.gachon.ReAction_bank_server.entity.Account;
import com.gachon.ReAction_bank_server.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // 이미 계좌번호가 존재한다면 true return
    public Boolean existsByAccountNum(String accountNum);

    // user와 매핑된 account 가져옴
    public Optional<Account> findByUser(User user);


    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.accountNum = :accountNum")
    public Optional<Account> findDepositAccountWithLock(@Param("accountNum") String accountNum);

    @Modifying(clearAutomatically = true)
    @Query("update Account a set a.balance = a.balance + :amount where a = :account")
    public int deposit(@Param("account") Account account, @Param("amount") int amount);

    @Modifying(clearAutomatically = true)
    @Query("update Account a set a.balance = a.balance - :amount where a = :account")
    public int withdraw(@Param("account") Account account, @Param("amount") int amount);


}
