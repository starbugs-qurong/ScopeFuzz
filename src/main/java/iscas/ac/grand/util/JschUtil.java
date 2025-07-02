package iscas.ac.grand.util;
/**
 * Author qurong
 * create time 2019/9/23
 */


import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class JschUtil {
    private String charset = "UTF-8"; // 设置编码格式
    private String user; // 用户名
    private String passwd; // 登录密码
    private String host; // 主机IP
    private int port; //端口
    private JSch jsch;
    private Session session;
    static String PATHSEPARATOR = "/";


    public JschUtil(String user, String passwd, String host ,int port ) {
        this.user = user;
        this.passwd = passwd;
        this.host = host;
        this.port = port;
    }

    /**
     *默认连接
     */
    public static JschUtil getConfig(){
        String username = "qurong";
        String password = "ZzBFVoyIzOu0hnHd";
//        String host = "124.16.137.61";
        String host = "192.168.5.61";
        int port = 22;
        return new JschUtil(username, password, host, port);
    }


    /**
     * 连接到指定的IP
     *
     * @throws JSchException
     */
    public void connect() throws JSchException {
        jsch = new JSch();
        session = jsch.getSession(user, host, port);
        session.setPassword(passwd);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
    }
    /**
     * 关闭连接
     */
    public void disconnect() throws Exception{
        if(session != null && session.isConnected()){
            session.disconnect();
        }
    }
    /**
     * 执行一条命令
     */
    public String execCmd(String command) throws Exception{
        String result="";
        BufferedReader reader = null;
        Channel channel = null;

        channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(System.err);
        channel.connect();
        InputStream in = channel.getInputStream();
        reader = new BufferedReader(new InputStreamReader(in,
                Charset.forName(charset)));
        String buf = null;
        while ((buf = reader.readLine()) != null) {
            result+=buf;
            //System.out.println(buf);
        }
        channel.disconnect();
        return result;
    }
    
    /**
     * 在某个目录下执行一条命令
     */
    public String execCmd(String path,String command) throws Exception{
        String result="";
        BufferedReader reader = null;
        Channel channel = null;
        channel = session.openChannel("exec");
        ((ChannelSftp) channel).cd(path);
        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(System.err);
        channel.connect();
        InputStream in = channel.getInputStream();
        reader = new BufferedReader(new InputStreamReader(in,
                Charset.forName(charset)));
        String buf = null;
        while ((buf = reader.readLine()) != null) {
            result+=buf;
            //System.out.println(buf);
        }
        channel.disconnect();
        return result;
    }

    /**
     * 执行一条命令返回终端输出列表
     */
    public List<String> getExecCmdRst(String command) throws Exception{
        List<String> result=new ArrayList<>();
        BufferedReader reader = null;
        Channel channel = null;

        channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(System.err);
        channel.connect();
        InputStream in = channel.getInputStream();
        reader = new BufferedReader(new InputStreamReader(in,
                Charset.forName(charset)));
        String buf = null;
        while ((buf = reader.readLine()) != null) {
            //System.out.println(buf);
            result.add(buf);
        }
        channel.disconnect();
        return result;
    }

    /**
     * 执行相关的命令
     */
    public void execCmd() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String command = "";
        BufferedReader reader = null;
        Channel channel = null;

        try {
            while ((command = br.readLine()) != null) {
                channel = session.openChannel("exec");
                ((ChannelExec) channel).setCommand(command);
                channel.setInputStream(null);
                ((ChannelExec) channel).setErrStream(System.err);

                channel.connect();
                InputStream in = channel.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in,
                        Charset.forName(charset)));
                String buf = null;
                while ((buf = reader.readLine()) != null) {
                    //System.out.println(buf);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            channel.disconnect();
        }
    }
    /**
     * 上传文件
     */
    public void uploadFile(String local,String remote) throws Exception {
        ChannelSftp channel = null;
        InputStream inputStream = null;
        try {
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect(5000);
            inputStream = new FileInputStream(new File(local));
            channel.setInputStream(inputStream);
            channel.put(inputStream, remote, null);
        } catch (Exception e) {
            throw e;
        }finally{
            if(channel != null){
                channel.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
    }
    
    /**
     * 上传文件夹
     */
    public void uploadDir(String local,String remote) throws Exception {
        ChannelSftp channel = null;
        InputStream inputStream = null;
        try {
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            //channel.mkdir(remote);
            uploadDirectory(channel, local, remote);
        } catch (Exception e) {
            throw e;
        }finally{
            if(channel != null){
                channel.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
    }
    /**
     * qurong
     * 2024.4.17
     * 递归的上传文件夹中的文件
     * @param channel
     * @param localDirectory
     * @param remoteDirectory
     * @throws SftpException
     */
    private static void uploadDirectory(ChannelSftp channel, String localDirectory, String remoteDirectory) throws SftpException {
        File localFile = new File(localDirectory);
        if (localFile.isDirectory()) {
            channel.cd(remoteDirectory);
            File[] files = localFile.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    channel.put(file.getAbsolutePath(), file.getName());
                } else if (file.isDirectory()) {
                    String newRemoteDirectory = remoteDirectory + "/" + file.getName();
                    channel.mkdir(newRemoteDirectory);
                    channel.cd(newRemoteDirectory);
                    uploadDirectory(channel, file.getAbsolutePath(), newRemoteDirectory);
                    channel.cd("..");
                }
            }
        }
    }

    /**
     * 下载文件
     */
    public void downloadFile(String remote,String local) throws Exception{
        ChannelSftp channel = null;
        OutputStream outputStream = null;
        try {
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect(5000);
            outputStream = new FileOutputStream(new File(local));
            channel.get(remote, outputStream, null);
            outputStream.flush();
        } catch (Exception e) {
            throw e;
        }finally{
            if( channel != null){
                channel.disconnect();
            }
            if(outputStream != null){
                outputStream.close();
            }
        }
    }

    /**
     * This method is called recursively to download the folder content from SFTP server
     *
     * @param sourcePath
     * @param destinationPath
     * @throws SftpException
     */
    @SuppressWarnings("unchecked")
    public  void recursiveFolderDownload(String sourcePath, String destinationPath) throws SftpException {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect(5000);
        } catch (JSchException e) {
            e.printStackTrace();
        }
        Vector<ChannelSftp.LsEntry> fileAndFolderList = channelSftp.ls(sourcePath); // Let list of folder content

        //Iterate through list of folder content
        for (ChannelSftp.LsEntry item : fileAndFolderList) {

            if (!item.getAttrs().isDir()) { // Check if it is a file (not a directory).
                if (!(new File(destinationPath + PATHSEPARATOR + item.getFilename())).exists()
                        || (item.getAttrs().getMTime() > Long
                        .valueOf(new File(destinationPath + PATHSEPARATOR + item.getFilename()).lastModified()
                                / (long) 1000)
                        .intValue())) { // Download only if changed later.

                    new File(destinationPath + PATHSEPARATOR + item.getFilename());
                    channelSftp.get(sourcePath + PATHSEPARATOR + item.getFilename(),
                            destinationPath + PATHSEPARATOR + item.getFilename()); // Download file from source (source filename, destination filename).
                }
            } else if (!(".".equals(item.getFilename()) || "..".equals(item.getFilename()))) {
                new File(destinationPath + PATHSEPARATOR + item.getFilename()).mkdirs(); // Empty folder copy.
                recursiveFolderDownload(sourcePath + PATHSEPARATOR + item.getFilename(),
                        destinationPath + PATHSEPARATOR + item.getFilename()); // Enter found folder on server to read its contents and create locally.
            }
        }
    }

}
