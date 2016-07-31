package my.wf.samlib.updater;

import my.wf.samlib.model.dto.IpCheckState;
import my.wf.samlib.updater.parser.SamlibAuthorParser;
import my.wf.samlib.updater.parser.SamlibPageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BanChecker {
    private static final Logger logger = LoggerFactory.getLogger(BanChecker.class);

    private SamlibAuthorParser samlibAuthorParser;
    private SamlibPageReader samlibPageReader;


    public BanChecker(SamlibAuthorParser samlibAuthorParser, SamlibPageReader samlibPageReader) {
        this.samlibAuthorParser = samlibAuthorParser;
        this.samlibPageReader = samlibPageReader;
    }

    public boolean checkIpState(String banCheckUrl) {
        IpCheckState ipCheckState = new IpCheckState();
        try{
            logger.debug("check IP state {}", banCheckUrl);
            String checkPage = samlibPageReader.readPage(banCheckUrl);
            ipCheckState = samlibAuthorParser.parseIpCheckState(checkPage);
        }catch (Exception e){
            ipCheckState.setInfo(e.getMessage());
            ipCheckState.setOtherError(true);
        }
        printCheckState(ipCheckState);
        return ipCheckState.isOk();
    }

    void printCheckState(IpCheckState ipCheckState){
        logger.info("ip: {}, in spam: {}, is blocked: {}, other: {}", ipCheckState.getIp(), ipCheckState.isInSpamList(), ipCheckState.isBlocked(), ipCheckState.isOtherError());
        if(!ipCheckState.isOk()){
            logger.error("IP Check problems:");
            logger.error(ipCheckState.getInfo());
        }
    }

}
