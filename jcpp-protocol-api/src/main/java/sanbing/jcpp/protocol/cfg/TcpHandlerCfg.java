/**
 * 抖音关注：程序员三丙
 * 知识星球：https://t.zsxq.com/j9b21
 */
package sanbing.jcpp.protocol.cfg;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import sanbing.jcpp.infrastructure.util.jackson.JacksonUtil;
import sanbing.jcpp.infrastructure.util.property.PropertyUtils;
import sanbing.jcpp.protocol.cfg.enums.TcpHandlerType;
import sanbing.jcpp.protocol.listener.tcp.configs.BinaryHandlerConfiguration;
import sanbing.jcpp.protocol.listener.tcp.configs.HandlerConfiguration;
import sanbing.jcpp.protocol.listener.tcp.configs.JsonHandlerConfiguration;
import sanbing.jcpp.protocol.listener.tcp.configs.TextHandlerConfiguration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TcpHandlerCfg {

    @Getter
    private TcpHandlerType type;

    @Min(1)
    @Setter
    @Getter
    private int idleTimeoutSeconds;

    @Min(1)
    @Setter
    @Getter
    private int maxConnections;

    private final Map<TcpHandlerType, HandlerConfiguration> HANDLER_MAP = new ConcurrentHashMap<>();

    public HandlerConfiguration getConfiguration(TcpHandlerType type) {
        return HANDLER_MAP.get(type);
    }

    public void setConfiguration(String configuration) {
        final JsonNode cfgJson = JacksonUtil.valueToTree(PropertyUtils.getProps(configuration));

        type = TcpHandlerType.valueOf(cfgJson.get("type").asText());

        switch (type) {
            case TEXT -> HANDLER_MAP.put(type, JacksonUtil.treeToValue(cfgJson, TextHandlerConfiguration.class));
            case JSON -> HANDLER_MAP.put(type, JacksonUtil.treeToValue(cfgJson, JsonHandlerConfiguration.class));
            case BINARY -> HANDLER_MAP.put(type, JacksonUtil.treeToValue(cfgJson, BinaryHandlerConfiguration.class));
            default -> throw new IllegalArgumentException("Unknown TCP handler type: " + type);
        }
    }
}
