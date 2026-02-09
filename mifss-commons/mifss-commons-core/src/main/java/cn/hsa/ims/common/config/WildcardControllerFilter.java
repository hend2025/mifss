package cn.hsa.ims.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import java.io.IOException;

/**
 * 支持多级目录通配的控制器替换过滤器
 */
public class WildcardControllerFilter implements TypeFilter, EnvironmentAware {

    private static final Logger log = LoggerFactory.getLogger(WildcardControllerFilter.class);

    private String mainRoot;
    private String siteRoot;
    private String siteClassPrefix;

    @Override
    public void setEnvironment(Environment environment) {
        // 从配置文件读取根路径
        this.mainRoot = environment.getProperty("project.override.main-root");
        this.siteRoot = environment.getProperty("project.override.site-root");
        this.siteClassPrefix = environment.getProperty("project.override.class-prefix", "");
    }

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        String className = metadataReader.getClassMetadata().getClassName();

        // 1. 判定是否属于需要处理的主干包范围
        if (mainRoot != null && className.startsWith(mainRoot)) {

            // 2. 提取包名和类名
            String packageName = className.substring(0, className.lastIndexOf("."));
            String simpleClassName = ClassUtils.getShortName(className);

            // 3. 将主干包前缀替换为现场包前缀 (实现 ** 通配的效果)
            // 比如 com.main.project.a.b.controller -> com.site.project.a.b.controller
            String targetPackage = packageName.replace(mainRoot, siteRoot);

            // 4. 拼接预期的现场类全路径
            String expectedSiteClassName = targetPackage + "." + siteClassPrefix + simpleClassName;

            try {
                // 5. 探测现场类是否存在。如果存在，说明要覆盖。
                Class.forName(expectedSiteClassName);

                log.info("[Wildcard Override] 发现覆盖类: 排除主干 {} -> 启用现场 {}", className, expectedSiteClassName);
                return true; // 返回 true 表示命中排除规则，主干 Controller 不会被加载
            } catch (ClassNotFoundException e) {
                // 现场没有对应的类，则不拦截，让主干 Controller 正常工作
                return false;
            }
        }
        return false;
    }

}