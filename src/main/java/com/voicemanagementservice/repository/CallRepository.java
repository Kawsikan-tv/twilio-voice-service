package com.voicemanagementservice.repository;

import com.voicemanagementservice.model.CallModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallRepository extends JpaRepository<CallModel, Long> {
}
