package com.company.helper;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

/**
 * Created by huxm11315 on 2017/7/18.
 */
public class LdapADHelper {

    private String host;
    private String url;
    private String adminName;
    private String adminPassword;
    private LdapContext ctx = null;

    public LdapADHelper()
    {
    }

    public LdapADHelper(String host, String adminName, String adminPassword) {
        this.host = host;
        this.url = new String("ldap://" + host);
        this.adminName = adminName;
        this.adminPassword = adminPassword;

        initLdap();
    }

    private void initLdap()
    {
        Hashtable HashEnv = new Hashtable();
        HashEnv.put("java.naming.security.authentication", "simple");
        HashEnv.put("java.naming.security.principal", this.adminName);
        HashEnv.put("java.naming.security.credentials", this.adminPassword);
        HashEnv.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        HashEnv.put("java.naming.provider.url", this.url);
        try {
            this.ctx = new InitialLdapContext(HashEnv, null);
            System.out.println("初始化ldap成功！");
        } catch (NamingException e) {
            e.printStackTrace();
            System.err.println("Throw Exception : " + e);
        }
    }

    public void closeLdap()
    {
        try
        {
            this.ctx.close();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public String GetADInfo(String type, String filter, String name, String node)
            throws UnsupportedEncodingException
    {
        String userName = name;
        if (userName == null) {
            userName = "";
        }
        String company = "";

        StringBuffer ret = new StringBuffer();
        try
        {
            String searchBase = node;

            String searchFilter = "(&(objectClass=" + type + ")(" + filter + "=*" + name + "*))";

            SearchControls searchCtls = new SearchControls();

            searchCtls.setSearchScope(2);

            NamingEnumeration answer = this.ctx.search(searchBase, searchFilter, searchCtls);

            int totalResults = 0;
            int rows = 0;
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult)answer.next();
                ++rows;
                String dn = sr.getName();
                System.out.println(dn);
                ret.append("dn:" + dn + "<br />");
                ret.append("    ---------------<br />");
                Attributes Attrs = sr.getAttributes();
                if (Attrs == null) continue;
                try {
                    for (NamingEnumeration ne = Attrs.getAll(); ne.hasMore(); ) {
                        Attribute Attr = (Attribute)ne.next();
                        System.out.println(" AttributeID=属性名：" + Attr.getID().toString());
                        ret.append(" AttributeID=属性名：" + Attr.getID().toString() + "<br />");

                        for (NamingEnumeration e = Attr.getAll(); e.hasMore(); ++totalResults) {
                            company = e.next().toString();
                            System.out.println("    AttributeValues=属性值：" + new String(company.getBytes(), "utf-8"));
                            ret.append("    AttributeValues=属性值：" + new String(company.getBytes(), "utf-8") + "<br />");
                        }
                        System.out.println("    ---------------");
                        ret.append("    ---------------<br />");
                    }
                } catch (NamingException e) {
                    System.err.println("Throw Exception : " + e);
                }
            }

            System.out.println("************************************************");
            ret.append("************************************************<br />");
            System.out.println("Number: " + totalResults);
            ret.append("Number: " + totalResults + "<br>");
            System.out.println("总共用户数：" + rows);
            ret.append("总共用户数：" + rows + "<br />");
        } catch (NamingException e) {
            e.printStackTrace();
            System.err.println("Throw Exception : " + e);
        }
        return ret.toString();
    }
    public String getHost() {
        return this.host; }
    public void setHost(String host) { this.host = host; }
    public String getUrl() { return this.url; }
    public void setUrl(String url) { this.url = url; }
    public String getAdminName() { return this.adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }
    public String getAdminPassword() { return this.adminPassword; }
    public void setAdminPassword(String adminPassword) { this.adminPassword = adminPassword; }
    public LdapContext getCtx() { return this.ctx; }
    public void setCtx(LdapContext ctx) { this.ctx = ctx; }


    public static void main(String[] args)
            throws UnsupportedEncodingException
    {
        LdapADHelper ad = new LdapADHelper("192.168.1.201:389", "Cayzlh@cayzlh.com", "WS2008oracle11g");
        ad.GetADInfo("user", "cn", "user1", "DC=cayzlh,DC=com");

        ad.closeLdap();
    }

}
