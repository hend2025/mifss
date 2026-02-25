package com.aeye.mifss.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class AeyeGmConfig {
    @Value("${gm.privateKey:AJPvjBsTHtuu5XzucTcCpyWxN13V9bgqmTA5ScCoIv5b}")
    private String privateKey;
    @Value("${gm.pubKey:5aEWtTgTtyaAl2xg4NH45xxqFqzOuFmGDjCSx9gDmG2sBk6ZZdht9imDBdo871rWZOIC+by/fzyaSp3jKbgjKQ==}")
    private String pubKey;
}
