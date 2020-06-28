

package com.pega.scripts;

public class OffSetCalc {
    private static final String VERSION = "$Id: OffSetCalc.java 121818 2015-01-26 07:18:23Z SachinVellanki $";
    public static final String ABSOLUTE_OFFSET_LEFT = "window.getAbsoluteElementOffsetLeft=function getAbsoluteElementOffsetLeft(elmt){var clientRect = elmt.getBoundingClientRect();var absoluteLeft = clientRect.left;var parent = elmt.ownerDocument.defaultView.frameElement;while(parent!=null){absoluteLeft = absoluteLeft+parent.getBoundingClientRect().left;parent = parent.ownerDocument.defaultView.frameElement;}return Math.round(absoluteLeft);}";
    public static final String ABSOLUTE_OFFSET_TOP = "window.getAbsoluteElementOffsetTop=function getAbsoluteElementOffsetTop(elmt){var clientRect = elmt.getBoundingClientRect();var absoluteTop = clientRect.top;var parent = elmt.ownerDocument.defaultView.frameElement;while(parent!=null){absoluteTop = absoluteTop+parent.getBoundingClientRect().top;parent = parent.ownerDocument.defaultView.frameElement;}return Math.round(absoluteTop);}";
}
