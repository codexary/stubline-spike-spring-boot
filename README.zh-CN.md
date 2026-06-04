[English](README.md) | 简体中文

# Spring Boot 双路由小项目

运行 `./mvnw -q verify` 和 `./mvnw spring-boot:run`。`GET /events/concert-1/seats` 返回两个内存座位；用 `{"eventId":"concert-1","seatId":"A1"}` 调用 `POST /orders` 首次返回 201，再次返回 409。进程重启后内存状态复位。

| Spring 组件 | 原始 HTTP server 与阻塞负载实验中的手写代码 |
| --- | --- |
| `spring-boot-starter-webmvc` 和内嵌服务器 | `RawHttpServer` 的 accept 循环、`ServerExecutor`、socket 生命周期 |
| `spring.threads.virtual.enabled=true` | 阻塞负载实验中的 `ServerExecutor.VIRTUAL` 线程选择 |
| `@SpringBootApplication` | `TicketingMain` 的启动及装配 |
| `@RestController`、`@GetMapping`、`@PostMapping`、`@PathVariable` | 请求行解析和 `TicketingApi.handle` 路由分发 |
| `@RequestBody` 和 MVC 消息转换 | `Content-Length` 分帧及 Jackson JSON 映射 |
| `@ResponseStatus` 和 `ResponseStatusException` | 显式的 `HttpReply` 状态码 |
| `@SpringBootTest` 和 `TestRestTemplate` | 真实 HTTP 集成测试 |

团队仍须理解 servlet 线程模型、请求与连接超时，以及默认错误响应格式，才能部署。Spring Boot 为这三项提供默认值；团队必须按实际负载检查它们。

## 许可证

本仓库采用 [MIT License](LICENSE)。

## 关于本仓库

本仓库包含一项针对 Stubline 的独立实验。README 说明运行命令与观察结果。

本仓库的代码与文档借助 AI 工具编写；记录的输出都来自实际运行。

## 结论

实验结果及其对后端决策的影响见[FINDINGS.zh-CN.md](FINDINGS.zh-CN.md)。
