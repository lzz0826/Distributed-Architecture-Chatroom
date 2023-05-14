package org.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class IdGenerator {
    private static final Logger logger = LoggerFactory.getLogger(IdGenerator.class);

    //工作機器 id
    private final long workerId;
    //數據中心 id
    private final long datacenterId;
    //序列號
    private long sequence = 0L;

    //基準時間，一般取系統的最近時間（一旦確定不能變動）
    private final long twepoch;

    private final long workerIdBits;
    private final long datacenterIdBits;
    private final long maxWorkerId;
    private final long maxDatacenterId;

    //毫秒內自增位數
    private final long sequenceBits;
    //位與運算保證毫秒內 Id 範圍
    private final long sequenceMask;

    //工作機器 id 需要左移的位數
    private final long workerIdShift;
    //數據中心 id 需要左移位數
    private final long datacenterIdShift;
    //時間戳需要左移位數
    private final long timestampLeftShift;

    //上次生成 id 的時間戳，初始值為負數
    private long lastTimestamp = -1L;

    //true 表示毫秒內初始序列採用隨機值
    private final boolean randomSequence;
    //隨機初始序列計數器
    private long count = 0L;

    //允許時鐘回撥的毫秒數
    private final long timeOffset;

    private final ThreadLocalRandom tlr = ThreadLocalRandom.current();

    /**
     * 無參構造器，自動生成 workerId/datacenterId
     */
    public IdGenerator() {
        this(false, 10, null, 5L, 5L, 12L);
    }

    /**
     * 有參構造器,調用者自行保證數據中心 ID+機器 ID 的唯一性
     * 標準 snowflake 實現
     *
     * @param workerId     工作機器 ID
     * @param datacenterId 數據中心 ID
     */
    public IdGenerator(long workerId, long datacenterId) {
        this(workerId, datacenterId, false, 10, null, 5L, 5L, 12L);
    }

    /**
     * @param randomSequence   true 表示每毫秒內起始序號使用隨機值
     * @param timeOffset       允許時間回撥的毫秒數
     * @param epochDate        基準時間
     * @param workerIdBits     workerId 位數
     * @param datacenterIdBits datacenterId 位數
     * @param sequenceBits     sequence 位數
     */
    public IdGenerator(boolean randomSequence, long timeOffset, Date epochDate, long workerIdBits, long datacenterIdBits, long sequenceBits) {
        if (null != epochDate) {
            this.twepoch = epochDate.getTime();
        } else {
            // 2012/12/12 23:59:59 GMT
            this.twepoch = 1355327999000L;
        }

        this.workerIdBits = workerIdBits;
        this.datacenterIdBits = datacenterIdBits;
        this.maxWorkerId = ~(-1L << workerIdBits);
        this.maxDatacenterId = ~(-1L << datacenterIdBits);

        this.sequenceBits = sequenceBits;
        this.sequenceMask = ~(-1L << sequenceBits);

        this.workerIdShift = sequenceBits;
        this.datacenterIdShift = sequenceBits + workerIdBits;
        this.timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

        this.datacenterId = getDatacenterId(maxDatacenterId);
        this.workerId = getMaxWorkerId(datacenterId, maxWorkerId);
        this.randomSequence = randomSequence;
        this.timeOffset = timeOffset;
        String initialInfo = String.format("worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d, datacenterid  %d, workerid %d",
                timestampLeftShift, datacenterIdBits, workerIdBits, sequenceBits, datacenterId, workerId);
        logger.info(initialInfo);
    }

    /**
     * 自定義 workerId+datacenterId+其它初始配置
     * 調整 workerId、datacenterId、sequence 位數定製雪花算法,控制生成的 Id 的位數
     *
     * @param workerId         工作機器 ID
     * @param datacenterId     數據中心 ID
     * @param randomSequence   true 表示每毫秒內起始序號使用隨機值
     * @param timeOffset       允許時間回撥的毫秒數
     * @param epochDate        基準時間
     * @param workerIdBits     workerId 位數
     * @param datacenterIdBits datacenterId 位數
     * @param sequenceBits     sequence 位數
     */
    public IdGenerator(long workerId, long datacenterId, boolean randomSequence, long timeOffset, Date epochDate, long workerIdBits, long datacenterIdBits, long sequenceBits) {
        this.workerIdBits = workerIdBits;
        this.datacenterIdBits = datacenterIdBits;
        this.maxWorkerId = ~(-1L << workerIdBits);
        this.maxDatacenterId = ~(-1L << datacenterIdBits);

        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0\r\n", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0\r\n", maxDatacenterId));
        }

        if (null != epochDate) {
            this.twepoch = epochDate.getTime();
        } else {
            // 2012/12/12 23:59:59 GMT
            this.twepoch = 1355327999000L;
        }

        this.sequenceBits = sequenceBits;
        this.sequenceMask = ~(-1L << sequenceBits);

        this.workerIdShift = sequenceBits;
        this.datacenterIdShift = sequenceBits + workerIdBits;
        this.timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.timeOffset = timeOffset;
        this.randomSequence = randomSequence;

        String initialInfo = String.format("worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d, datacenterid  %d, workerid %d",
                timestampLeftShift, datacenterIdBits, workerIdBits, sequenceBits, datacenterId, workerId);
        logger.info(initialInfo);
    }

    private static long getDatacenterId(long maxDatacenterId) {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                if (null != mac) {
                    id = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                    id = id % (maxDatacenterId + 1);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("GetDatacenterId Exception", e);
        }
        return id;
    }

    private static long getMaxWorkerId(long datacenterId, long maxWorkerId) {
        StringBuilder macIpPid = new StringBuilder();
        macIpPid.append(datacenterId);
        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            if (name != null && !name.isEmpty()) {
                //GET jvmPid
                macIpPid.append(name.split("@")[0]);
            }
            //GET hostIpAddress
            String hostIp = InetAddress.getLocalHost().getHostAddress();
            String ipStr = hostIp.replaceAll("\\.", "");
            macIpPid.append(ipStr);
        } catch (Exception e) {
            throw new RuntimeException("GetMaxWorkerId Exception", e);
        }
        //MAC + PID + IP 的 hashcode 取低 16 位
        return (macIpPid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }

    public synchronized long nextId() {
        long currentTimestamp = timeGen();

        //獲取當前時間戳如果小於上次時間戳，則表示時間戳獲取出現異常
        if (currentTimestamp < lastTimestamp) {
            // 校驗時間偏移回撥量
            long offset = lastTimestamp - currentTimestamp;
            if (offset > timeOffset) {
                throw new RuntimeException("Clock moved backwards, refusing to generate id for [" + offset + "ms]");
            }

            try {
                // 時間回退 timeOffset 毫秒內，則允許等待 2 倍的偏移量后重新獲取，解決小範圍的時間回撥問題
                this.wait(offset << 1);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            currentTimestamp = timeGen();
            if (currentTimestamp < lastTimestamp) {
                throw new RuntimeException("Clock moved backwards, refusing to generate id for [" + offset + "ms]");
            }
        }

        //如果獲取的當前時間戳等於上次時間戳（即同一毫秒內），則序列號自增
        if (lastTimestamp == currentTimestamp) {
            // randomSequence 為 true 表示隨機生成允許範圍內的起始序列,否則毫秒內起始值從 0L 開始自增
            long tempSequence = sequence + 1;
            if (randomSequence) {
                sequence = tempSequence & sequenceMask;
                count = (count + 1) & sequenceMask;
                if (count == 0) {
                    currentTimestamp = this.tillNextMillis(lastTimestamp);
                }
            } else {
                sequence = tempSequence & sequenceMask;
                if (sequence == 0) {
                    currentTimestamp = this.tillNextMillis(lastTimestamp);
                }
            }
        } else {
            sequence = randomSequence ? tlr.nextLong(sequenceMask + 1) : 0L;
            count = 0L;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - twepoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

    private long tillNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
}
