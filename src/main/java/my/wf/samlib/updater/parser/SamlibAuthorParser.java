package my.wf.samlib.updater.parser;

import my.wf.samlib.model.dto.IpCheckState;
import my.wf.samlib.model.entity.Writing;

import java.util.Set;

public interface SamlibAuthorParser {

    String parseAuthorName(String pageString);

    Set<Writing> parseWritings(String pageString);

    IpCheckState parseIpCheckState(String checkPageString);
}
