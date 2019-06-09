package com.example.autobuild.demo.util;

import org.openstack4j.model.compute.Address;
import org.openstack4j.model.compute.Addresses;
import org.openstack4j.model.compute.Server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GetVmAddressUtil {
    public static List<String> getVmAddress(Server server) {
        List<String> res = new ArrayList<>();
        Addresses addresses = server.getAddresses();
        Map<String, List<? extends Address>> map = addresses.getAddresses();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iterator.next();
            List<? extends Address> list = (List<? extends Address>) entry.getValue();
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Address address = list.get(i);
                    res.add(address.getAddr());
                    //System.out.println(System.currentTimeMillis() + " ip地址 ：" + address.getAddr() + " 虚拟机名 ：" + server.getName());
                }
            }
        }
        return res;
    }
}
