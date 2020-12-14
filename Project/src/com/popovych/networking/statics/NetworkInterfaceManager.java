package com.popovych.networking.statics;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class NetworkInterfaceManager {
    static public List<NetworkInterface> getAllActiveNetworkInterfaces() throws SocketException {
        ArrayList<NetworkInterface> activeInterfaces = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface Interface = interfaces.nextElement();
            if (Interface.isUp())
                activeInterfaces.add(Interface);
        }
        activeInterfaces.trimToSize();
        return activeInterfaces;
    }

    static public List<NetworkInterface> getAllNetworkInterfaces() throws SocketException {
        return Collections.list(NetworkInterface.getNetworkInterfaces());
    }
}
