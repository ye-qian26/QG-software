import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MACtest1 {
    public static void main(String[] args) throws UnknownHostException, SocketException {
        InetAddress ip = InetAddress.getLocalHost();
        NetworkInterface network = NetworkInterface.getByInetAddress(ip);
        byte[] mac = network.getHardwareAddress();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }
        System.out.println("MAC地址："  + sb.toString());
    }
}
