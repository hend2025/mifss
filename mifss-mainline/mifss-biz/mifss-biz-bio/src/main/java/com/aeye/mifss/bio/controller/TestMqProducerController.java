package com.aeye.mifss.bio.controller;

import cn.hsa.hsaf.core.mq.MQMessage;
import cn.hsa.hsaf.core.mq.MQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息生产测试服务
 */
@RestController
@RequestMapping("/mq")
public class TestMqProducerController {

    @Autowired(required = false)
    private MQProducer mqProducer;

    @GetMapping("/send")
    public String sendTestMessage() {
        try {
            if (mqProducer != null) {
                MQMessage mqMessage = new MQMessage();
                mqMessage.setTopic("MQ_IMS_CRTF_TEST");
                mqMessage.setTag("crtf");
                mqMessage.setContent("测试消息内容");
                mqMessage.setTimestamp(System.currentTimeMillis());

                boolean stats = mqProducer.send(mqMessage);
                System.out.println(stats);

                System.out.println("消息发送成功");
                return "消息发送成功";
            } else {
                return "未找到MQ生产者或配置";
            }
        } catch (Exception e) {
            return "消息发送异常";
        }
    }

}
