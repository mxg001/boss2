package cn.eeepay.framework.util;

/**
 * 超级银行家的常量
 * @author tans
 * @date 2018/11/13 10:47
 */
public class SuperBankApiConstant {

    public static final String SUPER_BANK_API_KEY = "TeENiFAOo4Qku1jKuk5x";

    public static void main(String[] args) {
//        System.out.println(RandomNumber.mumberRandom("",20, 4));
        System.out.println("superBank" + SUPER_BANK_API_KEY);
        System.out.println(Md5.md5Str("superBank" + SUPER_BANK_API_KEY));
    }
}
