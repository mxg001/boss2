package test;
import cn.eeepay.framework.enums.RepayEnum;
import cn.eeepay.framework.service.ActivationCodeService;
import cn.eeepay.framework.service.impl.ActivationCodeServiceImpl;

public class TestActivationCodeService {

    public static void main(String[] args) throws InterruptedException {
        final ActivationCodeService activationCodeService = new ActivationCodeServiceImpl();
        final String codeType = RepayEnum.REPAY.getType();
        for(int i = 0; i < 3; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    activationCodeService.buildActivationCode(50, codeType);
                }
            }).start();
        }

        Thread.sleep(3100);
        activationCodeService.buildActivationCode(50, codeType);
    }
}
