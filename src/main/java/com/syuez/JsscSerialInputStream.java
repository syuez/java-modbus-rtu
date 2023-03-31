package com.syuez;

import jssc.SerialPort;
import jssc.SerialPortException;

import java.io.IOException;
import java.io.InputStream;

public class JsscSerialInputStream extends InputStream {
    private final SerialPort serialPort;

    public JsscSerialInputStream(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    /**
     * 读取下一个字节的数据，如果没有则返回 -1
     */
    @Override
    public int read() throws IOException {
        try {
            byte[] buffer = serialPort.readBytes(1);
            if (buffer != null && buffer.length > 0) {
                return buffer[0] & 0xFF;
            } else {
                return -1;
            }
        } catch (SerialPortException e) {
            throw new IOException("Error reading from serial port", e);
        }
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    /**
     * 从第 off 位置读取【最多(实际可能小于)】 len 长度字节的数据放到 byte 数组中，流是以 -1 来判断是否读取结束的; 此方法会一直阻止，
     * 直到输入数据可用、检测到stream结尾或引发异常为止
     * @param b     the buffer into which the data is read.
     * @param off   the start offset in array {@code b}
     *                   at which the data is written.
     * @param len   the maximum number of bytes to read.
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        try {
            int availableBytes = available();
            if (availableBytes <= 0) {
                return -1;
            }

            byte[] buffer = serialPort.readBytes(Math.min(len, availableBytes));
            if (buffer != null && buffer.length > 0) {
                System.arraycopy(buffer, 0, b, off, buffer.length);
                return buffer.length;
            } else {
                return -1;
            }
        } catch (SerialPortException e) {
            throw new IOException("Error reading from serial port", e);
        }
    }

    /**
     * 返回可读的字节数量
     */
    @Override
    public int available() throws IOException {
        try {
            return serialPort.getInputBufferBytesCount();
        } catch (SerialPortException e) {
            throw new IOException("Error getting available bytes count", e);
        }
    }
}
