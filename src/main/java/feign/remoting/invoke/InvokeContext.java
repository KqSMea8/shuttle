package feign.remoting.invoke;

import java.util.concurrent.ConcurrentHashMap;

//对于调用的上下文环境，包括客户端
public class InvokeContext {
    // ~~~ invoke context keys of client side
    public final static String                CLIENT_LOCAL_IP        = "bolt.client.local.ip";
    public final static String                CLIENT_LOCAL_PORT      = "bolt.client.local.port";
    public final static String                CLIENT_REMOTE_IP       = "bolt.client.remote.ip";
    public final static String                CLIENT_REMOTE_PORT     = "bolt.client.remote.port";
    //客户端创建Connection对象的时间
    public final static String                CLIENT_CONN_CREATETIME = "bolt.client.conn.createtime";

    // ~~~ invoke context keys of server side
    public final static String                SERVER_LOCAL_IP        = "bolt.server.local.ip";
    public final static String                SERVER_LOCAL_PORT      = "bolt.server.local.port";
    public final static String                SERVER_REMOTE_IP       = "bolt.server.remote.ip";
    public final static String                SERVER_REMOTE_PORT     = "bolt.server.remote.port";

    // ~~~ invoke context keys of bolt client and server side
    public final static String                BOLT_INVOKE_REQUEST_ID = "bolt.invoke.request.id";
    //从请求到达至请求被处理，这是个时间戳
    /** time consumed start from the time when request arrive, to the time when request be processed, this is a timespan */
    public final static String                BOLT_PROCESS_WAIT_TIME = "bolt.invoke.wait.time";
    public final static String                BOLT_CUSTOM_SERIALIZER = "bolt.invoke.custom.serializer";
    public final static String                BOLT_CRC_SWITCH        = "bolt.invoke.crc.switch";

    // ~~~ constants
    public final static int                   INITIAL_SIZE           = 8;

    /** context */
    private ConcurrentHashMap<String, Object> context;

    /**
     * default construct
     */
    public InvokeContext() {
        this.context = new ConcurrentHashMap<String, Object>(INITIAL_SIZE);
    }

    /**
     * put if absent
     *
     * @param key
     * @param value
     */
    public void putIfAbsent(String key, Object value) {
        this.context.putIfAbsent(key, value);
    }

    /**
     * put
     *
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        this.context.put(key, value);
    }

    /**
     * get
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) this.context.get(key);
    }

    /**
     * get and use default if not found
     * 
     * @param key
     * @param defaultIfNotFound
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defaultIfNotFound) {
        return this.context.get(key) != null ? (T) this.context.get(key) : defaultIfNotFound;
    }

    /**
     * clear all mappings.
     */
    public void clear() {
        this.context.clear();
    }
}
