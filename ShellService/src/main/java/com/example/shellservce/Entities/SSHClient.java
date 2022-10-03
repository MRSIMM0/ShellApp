package com.example.shellservce.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "shhclient")
public class SSHClient {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ssh_seq")
    @SequenceGenerator(name = "ssh_seq", sequenceName = "ssh_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String username;

    private String host;
    @Column(name = "ssh_user")
    private String user;

    private String port;

    private String password;

    private String key;


}
