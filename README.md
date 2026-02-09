# System Directory Structure

```text

mifss/
├── pom.xml                                        # Root POM
│
├── mifss-dependencies/                            # 全局依赖管理
│   └── pom.xml
│
├── mifss-commons/                                 # 公共模块
│   ├── pom.xml
│   ├── mifss-commons-core/                        # 核心工具类
│   ├── mifss-commons-security/                    # 安全组件
│   ├── mifss-commons-orm/                         # 数据库组件
│   ├── mifss-commons-web/                         # Web组件
│   └── mifss-commons-swagger/                     # API组件
│
├── mifss-adapters/                                # 适配器模块
│   ├── pom.xml
│   ├── mifss-adapter-api/                         # 适配器API接口
│   ├── mifss-adapter-generic/                     # 通用适配器（开源/本地）
│   ├── mifss-adapter-ali/                         # 阿里云适配器
│   ├── mifss-adapter-huawei/                      # 华为云适配器
│   └── mifss-adapter-tencent/                     # 腾讯云适配器
│
├── mifss-mainline/                                # 主干版本
│   ├── pom.xml
│   ├── mifss-app/                                 # 主干-发布包构建
│   ├── mifss-intf/                                # 主干接口定义
│   │   ├── pom.xml
│   │   ├── mifss-intf-bas/                        # 监控基础模块接口
│   │   ├── mifss-intf-bio/                        # 生物识别服务接口
│   │   ├── mifss-intf-ipt/                        # 住院场景服务接口
│   │   ├── mifss-intf-opt/                        # 门诊场景服务接口
│   │   ├── mifss-intf-hd/                         # 血透场景服务接口
│   │   ├── mifss-intf-pha/                        # 购药场景监控接口
│   │   ├── mifss-intf-trt/                        # 理疗场景监控接口
│   │   ├── mifss-intf-video/                      # 视频监控接口
│   │   ├── mifss-intf-job/                        # 调度任务接口
│   │   └── mifss-intf-api/                        # 网关鉴权/IoT接入接口
│   │
│   └── mifss-biz/                                 # 主干业务实现模块
│       ├── pom.xml
│       ├── mifss-biz-bas/                         # 监控基础模块
│       ├── mifss-biz-bio/                         # 生物识别服务
│       ├── mifss-biz-ipt/                         # 住院场景服务
│       ├── mifss-biz-opt/                         # 门诊场景服务
│       ├── mifss-biz-hd/                          # 血透场景服务
│       ├── mifss-biz-pha/                         # 购药场景服务
│       ├── mifss-biz-trt/                         # 理疗场景服务
│       ├── mifss-biz-video/                       # 视频监控服务
│       ├── mifss-biz-job/                         # 调度任务定义
│       ├── mifss-biz-api/                         # 网关鉴权/IoT接入
│       ├── mifss-biz-bc/                          # 业务中心聚合模块
│       └── ...
│
└── mifss-projects/                                # 现场版本
    ├── pom.xml
    └── mifss-guangxi/                             # 广西项目
        ├── pom.xml
        ├── mifss-guangxi-app/                     # 广西-发布包构建        
        ├── mifss-guangxi-dependencies/            # 广西依赖管理
        ├── mifss-guangxi-intf/                    # 广西扩展API
        └── mifss-guangxi-biz/                     # 广西定制服务
            ├── pom.xml
            ├── mifss-guangxi-app-bio/             # 定制生物识别服务
            ├── mifss-guangxi-app-ipt/             # 定制住院场景服务
            └── mifss-guangxi-app-hd/              # 定制血透场景服务


请使用jdk1.8、springboot2.0.9、mysql5.7、mybatis plus、Dubbo3.0、spring-security-core5.7.12、redis、hutool-all、fastjson等创建这个系统的手脚架。
要求1: 模块命名严格按照上面的定义。
要求2: 现场版本继承主干，同时可以新增功能、也可以重写主干原有功能，如：重写主干中的类、或只重写类中的一个方法。
要求3: 以mifss-guangxi-app-bio为示例，采用继承/重写的方式实现人脸识别服务主干版本和现场版本的定制需求。


你是一位拥有 20 年经验的 Java 首席架构师，精通 Maven 依赖仲裁机制、类加载隔离及 JVM 性能优化。
核心任务1： 严格按照“最少必须（Minimum Viable Dependencies）”原则，对所有 pom.xml 文件进行全面诊断，提供深度优化建议；
核心任务2： 重复与多次引用、无效引用、循环引用、不合理、Scope（作用域）配置不当、传递性依赖过深、版本号硬编码、大杂烩；
核心任务3： 依赖冲突与重复性审计、引用有效性与“瘦身”审计、循环依赖与架构合规性、行业特性与安全审计；
核心任务4： pom.xml文件中的依赖按照提供商、功能、版本号等进行分类，并提供依赖仲裁机制的说明。

-Dspring.main.allow-circular-references=true

```
