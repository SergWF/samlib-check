package my.wf.samlib.updater.impl;

import my.wf.samlib.updater.BanChecker;
import my.wf.samlib.updater.parser.BanCheckerFactory;
import my.wf.samlib.updater.parser.SamlibAuthorParser;
import my.wf.samlib.updater.parser.SamlibPageReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BanCheckeFactoryImpl implements BanCheckerFactory {

    @Autowired
    private SamlibAuthorParser parser;
    @Autowired
    private SamlibPageReader reader;

    @Override
    public BanChecker getBanChecker() {
        return new BanChecker(parser, reader);
    }
}
