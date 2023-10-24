package com.example.ZubrilkaEnglishServer.repositories;

import com.example.ZubrilkaEnglishServer.models.PropModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropRepository extends JpaRepository<PropModel,String> {
    Optional<PropModel> findByKey(String key);
    @Modifying
    @Query("UPDATE PropModel SET value=:value WHERE key=:key ")
    void setNewValue(@Param("key")String key,@Param("value")String value);
}
