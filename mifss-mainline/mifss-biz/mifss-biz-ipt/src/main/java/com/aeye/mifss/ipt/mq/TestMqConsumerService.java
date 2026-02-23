package com.aeye.mifss.ipt.mq;

import cn.hsa.hsaf.core.mq.MQBusinessHandler;
import cn.hsa.hsaf.core.mq.MQMessage;
import cn.hsa.hsaf.core.mq.MQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("testMqConsumerService")
public class TestMqConsumerService implements MQBusinessHandler {
    private static final Logger log = LoggerFactory.getLogger(TestMqConsumerService.class);

    @Autowired(required = false)
    private MQProducer mqProducer;

    @Override
    public boolean doBusiness(MQMessage<?> mqMessage) {
        System.out.println(mqMessage.getContent().toString());
        return true;
    }


}
