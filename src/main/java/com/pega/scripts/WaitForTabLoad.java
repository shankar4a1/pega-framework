

package com.pega.scripts;

public class WaitForTabLoad {

    public static final String WAIT_FOR_TAB_LOAD = "window.isTabLoaded=function isTabLoaded(){var tabs = document.querySelectorAll(\"ul[class*='Temporary_top_tabsList'] li[role='tab']\");for(var i=0; i<tabs.length; i++){var curr = tabs[i];if(!curr.hasAttribute('title')){return false;}var title = curr.getAttribute('title');if(title.indexOf('Opening')!=-1){return false;}}return true;}";
}
