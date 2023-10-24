package com.example.bookAnalyzer.bd_service;


import com.example.bookAnalyzer.models.PropModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PropRepository extends JpaRepository<PropModel,String> {
    @Modifying
    @Query("UPDATE PropModel SET value=:value WHERE key=:key ")
    void setNewValue(@Param("key")String key, @Param("value")String value);
}
