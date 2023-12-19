package com.example.coffee.repository;

import com.example.coffee.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    @Query("SELECT a FROM Address a WHERE a.order.id = :orderId")
    Page<Address> findByOrder_Id(Integer orderId, Pageable pageable);

    @Query("SELECT a FROM Address a WHERE a.user.id = :userId")
    Page<Address> findByUserId(Integer userId, Pageable pageable);;
}


