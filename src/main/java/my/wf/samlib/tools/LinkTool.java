package my.wf.samlib.tools;

import my.wf.samlib.model.entity.Writing;

public class LinkTool {
    public static String getAuthorLink(String url, String linkSuffix){
        return url.endsWith(linkSuffix)
                ? url.substring(0, url.length() - linkSuffix.length())
                : url.endsWith("/") ? url : url + "/";
    }

    public static String getFullAuthorLink(String url, String linkSuffix){
        return url.endsWith(linkSuffix)
                ? url
                : (url.endsWith("/") ?url + linkSuffix : url + "/" + linkSuffix);
    }

    public static String getFullWritingLink(Writing writing){
        return writing.getAuthor().getLink() + writing.getLink();
    }
}
