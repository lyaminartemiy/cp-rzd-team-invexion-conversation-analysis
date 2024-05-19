package org.invexion.proxy.controller;

import org.springframework.stereotype.Controller;

import org.invexion.proxy.entity.Proxy;
import org.invexion.proxy.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/proxy")
@CrossOrigin
public class ProxyController {

    private final ProxyService proxyService;

    @Autowired
    public ProxyController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @GetMapping("/working")
    public ResponseEntity<Proxy> getWorkingProxy() {
        Proxy proxy = proxyService.getWorkingProxy();
        if (proxy != null) {
            return new ResponseEntity<>(proxy, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/working/string")
    public ResponseEntity<String> getWorkingProxyString() {
        Proxy proxy = proxyService.getWorkingProxy();
        if (proxy != null) {
            return new ResponseEntity<>(proxy.toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Proxy>> getAllProxies(){
        return ResponseEntity.ok(proxyService.getAllProxies());
    }

    @PostMapping
    public ResponseEntity<Proxy> addProxy(@RequestParam String proxy, @RequestParam String proxyType) {
        Proxy addedProxy = proxyService.addProxy(proxy, Proxy.ProxyType.valueOf(proxyType));
        return new ResponseEntity<>(addedProxy, HttpStatus.CREATED);
    }

    @GetMapping("/working/all")
    public ResponseEntity<List<Proxy>> checkAll() {
        List<Proxy> proxies = proxyService.checkAll();
        return new ResponseEntity<>(proxies, HttpStatus.CREATED);
    }
}