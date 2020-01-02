package com.xl.game.util;


import lombok.extern.slf4j.Slf4j;

import java.lang.management.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * 系统工具
 *
 * @author xuliang
 * @date 2019-12-24 QQ:2755055412
 */
@Slf4j
public class SysUtil {


    private static final Runtime RUNTIME = Runtime.getRuntime();
    private static final int MB_NUM = 1024 * 1024;

    public SysUtil() {
    }


    /**
     * 关闭服务
     *
     * @param cls
     * @param e
     * @param info
     * @param obs
     */

    public static void exit(Class<?> cls, Exception e, String info, Objects... obs) {
        log.error(cls.getName(), e);
        log.warn(info, obs);
        System.exit(1);
    }

    /**
     * 虚拟机可用cpu
     *
     * @return
     * @author xuliang
     * @QQ 2755055412 2019年12月24日 下午1:56:46
     */

    public static int availableProcessors() {
        return RUNTIME.availableProcessors() / MB_NUM;
    }


    /**
     * 空闲内存
     *
     * @return
     * @author xulaing
     * @QQ 2755055412 2019年12月24日 下午1:59:56
     */

    public static int freeMemory() {
        return (int) RUNTIME.freeMemory() / MB_NUM;
    }


    /**
     * 可用内存
     *
     * @return
     * @author xuliang
     * @QQ 2755055412 2019年12月24日 下午2:01:32
     */

    public static int totalMemory() {
        return (int) RUNTIME.totalMemory() / MB_NUM;
    }


    /**
     * 虚拟机能获取的最大内存
     *
     * @return
     * @author xuliang
     * @QQ 2755055412 2019年12月24日 下午2:03:08
     */

    public static int maxMemory() {
        return (int) RUNTIME.maxMemory() / MB_NUM;
    }


    /**
     * 类加载信息
     *
     * @param spliteStr
     * @return
     * @author xuliang
     * @QQ 359135103 2019年12月24日 下午3:39:22
     */

    public static String classLoadInfo(String spliteStr) {
        ClassLoadingMXBean bean = ManagementFactory.getClassLoadingMXBean();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("类加载数：  " + bean.getLoadedClassCount()).append(spliteStr);
        stringBuilder.append("类加载总数：  " + bean.getTotalLoadedClassCount()).append(spliteStr);
        stringBuilder.append("类卸载数：  " + bean.getUnloadedClassCount()).append(spliteStr);
        return stringBuilder.toString();
    }


    /**
     * 类编译信息
     *
     * @param spliteStr
     * @return
     * @author xuliang
     * @QQ 359135103 2019年12月24日 下午3:39:22
     */
    public static String compilationInfo(String spliteStr) {
        CompilationMXBean bean = ManagementFactory.getCompilationMXBean();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("编译器名称：   " + bean.getName()).append(spliteStr);
        stringBuilder.append("编译耗时：     " + bean.getTotalCompilationTime()).append(spliteStr);
        stringBuilder.append("是否支持编译监视：    " + bean.isCompilationTimeMonitoringSupported()).append(spliteStr);
        return stringBuilder.toString();
    }

    /**
     * 垃圾回收信息
     *
     * @param spliteStr
     * @return
     * @author xuliang
     * @QQ 359135103 2019年12月25日 下午3:39:22
     */

    public static String collectionInfo(String spliteStr) {
        List<GarbageCollectorMXBean> beans = ManagementFactory.getGarbageCollectorMXBeans();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < beans.size(); i++) {
            GarbageCollectorMXBean garbageCollectorMXBean = beans.get(i);
            stringBuilder.append(i).append("垃圾回收次数：    ").append(garbageCollectorMXBean.getCollectionCount()).append(spliteStr);
            stringBuilder.append(i).append("垃圾回收累积时间：  ").append(garbageCollectorMXBean.getCollectionTime()).append(spliteStr);
            stringBuilder.append(i).append("垃圾回收对象名称：     ").append(garbageCollectorMXBean.getObjectName()).append(spliteStr);
        }

        return stringBuilder.toString();
    }

    /**
     * 内存信息
     *
     * @param spliteStr
     * @return
     * @author xuliang
     * @QQ 359135103 2019年12月25日 下午3:39:22
     */

    public static String memoryInfo(String spliteStr) {

        MemoryMXBean bean = ManagementFactory.getMemoryMXBean();
        StringBuilder sb = new StringBuilder();
        sb.append("堆内存使用：    ").append(bean.getHeapMemoryUsage()).append(spliteStr);
        sb.append("栈内存使用：    ").append(bean.getNonHeapMemoryUsage()).append(spliteStr);
        sb.append("挂起对象数：    ").append(bean.getObjectPendingFinalizationCount()).append(spliteStr);
        //内存池
        List<MemoryPoolMXBean> beans = ManagementFactory.getMemoryPoolMXBeans();
        for (int i = 0; i < beans.size(); i++) {
            MemoryPoolMXBean b = beans.get(i);
//			sb.append(i).append("垃圾回收后内存:   ").append(b.getCollectionUsage()).append(spliteStr);
//			sb.append(i).append("内存池的回收使用量阈值:   ").append(b.getCollectionUsageThreshold()).append(spliteStr);
//			sb.append(i).append("虚拟机已检测到内存使用量达到或超过回收使用量阈值的次数:   ").append(b.getCollectionUsageThresholdCount()).append(spliteStr);
            sb.append(i).append("内存池管理器名称:   ").append(b.getMemoryManagerNames().toString()).append(spliteStr);
            sb.append(i).append("内存池名称:   ").append(b.getName()).append(spliteStr);
            sb.append(i).append("内存使用峰值:   ").append(b.getPeakUsage()).append(spliteStr);
            sb.append(i).append("内存池类型:   ").append(b.getType()).append(spliteStr);
            sb.append(i).append("内存池使用量:   ").append(b.getUsage()).append(spliteStr);
            sb.append(i).append("内存使用量阀值:   ").append(b.getUsageThreshold()).append(spliteStr);
            sb.append(i).append("超过阀值次数:   ").append(b.getUsageThresholdCount()).append(spliteStr);
        }
        return sb.toString();

    }


    /**
     * 操作系统信息
     *
     * @param spliteStr
     * @return
     * @author xuliang
     * @QQ 359135103
     * 2019年12月25日 下午5:21:19
     */


    public static String osInfo(String spliteStr) {

        OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
        StringBuilder sb = new StringBuilder();
        sb.append("操作系统架构:   ").append(bean.getArch()).append(spliteStr);
        sb.append("可使用的cpu数量:   ").append(bean.getAvailableProcessors()).append(spliteStr);
        sb.append("操作系统名称:   ").append(bean.getName()).append(spliteStr);
        sb.append("1分钟cpu消耗平均值:   ").append(bean.getSystemLoadAverage()).append(spliteStr);
        sb.append("操作系统版本:   ").append(bean.getVersion()).append(spliteStr);
        return sb.toString();

    }

    /**
     * 线程信息
     *
     * @param spliteStr
     * @return
     * @author xulaing
     * @QQ 359135103
     * 2019年12月12日 下午5:21:19
     */
    public static String threadInfo(String spliteStr) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        StringBuilder sb = new StringBuilder();
        ThreadInfo[] threads = bean.dumpAllThreads(false, false);
        for (ThreadInfo thread : threads) {
            sb.append("线程信息:   ").append(thread).append(spliteStr);
        }
        sb.append("活动守护线程数目:   ").append(bean.getDaemonThreadCount()).append(spliteStr);
        sb.append("峰值线程数:   ").append(bean.getPeakThreadCount()).append(spliteStr);
        sb.append("当前线程数:   ").append(bean.getThreadCount()).append(spliteStr);
        sb.append("总启用线程数:   ").append(bean.getTotalStartedThreadCount()).append(spliteStr);
        return sb.toString();
    }


    /**
     * 虚拟机信息
     *
     * @param spliteStr 分隔符
     * @return
     * @author JiangZhiYong
     * @QQ 359135103 2017年10月12日 下午2:18:01
     */
    public static String jvmInfo(String spliteStr) {
        StringBuilder sb = new StringBuilder();
        InetAddress addr = null;
        String ip = "";
        try {
            addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
        } catch (UnknownHostException e) {
            log.error("虚拟机地址", e);
        }
        Properties props = System.getProperties();
        Map<String, String> map = System.getenv();
        sb.append("用户名:    " + map.get("USERNAME")).append(spliteStr);
        sb.append("计算机名:    " + map.get("COMPUTERNAME")).append(spliteStr);
        sb.append("计算机域名:    " + map.get("USERDOMAIN")).append(spliteStr);
        sb.append("本地ip地址:    " + ip).append(spliteStr);
        sb.append("本地主机名:    " + addr.getHostName()).append(spliteStr);
        sb.append("JVM可以使用的总内存:    " + totalMemory()).append(spliteStr);
        sb.append("JVM可以使用的剩余内存:    " + freeMemory()).append(spliteStr);
        sb.append("JVM可以使用的处理器个数:    " + availableProcessors()).append(spliteStr);
        sb.append("Java的运行环境版本：    " + props.getProperty("java.version")).append(spliteStr);
        sb.append("Java的运行环境供应商：    " + props.getProperty("java.vendor")).append(spliteStr);
        sb.append("Java供应商的URL：    " + props.getProperty("java.vendor.url")).append(spliteStr);
        sb.append("Java的安装路径：    " + props.getProperty("java.home")).append(spliteStr);
        sb.append("Java的虚拟机规范版本：    " + props.getProperty("java.vm.specification.version")).append(spliteStr);
        sb.append("Java的虚拟机规范供应商：    " + props.getProperty("java.vm.specification.vendor")).append(spliteStr);
        sb.append("Java的虚拟机规范名称：    " + props.getProperty("java.vm.specification.name")).append(spliteStr);
        sb.append("Java的虚拟机实现版本：    " + props.getProperty("java.vm.version")).append(spliteStr);
        sb.append("Java的虚拟机实现供应商：    " + props.getProperty("java.vm.vendor")).append(spliteStr);
        sb.append("Java的虚拟机实现名称：    " + props.getProperty("java.vm.name")).append(spliteStr);
        sb.append("Java运行时环境规范版本：    " + props.getProperty("java.specification.version")).append(spliteStr);
        sb.append("Java运行时环境规范供应商：    " + props.getProperty("java.specification.vender")).append(spliteStr);
        sb.append("Java运行时环境规范名称：    " + props.getProperty("java.specification.name")).append(spliteStr);
        sb.append("Java的类格式版本号：    " + props.getProperty("java.class.version")).append(spliteStr);
//		sb.append("Java的类路径：    " + props.getProperty("java.class.path")).append(spliteStr);
        sb.append("加载库时搜索的路径列表：    " + props.getProperty("java.library.path")).append(spliteStr);
        sb.append("默认的临时文件路径：    " + props.getProperty("java.io.tmpdir")).append(spliteStr);
        sb.append("一个或多个扩展目录的路径：    " + props.getProperty("java.ext.dirs")).append(spliteStr);
        sb.append("操作系统的名称：    " + props.getProperty("os.name")).append(spliteStr);
        sb.append("操作系统的构架：    " + props.getProperty("os.arch")).append(spliteStr);
        sb.append("操作系统的版本：    " + props.getProperty("os.version")).append(spliteStr);
        sb.append("文件分隔符：    " + props.getProperty("file.separator")).append(spliteStr);
        sb.append("路径分隔符：    " + props.getProperty("path.separator")).append(spliteStr);
        sb.append("行分隔符：    " + props.getProperty("line.separator")).append(spliteStr);
        sb.append("用户的账户名称：    " + props.getProperty("user.name")).append(spliteStr);
        sb.append("用户的主目录：    " + props.getProperty("user.home")).append(spliteStr);
        sb.append("用户的当前工作目录：    " + props.getProperty("user.dir")).append(spliteStr);
        sb.append(classLoadInfo(spliteStr));
        sb.append(compilationInfo(spliteStr));
        sb.append(collectionInfo(spliteStr));
        sb.append(memoryInfo(spliteStr));
        sb.append(osInfo(spliteStr));
//		sb.append(threadInfo(spliteStr));

        return sb.toString();
    }


}