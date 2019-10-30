package com.elasticsearch.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class ESConfig {
    @Value("${spring.data.elasticsearch.cluster-name}")
    private String clusterName;

    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String clusterNodes;

    @Bean
    public TransportClient client() throws UnknownHostException {
        //es集群连接
        Settings settings = Settings.builder()
                .put("cluster.name",clusterName)
                .put("client.transport.sniff", true)//增加嗅探机制，找到ES集群
                .put("thread_pool.search.size", Integer.parseInt("10"))//增加线程池个数，暂时设为5
                .build();

        PreBuiltTransportClient client = new PreBuiltTransportClient(settings);
        List<String> nodes = Arrays.asList(clusterNodes.split(","));
        for (String node : nodes) {
            String hostname = node.replace(" ", "").split(":")[0];
            Integer port = Integer.parseInt(node.replace(" ", "").split(":")[1]);
            client.addTransportAddress(new TransportAddress(InetAddress.getByName(hostname), port));
        }

        return client;
    }
}