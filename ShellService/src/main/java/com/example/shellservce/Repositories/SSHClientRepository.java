package com.example.shellservce.Repositories;

import com.example.shellservce.Entities.SSHClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SSHClientRepository extends JpaRepository<SSHClient, Long> {

    List<Optional<SSHClient>> findSSHClientsByUsername(String username);

}
