package com.syuez;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsRequest;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsResponse;
import com.serotonin.modbus4j.serial.SerialPortWrapper;

import java.util.logging.Logger;

public class ModbusRTUExample
{

    private static final Logger logger = Logger.getLogger(ModbusRTUExample.class.getName());
    public static void main( String[] args )
    {
        ModbusFactory modbusFactory = new ModbusFactory();

        String commPortId = "COM3";
        int baudRate = 9600;
        int dataBits = 8;
        int stopBits = 1;
        int parity = 0;

        // Create a JsscSerialPortWrapper instance
        SerialPortWrapper serialPortWrapper = new JsscSerialPortWrapper(commPortId, baudRate, dataBits, stopBits, parity);

        // Create a Modbus RTU master
        ModbusMaster modbusMaster = modbusFactory.createRtuMaster(serialPortWrapper);

        // Set the timeout for the Modbus RTU master
        modbusMaster.setTimeout(5000); // Set the timeout to 5000 ms
        modbusMaster.setRetries(0); // Set the number of retries to 0

        // Initialize the Modbus RTU master
        try {
            modbusMaster.init();
            logger.info("Modbus RTU master initialized successfully");
        } catch (ModbusInitException e) {
            logger.warning("Error initializing Modbus RTU master: " + e.getMessage());
        }

        try {
            ReadDiscreteInputsRequest request = new ReadDiscreteInputsRequest(5,11,1);
            ReadDiscreteInputsResponse response = (ReadDiscreteInputsResponse) modbusMaster.send(request);
            System.out.println(byteArrayToInt(response.getData()));
        } catch (ModbusTransportException e) {
            logger.warning("Error while reading discrete inputs: " + e.getMessage());
        }

        modbusMaster.destroy();
    }

    public static int byteArrayToInt(byte[] bytes) {
        int result = 0;
        for (byte b : bytes) {
            result = (result << 8) | (b & 0xFF);
        }
        return result;
    }
}
