package org.invexion.proxy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "proxies")
public class Proxy {

    public enum ProxyType {
        HTTP,
        SOCKS5
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String host;
    private int port;

    @Enumerated(EnumType.STRING)
    private ProxyType type;

    private String username;
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    private Date creationDate;

    private boolean alive;

    @Override
    public String toString() {
        return username+":"+password+"@"+host+":"+port;
    }

    public Proxy(String proxyString, ProxyType proxyType) {
        String[] parts = proxyString.split("@");
        if (parts.length == 2) {
            String[] loginParts = parts[0].split(":");
            if (loginParts.length == 2) {
                this.username = loginParts[0];
                this.password = loginParts[1];
            }
            String[] addressParts = parts[1].split(":");
            if (addressParts.length == 2) {
                this.host = addressParts[0];
                this.port = Integer.parseInt(addressParts[1]);
            }
        }
        this.creationDate = new Date();
        this.alive = true;
        this.type=proxyType;
    }

}