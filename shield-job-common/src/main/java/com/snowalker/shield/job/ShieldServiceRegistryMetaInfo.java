package com.snowalker.shield.job;

import java.io.Serializable;
import java.util.Date;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/1/18 17:12
 * @className ShieldServiceRegistryInfo
 * @desc shield-job服务注册元数据
 */
public class ShieldServiceRegistryMetaInfo implements Serializable {

    private static final long serialVersionUID = 6645115210200784838L;

    /**自然主键*/
    private int id;
    /**任务调度服务主键*/
    private String serviceId;
    /**服务功能描述*/
    private String serviceName;
    /**服务描述*/
    private String serviceDesc;
    /**服务所属应用名*/
    private String serviceAppName;
    /**服务ip地址,格式：ip:port*/
    private String serviceAddress;
    /**服务ip*/
    private String serviceIp;
    /**服务端口*/
    private String servicePort;
    /**服务绑定任务id*/
    private String taskId;
    /**任务MQ主题*/
    private String taskMqTopic;
    /**服务完整类名*/
    private String serviceClassName;
    /**服务方法名*/
    private String serviceMethodName;
    /**执行参数*/
    private String serviceParamList;
    /**路由策略*/
    private String serviceRouteStrategy;
    /**代码脚本*/
    private String serviceScript;
    /**入库时间*/
    private Date gmtCreate;
    /**修改时间*/
    private Date gmtModified;

    public int getId() {
        return id;
    }

    public ShieldServiceRegistryMetaInfo setId(int id) {
        this.id = id;
        return this;
    }

    public String getServiceId() {
        return serviceId;
    }

    public ShieldServiceRegistryMetaInfo setServiceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public String getServiceName() {
        return serviceName;
    }

    public ShieldServiceRegistryMetaInfo setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public ShieldServiceRegistryMetaInfo setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
        return this;
    }

    public String getServiceAppName() {
        return serviceAppName;
    }

    public ShieldServiceRegistryMetaInfo setServiceAppName(String serviceAppName) {
        this.serviceAppName = serviceAppName;
        return this;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public ShieldServiceRegistryMetaInfo setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
        return this;
    }

    public String getServiceIp() {
        return serviceIp;
    }

    public ShieldServiceRegistryMetaInfo setServiceIp(String serviceIp) {
        this.serviceIp = serviceIp;
        return this;
    }

    public String getServicePort() {
        return servicePort;
    }

    public ShieldServiceRegistryMetaInfo setServicePort(String servicePort) {
        this.servicePort = servicePort;
        return this;
    }

    public String getTaskId() {
        return taskId;
    }

    public ShieldServiceRegistryMetaInfo setTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    public String getTaskMqTopic() {
        return taskMqTopic;
    }

    public ShieldServiceRegistryMetaInfo setTaskMqTopic(String taskMqTopic) {
        this.taskMqTopic = taskMqTopic;
        return this;
    }

    public String getServiceClassName() {
        return serviceClassName;
    }

    public ShieldServiceRegistryMetaInfo setServiceClassName(String serviceClassName) {
        this.serviceClassName = serviceClassName;
        return this;
    }

    public String getServiceMethodName() {
        return serviceMethodName;
    }

    public ShieldServiceRegistryMetaInfo setServiceMethodName(String serviceMethodName) {
        this.serviceMethodName = serviceMethodName;
        return this;
    }

    public String getServiceParamList() {
        return serviceParamList;
    }

    public ShieldServiceRegistryMetaInfo setServiceParamList(String serviceParamList) {
        this.serviceParamList = serviceParamList;
        return this;
    }

    public String getServiceRouteStrategy() {
        return serviceRouteStrategy;
    }

    public ShieldServiceRegistryMetaInfo setServiceRouteStrategy(String serviceRouteStrategy) {
        this.serviceRouteStrategy = serviceRouteStrategy;
        return this;
    }

    public String getServiceScript() {
        return serviceScript;
    }

    public ShieldServiceRegistryMetaInfo setServiceScript(String serviceScript) {
        this.serviceScript = serviceScript;
        return this;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public ShieldServiceRegistryMetaInfo setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        return this;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public ShieldServiceRegistryMetaInfo setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
        return this;
    }

    @Override
    public String toString() {
        return "ShieldServiceRegistryMetaInfo{" +
                "id=" + id +
                ", serviceId='" + serviceId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", serviceDesc='" + serviceDesc + '\'' +
                ", serviceAppName='" + serviceAppName + '\'' +
                ", serviceAddress='" + serviceAddress + '\'' +
                ", serviceIp='" + serviceIp + '\'' +
                ", servicePort='" + servicePort + '\'' +
                ", taskId='" + taskId + '\'' +
                ", taskMqTopic='" + taskMqTopic + '\'' +
                ", serviceClassName='" + serviceClassName + '\'' +
                ", serviceMethodName='" + serviceMethodName + '\'' +
                ", serviceParamList='" + serviceParamList + '\'' +
                ", serviceRouteStrategy='" + serviceRouteStrategy + '\'' +
                ", serviceScript='" + serviceScript + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
