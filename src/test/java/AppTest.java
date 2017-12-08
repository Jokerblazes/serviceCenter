import com.joker.container.ProviderContainer;
import com.joker.entity.*;
import com.joker.utils.MessagePackageFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joker on 2017/12/8.
 */
public class AppTest {

    @Test
    public void testRegist() {

        //new order provider
        Provider provider = new Provider();
        provider.setServiceName("order");
        Node node = new Node();
        node.setIp("192.168.1.1");
        node.setId(1);
        node.setPort(8080);
        provider.setNode(node);
        ProviderContainer container = ProviderContainer.getInstance();
        container.registService(provider.getServiceName(),provider.getNode().getId(),provider);


        //new phone provider
        Provider provider2 = new Provider();
        provider2.setServiceName("phone");
        Node node2 = new Node();
        node2.setIp("192.168.1.1");
        node2.setId(2);
        node2.setPort(8888);
        provider2.setNode(node2);
        container.registService(provider2.getServiceName(),provider2.getNode().getId(),provider2);


        //new customer
        Customer customer = new Customer();
        List<String> serviceNames = new ArrayList<String>();
        serviceNames.add("order");
        serviceNames.add("phone");
        customer.setServiceNames(serviceNames);
        Node node3 = new Node();
        node3.setId(1);
        node3.setPort(8080);
        node3.setIp("192.168.1.1");
        customer.setNode(node3);
        for (String serviceName:
             serviceNames) {
            ProviderSet set = container.getServiceSet(serviceName);
            set.addObserver(customer);
        }

        //new order provider
        Provider provider4 = new Provider();
        provider4.setServiceName("phone");
        Node node4 = new Node();
        node4.setIp("192.168.1.1");
        node4.setId(3);
        node4.setPort(8888);
        provider4.setNode(node4);
        container.registService(provider4.getServiceName(),provider4.getNode().getId(),provider4);

    }
}
