package my.wf.samlib.tools;

import my.wf.samlib.model.entity.Writing;

public class LinkTool {

    public static String getAuthorLink(String url, String linkSuffix){
        String suffix = fixSuffix(linkSuffix);
        return hasLinkSuffix(suffix) && url.endsWith(suffix)
                ? url.substring(0, url.length() - suffix.length())
                : isPageUrl(url) || url.endsWith("/") ? url : url + "/";
    }

    public static String getAuthorIndexPage(String url, String linkSuffix){
        String suffix = fixSuffix(linkSuffix);

        return isPageUrl(url) || !hasLinkSuffix(suffix) || url.endsWith(suffix)
                ? url
                : url.endsWith("/")
                    ? url + suffix
                    : url + "/" + suffix;
    }

    public static String getFullWritingLink(Writing writing){
        return writing.getAuthor().getLink() + writing.getLink();
    }

    private static boolean hasLinkSuffix(String linkSuffix){
        return (null != linkSuffix) && (0 < linkSuffix.trim().length());
    }

    private static boolean isPageUrl(String url){
        return url.endsWith(".html") || url.endsWith(".shtml") || url.endsWith(".htm");
    }

    private static String fixSuffix(String linkSuffix){
        return (null == linkSuffix)?"":linkSuffix.trim();
    }
}
