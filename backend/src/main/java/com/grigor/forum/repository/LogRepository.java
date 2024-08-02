package com.grigor.forum.repository;

import com.grigor.forum.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Integer> {

}
