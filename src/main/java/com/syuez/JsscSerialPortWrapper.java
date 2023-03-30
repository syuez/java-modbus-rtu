package com.syuez;

import com.serotonin.modbus4j.serial.SerialPortWrapper;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.io.InputStream;
import java.io.OutputStream;

public class JsscSerialPortWrapper implements SerialPortWrapper {

    private SerialPort serialPort;
    private final String commPortId;
    private final int baudRate;
    private final int dataBits;
    private final int stopBits;
    private final int parity;

    public JsscSerialPortWrapper(String commPortId, int baudRate, int dataBits, int stopBits, int parity) {
        this.commPortId = commPortId;
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
    }

    @Override
    public void close() throws Exception {
        if(serialPort != null) {
            try {
                serialPort.closePort();
            } catch (SerialPortException e) {
                throw new Exception("Error closing serial port", e);
            }
        }
    }

    @Override
    public void open() throws Exception {
        serialPort = new SerialPort(commPortId);
        try {
            serialPort.openPort();
            serialPort.setParams(baudRate, dataBits, stopBits, parity);
        } catch (SerialPortException e) {
            throw new Exception("Error opening serial port", e);
        }
    }

    @Override
    public InputStream getInputStream() {
        return new JsscSerialInputStream(serialPort);
    }

    @Override
    public OutputStream getOutputStream() {
        return new JsscSerialOutputStream(serialPort);
    }

    @Override
    public int getBaudRate() {
        return baudRate;
    }

    @Override
    public int getFlowControlIn() {
        return 0;
    }

    @Override
    public int getFlowControlOut() {
        return 0;
    }

    @Override
    public int getDataBits() {
        return dataBits;
    }

    @Override
    public int getStopBits() {
        return stopBits;
    }

    @Override
    public int getParity() {
        return parity;
    }
}
