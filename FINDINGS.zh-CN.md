[English](FINDINGS.md) | 简体中文

# 实验结论

## 观察

这个双路由 Spring Boot 应用通过 `GET /events/concert-1/seats` 返回两个内存座位；同一座位连续提交到 `/orders`，先返回 201，再返回 409。控制器和测试展示了框架处理路由、请求体绑定和响应状态的方式。重启后内存状态会清空。本实验没有订单事件的生产者或消费者。

## 对后端的影响

Spring Boot 为这两个路由处理了 HTTP 报文边界、路由和 JSON 绑定。后端仍要为其他服务使用的订单事件定义明确、带版本的 schema；本实验没有测试事件兼容性，也不能替团队作出这项决定。参见[后端 ADR 0007：Order event encoding](https://github.com/codexary/stubline-backend/blob/main/docs/adr/0007-order-event-encoding.md)。
