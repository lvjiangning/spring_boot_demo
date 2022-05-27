package com.rihao.property.util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

/**
 * linux或windows命令执行
 *
 * @author yhmsi
 */
public class CommandExecute {

    private static Logger logger = LoggerFactory.getLogger(CommandExecute.class);

    /**
     * 1.java执行shell /exe 脚本时会创建一个子进程，子进程创建后会和主进程分别独立运行，
     * 2.主进程调用Process.waitfor等待子进程完成，而主进程调用Process.waitfor后已挂起。当前子进程和主进程之间的缓冲区塞满后，子进程不能继续写数据，然后也会挂起
     * 3.两个进程相互等待，最终导致死锁
     *
     * @param command
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Deprecated
    public static String executeCommand(String command) throws IOException, InterruptedException {
        StringBuffer output = new StringBuffer();
        Process p;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            p = Runtime.getRuntime().exec(command);
            logger.info("执行命令完成：" + command);
            p.waitFor();
            logger.info("从内存中读取字符流：");
            inputStreamReader = new InputStreamReader(p.getInputStream(), "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            logger.info("文件写入完成：" + command);
        } catch (IOException e) {
            logger.info("IO异常：" + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (InterruptedException e) {
            logger.info("线程终止异常：" + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(inputStreamReader);
            logger.info("全部执行完毕：关闭流！");
        }
        logger.info(output.toString());
        return output.toString();
    }

    /**
     * 运行一个外部命令，返回状态.若超过指定的超时时间，抛出TimeoutException
     */
    public static String execute(final long timeout, final String command) throws IOException, InterruptedException, TimeoutException {

        Process process = Runtime.getRuntime().exec(command);

        Worker worker = new Worker(process);
        worker.start();
        ProcessStatus ps = worker.getProcessStatus();

        try {
            worker.join(timeout);
            logger.info("执行命令完成：" + command);
            if (ps.exitCode != null) {
                logger.info("命令执行结果：" + ps.exitCode);
                logger.info("命令返回报文：" + ps.output);
                return ps.output;
            } else {
                worker.interrupt();
                throw new TimeoutException("执行命令超时，请重试！");
            }
        } catch (InterruptedException e) {
            logger.info("线程终止异常：" + e.getMessage());
            worker.interrupt();
            throw e;
        } finally {
            process.destroy();
        }
    }


    private static class Worker extends Thread {
        private final Process process;
        private ProcessStatus ps;

        private Worker(Process process) {
            this.process = process;
            this.ps = new ProcessStatus();
        }

        @Override
        public void run() {
            InputStream is = null;
            try {
                is = process.getInputStream();
                ps.output = IOUtils.toString(is);
                ps.exitCode = process.waitFor();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException ignore) {
            } finally {
                IOUtils.closeQuietly(is);
            }
        }

        public ProcessStatus getProcessStatus() {
            return this.ps;
        }
    }

    public static class ProcessStatus {
        public volatile Integer exitCode;
        public volatile String output;
    }
}
