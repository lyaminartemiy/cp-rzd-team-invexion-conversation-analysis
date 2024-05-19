package org.invexion.proxy.repository;

import org.invexion.proxy.entity.Proxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProxyRepository extends JpaRepository<Proxy, Long> {

    @Query("select p from Proxy p where p.alive=true")
    Proxy findAliveProxy();

    @Query("select p from Proxy p where p.alive=true")
    List<Proxy> findAllAliveProxies();
}