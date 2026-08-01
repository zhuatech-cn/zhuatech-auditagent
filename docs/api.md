# AuditAgent API 摘要

版权所有 © 2026 上海如静知华信息科技有限公司。

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/login` | 审计用户登录 |
| GET | `/api/admin/dashboard` | 审计项目总览 |
| GET | `/api/admin/work-orders` | 审计程序清单 |
| GET | `/api/shopfloor/dashboard` | 审计人员工作台 |
| POST | `/api/shopfloor/work-orders/{id}/reports` | 提交底稿结论 |
| POST | `/api/shopfloor/agent-preview` | 运行本地审计演示智能体 |
| POST | `/api/shopfloor/audit-sampling` | 按异常率、重要性与控制缺陷生成风险导向抽样建议 |
