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
│   └── mifss-commons-web/                         # Web组件
│
├── mifss-adapters/                                # 适配器模块
│   ├── pom.xml
│   ├── mifss-adapter-core/                        # 适配器通用接口
│   ├── mifss-adapter-generic/                     # 开源适配器
│   ├── mifss-adapter-ali/                         # 阿里云适配器
│   ├── mifss-adapter-huawei/                      # 华为云适配器
│   └── mifss-adapter-tencent/                     # 腾讯云适配器
│
├── mifss-mainline/                                # 主干版本
│   ├── pom.xml
│   ├── mifss-app/                                 # 主干-发布包构建
│   │   ├── pom.xml
│   │   ├── mifss-bas/                             # 监控基础发布包
│   │   ├── mifss-bio/                             # 生物识别发布包
│   │   └── mifss-ipt/                             # 住院服务发布包
│   │
│   ├── mifss-intf/                                # 主干接口定义
│   │   ├── pom.xml
│   │   ├── mifss-intf-bas/                        # 监控基础模块接口
│   │   ├── mifss-intf-bio/                        # 生物识别服务接口
│   │   ├── mifss-intf-ipt/                        # 住院场景服务接口
│   │   ├── mifss-intf-opt/                        # 门诊场景服务接口
│   │   ├── mifss-intf-ckd/                        # 血透场景服务接口
│   │   ├── mifss-intf-pha/                        # 购药场景监控接口
│   │   ├── mifss-intf-trt/                        # 理疗场景监控接口
│   │   ├── mifss-intf-nvr/                        # 视频监控/NVR接口
│   │   ├── mifss-intf-job/                        # 调度任务接口
│   │   ├── mifss-intf-api/                        # 网关鉴权/IoT接入接口
│   │   └── mifss-intf-rpt/                        # 报表服务接口
│   │
│   └── mifss-biz/                                 # 主干业务实现模块
│       ├── pom.xml
│       ├── mifss-biz-bas/                         # 监控基础模块
│       ├── mifss-biz-bio/                         # 生物识别服务
│       ├── mifss-biz-ipt/                         # 住院场景服务
│       ├── mifss-biz-opt/                         # 门诊场景服务
│       ├── mifss-biz-ckd/                         # 血透场景服务
│       ├── mifss-biz-pha/                         # 购药场景服务
│       ├── mifss-biz-trt/                         # 理疗场景服务
│       ├── mifss-biz-nvr/                         # 视频监控/NVR服务
│       ├── mifss-biz-job/                         # 调度任务定义
│       ├── mifss-biz-api/                         # 网关鉴权/IoT接入
│       ├── mifss-biz-rpt/                         # 报表服务模块
│       └── ...
│
└── mifss-projects/                                # 现场版本
    ├── pom.xml
    └── mifss-hunan/                               # 湖南项目
        ├── pom.xml
        ├── mifss-hunan-intf/                      # 湖南扩展API
        └── mifss-hunan-app/                       # 湖南-发布包构建及定制服务
            ├── pom.xml
            ├── mifss-hunan-bio/                   # 定制生物识别服务发布包
            └── mifss-hunan-ipt/                   # 定制住院场景服务发布包

说明1: 现场版本继承主干，可以新增功能、重写主干原有功能，如：重写主干中的类、或只重写类中的一个方法。
说明2: 以mifss-hunan-app-bio为示例，采用继承/重写的方式，实现现场需求的定制开发。

请对mifss应用的所有 pom.xml 文件进行全面诊断，严格按照“最少必须（Minimum Viable Dependencies）”原则，
解决重复与多次引用、无效引用、循环引用、不合理、Scope（作用域）配置不当、传递性依赖过深、版本号硬编码、大杂烩等问题；
并对pom.xml文件中的依赖按照提供商、功能、版本号等常见规则进行分类排列在一起。

springboot中a模块引用了xx.jar  b模块引用了a, 是不是b模块再引用xx.jar 就是多余的？
基于上述传递性依赖原则，再次检查mifss下的所有pom文件，清除不必要的传递性依赖问题。

```
