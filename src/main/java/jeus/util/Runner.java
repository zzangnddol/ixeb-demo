package jeus.util;

import java.io.File;
import java.io.PrintStream;

public class Runner
{
    public static final int LIC_EDITION_BASE = 1;
    public static final int LIC_EDITION_BASEPLUS = 2;
    public static final int LIC_EDITION_STANDARD = 3;
    public static final int LIC_EDITION_ENTERPRISE = 5;
    public static final int LIC_TYPE_DEMO = 0;
    public static final int LIC_TYPE_REAL = 1;
    public static final int LIC_TYPE_BUNDLE = 2;
    public static final int LIC_TYPE_DEVSERVER = 3;
    public static final int LIC_TYPE_DEVELOPER = 4;
    public static final int LIC_TYPE_ACADEMIC = 5;
    public static final int LIC_TYPE_CLOUD = 16;
    private static final String LICENSE_FILE = System.getProperty("jeus.home") + File.separator + "license" + File.separator + "license";

    static
    {
        System.loadLibrary("Runner");
    }

    public static long getLicenseDue()
    {
        try
        {
            long due = getLicenseDueSec0(LICENSE_FILE);
            if (due < 0L) {
                return due;
            }
            return due * 1000L;
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        return -1L;
    }

    public static void getLicenseInfo()
    {
        try
        {
            printLicenseInfo0(LICENSE_FILE);
        }
        catch (Throwable t)
        {
            System.out.println(">>>>>>>>>>>> fail to get license information");
        }
    }

    public static synchronized boolean checkBoot()
    {
        try
        {
            int n = checkBoot0(LICENSE_FILE);
            if (n == 1) {
                return true;
            }
            return false;
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        return false;
    }

    public static boolean checkLicenseEdition(int edition)
    {
        try
        {
            int n = checkLicenseEdition0(LICENSE_FILE, edition);
            if (n == 1) {
                return true;
            }
            return false;
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        return false;
    }

    public static int getLicenseEdition()
    {
        try
        {
            return getLicenseEdition0(LICENSE_FILE);
        }
        catch (Throwable t) {}
        return -1;
    }

    public static int getLicenseType()
    {
        try
        {
            return getLicenseType0(LICENSE_FILE);
        }
        catch (Throwable t) {}
        return -1;
    }

    public static int getNumOfLicensedClient()
    {
        try
        {
            return 100;
            //return getLicenseNumOfClient0(LICENSE_FILE);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        return -1;
    }

    public static byte[] getProductCode(int code)
    {
        byte[] product = getProductCode0(LICENSE_FILE, code);
        if ((product != null) && (product.length > 0)) {
            return product;
        }
        return null;
    }

    public static boolean isEmbededJeus()
    {
        int b = 0;
        try
        {
            b = isEmbeded0();
        }
        catch (Throwable e) {}
        if (b == 1) {
            return true;
        }
        return false;
    }

    public static native int getNumCPU();

    private static native int getLicenseDueSec0(String paramString);

    private static native int printLicenseInfo0(String paramString);

    private static native int checkBoot0(String paramString);

    private static native int checkLicenseEdition0(String paramString, int paramInt);

    private static native int getLicenseEdition0(String paramString);

    private static native int getLicenseType0(String paramString);

    private static native int getLicenseNumOfClient0(String paramString);

    private static native byte[] getProductCode0(String paramString, int paramInt);

    private static native int isEmbeded0();
}
