package com.wjy.canal.listener;

import com.alibaba.fastjson.JSONObject;
import com.wjy.canal.pojo.CanalMessage;
import com.wjy.canal.pojo.User;
import com.wjy.canal.processor.RedisProcessor;
import com.wjy.common.constant.Constants;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CanalRabbitMQListener {
    @Autowired
    private RedisProcessor redisProcessor;

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "canal.queue", durable = "true"),
                    exchange = @Exchange(value = "canal.exchange"),
                    key = "canal.routing.key"

            )
    })
    public void handleDataChange(String message) {
       CanalMessage canalMessage = JSONObject.parseObject(message, CanalMessage.class);
       // 对于删除和修改操作
       if (!canalMessage.getType().equals("INSERT")) {
           List<User> data = canalMessage.getData();
           if (data == null) return;
           for (User user : data) {
               redisProcessor.delete(user.getId() + Constants.OFFSET + "");
           }
       }

    }
}
