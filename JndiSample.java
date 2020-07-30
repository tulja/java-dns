package matrices;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;

public class JndiSample {
    public static void main(String[] args) throws UnknownHostException {
        System.out.println("This demos JNDI API capability");
        //String hostName = "api.cloudflare.com";
        String hostName = "test.salesforce.com.";
        System.out.println("Resolving the hostname for "+hostName);
        String[] results = getRecords(hostName, "A");
        System.out.println("Printing out the results");
        for (String result:results) {
            System.out.println(result);
           // String[] ipResults = getRecords(result,"A");
            //for (String ipResult:ipResults)
            //System.out.println(ipResult) ;
        }
        System.out.println("Resolving the "+hostName+" with the Default JDK resolver");
        InetAddress address = InetAddress.getByName(hostName);
        System.out.println(address.getHostAddress());
    }
    public static String[] getRecords(String hostName, String type) {
        Set<String> results = new TreeSet<String>();
        System.out.println("Got type :"+type);
        try {
            Hashtable<String, String> envProps =
                    new Hashtable<String, String>();
            envProps.put(Context.INITIAL_CONTEXT_FACTORY,
                    "com.sun.jndi.dns.DnsContextFactory");
            DirContext dnsContext = new InitialDirContext(envProps);
            Attributes dnsEntries = dnsContext.getAttributes(
                    hostName, new String[]{type});
            if(dnsEntries != null) {
                System.out.println(dnsEntries);
                NamingEnumeration<?> dnsEntryIterator =
                        dnsEntries.get(type).getAll();
                while(dnsEntryIterator.hasMoreElements()) {
                    results.add(dnsEntryIterator.next().toString());
            }
        }} catch(NamingException e) {
            // Handle exception
        }
        return results.toArray(new String[results.size()]);
    }
}
