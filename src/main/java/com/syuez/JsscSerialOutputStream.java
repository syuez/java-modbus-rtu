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

    @Override
    public void write(int b) throws IOException {
        try {
            serialPort.writeByte((byte) b);
        } catch (SerialPortException e) {
            throw new IOException("Error writing to serial port", e);
        }
    }

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
