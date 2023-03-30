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

    @Override
    public int available() throws IOException {
        try {
            return serialPort.getInputBufferBytesCount();
        } catch (SerialPortException e) {
            throw new IOException("Error getting available bytes count", e);
        }
    }
}
