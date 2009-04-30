
/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.net.InterfaceAddress;
import java.util.List;

/**
 *
 * @author aymeric
 */
public class Operation_IP {

    private static int IPV4LENGTH = 32,  IPV6LENGTH = 128;
    private static Enumeration<NetworkInterface> if_list;
    private static int nb_if;
    private static Object[][] if_addresses;

    // --------------------- SETUP ENVIRONNEMENT METHODS -----------------------
    /**
     * This method retrives the list of network interface on the host computer, including loopback.
     * @throws java.net.SocketException
     */
    public static void getNetworkIf() throws SocketException {
        if_list = NetworkInterface.getNetworkInterfaces();
    }

    /**
     * This method retrives the number of network interfaces
     */
    public static void getNetworkCount() throws SocketException {
        getNetworkIf();
        nb_if = 0;
        while (if_list.hasMoreElements()) {
            nb_if++;
            if_list.nextElement();
        }
    }

    /**
     * This method permit to get all network interfaces in a two dimensions array structure.
     * First element is the NetworkInterface object and second a List<InterfaceAddresses> representing
     * whole Ip addresses attached to it.
     *
     */
    public static void getNetworkAddresses() throws SocketException {
        getNetworkIf();
        if_addresses = new Object[nb_if][2];
        int counter = 0;
        while (if_list.hasMoreElements()) {
            NetworkInterface inet_if = if_list.nextElement();
            if_addresses[counter][0] = inet_if;
            if_addresses[counter][1] = inet_if.getInterfaceAddresses();
            counter++;
        }
    }

    /**
     * This method places all variable to be able to get IP adresses, mask ...
     */
    public static void setEnvironnement() throws SocketException {
        getNetworkCount();
        getNetworkAddresses();
    }


    //------------------------- ENV METHODS ------------------------------------
    /**
     * This method return the Ipv4 masque of the interface in parameter
     * @param i_a the interface to analyse
     * @return an empty String[] if i_a is an IPv6 or, return the IPv4 masque in each String cells.
     */
    public static String[] getIPv4MaskAddress(InterfaceAddress i_a) {
        String temp = "";
        if (i_a.toString().contains(".")) {
            temp = i_a.toString().substring(i_a.toString().indexOf("[") + 2, i_a.toString().lastIndexOf("]"));
        }
        return temp.split("\\.");
    }

    /**
     * Return the numeral inet_addr suffix
     * @param i_a the interfaceAddress to analyze
     * @return -1 if the suffix isn't available (i_a is an IPv6 address) or the suffix.
     */
    public static int getIPv4MaskSuffix(InterfaceAddress i_a) {
        String temp = "";
        if (i_a.toString().contains(".")) {
            temp = i_a.toString().substring(i_a.toString().indexOf(":") + 2, i_a.toString().indexOf("[") - 1);
            temp = temp.substring(temp.indexOf("/") + 1);
            return Integer.parseInt(temp);
        } else {
            return -1;
        }
    }

    /**
     * This method permit to get the binary ipv4 address of the network interface in paramter
     * @param i_a the interface to ask for ip.
     * @return the ip in binary String format (32 bits).
     */
    public static String getIPv4MaskAddressString(InterfaceAddress i_a) {
        int suffix = getIPv4MaskSuffix(i_a);
        String mask = "", binary_suffix = "";
        for (int i = 0; i < suffix; i++) {
            binary_suffix += "1";
        }
        for (int i = 0; i < IPV4LENGTH - suffix; i++) {
            binary_suffix = binary_suffix + "0";
        }
        if (suffix != -1) {
            return convertBinaryIPv4ToPointedIPv4(binary_suffix);
        } else {
            return "-1";
        }
    }

    /**
     * This method permit to get the binary ipv4 address of a network
     * knowning his suffix.
     * @return the ip in binary String format (32 bits).
     * @param suffix the network suffix of the specified ipv4 address.
     */
    public static String getIPv4MaskAddressString(int suffix) {
        String mask = "", binary_suffix = "";
        for (int i = 0; i < suffix; i++) {
            binary_suffix += "1";
        }
        for (int i = 0; i < IPV4LENGTH - suffix; i++) {
            binary_suffix = binary_suffix + "0";
        }
        if (suffix != -1) {
            return convertBinaryIPv4ToPointedIPv4(binary_suffix);
        } else {
            return "-1";
        }
    }

    /**
     * This method permit to convert a binary well formed ip in a String one
     * correctly doted.
     * @param bin_ipv4 the ip in binary format (32 bits).
     * @return the ip well doted (ex : 192.168.0.1)
     */
    public static String convertBinaryIPv4ToPointedIPv4(String bin_ipv4) {
        String res = "", temp = "";
        for (int i = 1; i <= 4; i++) {
            temp = bin_ipv4.subSequence(((i - 1) * 8), (i * 8)).toString();
            temp = (Integer.valueOf(temp, 2)).toString();
            res += temp + ".";
        }
        return res.substring(0, res.length() - 1);
    }

    /**
     * Return the inet_addr in a String array (i.e : int {192,168,0,1}).
     * @param i_a the interfaceAddress to analyze
     * @return an empty String[] if i_a is an IPv6 or, return the IPv4 addr in each String cells.
     */
    public static String[] getIPv4Address(InterfaceAddress i_a) {
        String temp = "";
        if (i_a.toString().contains(".")) {
            temp = i_a.toString().substring(i_a.toString().indexOf("/") + 1, i_a.toString().lastIndexOf("["));
            temp = temp.substring(0, temp.indexOf("/"));
        }
        return temp.split("\\.");
    }

    /**Return the inet_addr in a String (i.e : 192.168.0.1)
     * @param i_a the interfaceAddress to analyze
     * @return an empty String if i_a has an IPv6 or, return the IPv4 addr in dotted format.
     */
    public static String getIPv4AddressString(InterfaceAddress i_a) {
        String temp = i_a.getAddress().toString();
        if (temp.contains(":")) {
            return "";
        } else {
            return temp.substring(1);
        }
    }

    /**
     * Test method wich display the host interfaces and each ip addresses of the interfaces.
     */
    public static void displayNetworkAddressesMaskAndSuffix() throws SocketException {
        getNetworkIf();
        for (int i = 0; i < if_addresses.length; i++) {
            System.out.println("Interface :" + ((NetworkInterface) if_addresses[i][0]).getName());
            for (InterfaceAddress _interface : (List<InterfaceAddress>) if_addresses[i][1]) {
                if (getIPv4AddressString(_interface).contains(".")) {
                    System.out.println("IPV4 Address : " + getIPv4AddressString(_interface));
                    System.out.println("IPV4 Mask : " + getIPv4MaskAddressString(_interface));
                    System.out.println("IPV4 Suffix : " + getIPv4MaskSuffix(_interface) + "\n");
                }
            }
        }
    }

    /**
     * This methd convert an IPv4 address in String binary format
     * @param tab the ip array (each cells is one of the par of the ip)
     * @return the binary format of the ip followed in parameter
     */
    public static String convertIPToBinaryIPv4String(int[] tab) {
        String temp = "", res = "", offsets = "";
        for (int i = 0; i < tab.length; i++) {
            offsets = "";
            temp = Integer.toBinaryString(tab[i]);
            if (temp.length() < 4) {
                for (int j = 0; j < (8 - temp.length()); j++) {
                    offsets += "0";
                }
                offsets += temp;
                temp = offsets;
            }
            res += temp;
        }
        return res;
    }

    /**
     * This method permit to get the network address of a particular IP address.
     * @param ip1 the first element of the ipv4 address (192)
     * @param ip2 the second element of the ipv4 address (168)
     * @param ip3 the third element of the ipv4 address (0)
     * @param ip4 the fourth element of the ipv4 address (1)
     * @param suffix the suffix of the specified IPv4 address
     * @return the network address in IPv4 dotted String format.
     */
    public static String networkFromIPv4AndMask(int ip1, int ip2, int ip3, int ip4, int suffix) {
        String network = "";
        int[] iptocheck = {ip1, ip2, ip3, ip4};
        String binarymask = getIPv4MaskAddressString(suffix);
        String[] temp = binarymask.split("\\.");
        int ip_mask[] = new int[temp.length];
        for (int i = 0; i < 4; i++) {
            ip_mask[i] = Integer.parseInt(temp[i]);
        }
        String ip = convertIPToBinaryIPv4String(iptocheck);
        String binarymask_ip = convertIPToBinaryIPv4String(ip_mask);
        for (int i = 0; i < IPV4LENGTH; i++) {
            network += ((("" + ip.charAt(i)).equals("1") && ("" + binarymask_ip.charAt(i)).equals("1")) ? "1" : "0");
        }
        return convertBinaryIPv4ToPointedIPv4(network);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        setEnvironnement();
        //displayNetworkAddressesMaskAndSuffix();
        System.out.println("reseau :" + networkFromIPv4AndMask(192, 168, 0, 158, 30));
    }
}
