

package com.pega.scripts;

public class OffSetCalc {

    public static final String ABSOLUTE_OFFSET_LEFT = "window.getAbsoluteElementOffsetLeft=function getAbsoluteElementOffsetLeft(elmt){var clientRect = elmt.getBoundingClientRect();var absoluteLeft = clientRect.left;var parent = elmt.ownerDocument.defaultView.frameElement;while(parent!=null){absoluteLeft = absoluteLeft+parent.getBoundingClientRect().left;parent = parent.ownerDocument.defaultView.frameElement;}return Math.round(absoluteLeft);}";
    public static final String ABSOLUTE_OFFSET_TOP = "window.getAbsoluteElementOffsetTop=function getAbsoluteElementOffsetTop(elmt){var clientRect = elmt.getBoundingClientRect();var absoluteTop = clientRect.top;var parent = elmt.ownerDocument.defaultView.frameElement;while(parent!=null){absoluteTop = absoluteTop+parent.getBoundingClientRect().top;parent = parent.ownerDocument.defaultView.frameElement;}return Math.round(absoluteTop);}";
}
