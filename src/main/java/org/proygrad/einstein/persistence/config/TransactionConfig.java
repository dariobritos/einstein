package org.proygrad.einstein.persistence.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Aspect
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement(mode = AdviceMode.PROXY,
                proxyTargetClass = true)
@ImportResource("classpath:tx-config.xml")
public class TransactionConfig{

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionConfig.class);



    @Pointcut("execution(* org.proygrad.einstein.service.transactional.*.*(..))")
    public void transactional() {
    }

    @Pointcut("execution(* org.proygrad.einstein.service.nontransactional.*.*(..))")
    public void nonTransactional() {
    }

}
