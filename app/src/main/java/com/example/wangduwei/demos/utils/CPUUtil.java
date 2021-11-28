package com.example.wangduwei.demos.utils;


import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class CPUUtil {
    private static final int BUFFER_SIZE = 128;

    // 获取CPU的最大频率，单位KHZ
    public static String getMaxCpuFreq() {
        StringBuilder builder = new StringBuilder();
        InputStream in = null;

        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            ProcessBuilder cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            in = process.getInputStream();

            int read;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((read = in.read(buffer)) != -1) {
                builder.append(new String(buffer, 0, read));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            builder.delete(0, builder.length());
            builder.append("N/A");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return builder.toString().trim();
    }

    // 获取CPU最小频率（单位KHZ）
    public static String getMinCpuFreq() {
        StringBuilder builder = new StringBuilder();
        InputStream in = null;

        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"};
            ProcessBuilder cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            in = process.getInputStream();

            int read;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((read = in.read(buffer)) != -1) {
                builder.append(new String(buffer, 0, read));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            builder.delete(0, builder.length());
            builder.append("N/A");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return builder.toString().trim();
    }

    // 实时获取CPU当前频率（单位KHZ）
    public static String getCurCpuFreq() {
        String result = "N/A";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(
                    "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq"));
            String text = br.readLine();
            result = text.trim();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    // 获取CPU名字
    public static String getCpuName() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("/proc/cpuinfo"));
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static int getHeart() {

        int cores;
        try {
            cores = new File("/sys/devices/system/cpu/").listFiles(CPU_FILTER).length;
        } catch (SecurityException e) {
            cores = 0;
        }
        return cores;
    }

    private static final FileFilter CPU_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return Pattern.matches("cpu[0-9]", pathname.getName());
//            String path = pathname.getName();
//            //regex is slow, so checking char by char.
//            if (path.startsWith("cpu")) {
//                for (int i = 3; i < path.length(); i++) {
//                    if (path.charAt(i) < '0' || path.charAt(i) > '9') {
//                        return false;
//                    }
//                }
//                return true;
//            }
//            return false;
        }
    };

    public static String getCpuTemp() {
        String temp = null;
        try {
            FileReader fr = new FileReader("/sys/class/thermal/thermal_zone9/subsystem/thermal_zone9/temp");
            BufferedReader br = new BufferedReader(fr);
            temp = br.readLine();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static String putCpuAbi() {
        String[] abis;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            abis = Build.SUPPORTED_ABIS;
        } else {
            abis = new String[]{Build.CPU_ABI, Build.CPU_ABI2};
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String abi : abis) {
            stringBuilder.append(abi);
            stringBuilder.append(",");
        }

        try {
            return stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
