package org.invexion.proxy.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.invexion.proxy.entity.Proxy;
import org.apache.hc.client5.http.auth.Credentials;
import org.invexion.proxy.repository.ProxyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProxyService {
    private final ProxyRepository proxyRepository;
    private final PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;

    public Proxy getWorkingProxy(){
        return proxyRepository.findAllAliveProxies()
                .stream()
                .filter(this::testProxy)
                .findFirst()
                .orElse(null);
    }

    private boolean testProxy(Proxy proxy){
        if (proxy == null) {
            return false;
        }
        try {

            RestTemplate restTemplate = new RestTemplate();

            Credentials credentials = new UsernamePasswordCredentials(proxy.getUsername(), proxy.getPassword().toCharArray());

            BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(new AuthScope(proxy.getHost(), proxy.getPort()), credentials);

            HttpHost myProxy = new HttpHost(proxy.getHost(), proxy.getPort());


            HttpClientBuilder clientBuilder = HttpClients.custom();
            clientBuilder.setProxy(myProxy)
                    .setDefaultCredentialsProvider(credsProvider)
                    .disableCookieManagement()
                    .setConnectionManager(poolingHttpClientConnectionManager);

            CloseableHttpClient httpClient = clientBuilder.build();
            HttpComponentsClientHttpRequestFactory factory= new HttpComponentsClientHttpRequestFactory(httpClient);
            factory.setConnectTimeout(1_000);
            factory.setConnectionRequestTimeout(1_000);

            restTemplate.setRequestFactory(factory);


            try {
                ResponseEntity<String> response = restTemplate.getForEntity("http://jsonplaceholder.typicode.com/todos/1", String.class);
                log.info(response.toString());
                return response.getStatusCode().is2xxSuccessful();
            } catch (Exception e) {

                proxy.setAlive(false);
                proxyRepository.save(proxy);
                e.printStackTrace();

                return false;
            }

        }catch (Exception e){
            log.warn("Failed testing proxy "+ e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public Proxy addProxy(String proxy, Proxy.ProxyType proxyType){
        return proxyRepository.save(new Proxy(proxy,proxyType));
    }

    @PostConstruct
    public void init(){
        log.info(addProxy("KSUKWr:JTc1fe@168.80.81.26:8000", Proxy.ProxyType.HTTP).toString());
    }

    public List<Proxy> getAllProxies() {
        return proxyRepository.findAll();
    }

    public List<Proxy> checkAll() {
        return proxyRepository.findAll()
                .stream()
                .filter(this::testProxy)
                .toList();

    }
}
