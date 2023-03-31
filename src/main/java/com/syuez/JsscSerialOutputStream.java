package com.syuez;

import jssc.SerialPort;
import jssc.SerialPortException;

import java.io.IOException;
import java.io.OutputStream;

public class JsscSerialOutputStream extends OutputStream {
    private final SerialPort serialPort;

    public JsscSerialOutputStream(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    /**
     * 写入一个字节，可以看到这里的参数是一个 int 类型，对应之前的读方法，int 类型的 32 位，只有低 8 位才写入，高 24 位将舍弃
     */
    @Override
    public void write(int b) throws IOException {
        try {
            serialPort.writeByte((byte) b);
        } catch (SerialPortException e) {
            throw new IOException("Error writing to serial port", e);
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);

    }
    /**
     * 将 byte 数组从 off 位置开始，len 长度的字节写入
     * @param b     the data.
     * @param off   the start offset in the data.
     * @param len   the number of bytes to write.
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }

        byte[] buffer = new byte[len];
        System.arraycopy(b, off, buffer, 0, len);

        try {
            serialPort.writeBytes(buffer);
        } catch (SerialPortException e) {
            throw new IOException("Error writing to serial port", e);
        }
    }
}
