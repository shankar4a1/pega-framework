

package com.pega.framework;

import org.slf4j.*;

import java.awt.*;

public class RobotKeyboardImpl implements RobotKeyboard {


    private static final Logger LOGGER;
    private Robot robot;

    static {
        LOGGER = LoggerFactory.getLogger(RobotKeyboard.class.getName());
    }

    public RobotKeyboardImpl() {

        this.robot = null;
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            RobotKeyboardImpl.LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void type(final String characters) {
        for (int length = characters.length(), i = 0; i < length; ++i) {
            final char character = characters.charAt(i);
            this.type(character);
        }
    }

    public void type(final char character) {
        switch (character) {
            case 'a': {
                this.doType(65);
                break;
            }
            case 'b': {
                this.doType(66);
                break;
            }
            case 'c': {
                this.doType(67);
                break;
            }
            case 'd': {
                this.doType(68);
                break;
            }
            case 'e': {
                this.doType(69);
                break;
            }
            case 'f': {
                this.doType(70);
                break;
            }
            case 'g': {
                this.doType(71);
                break;
            }
            case 'h': {
                this.doType(72);
                break;
            }
            case 'i': {
                this.doType(73);
                break;
            }
            case 'j': {
                this.doType(74);
                break;
            }
            case 'k': {
                this.doType(75);
                break;
            }
            case 'l': {
                this.doType(76);
                break;
            }
            case 'm': {
                this.doType(77);
                break;
            }
            case 'n': {
                this.doType(78);
                break;
            }
            case 'o': {
                this.doType(79);
                break;
            }
            case 'p': {
                this.doType(80);
                break;
            }
            case 'q': {
                this.doType(81);
                break;
            }
            case 'r': {
                this.doType(82);
                break;
            }
            case 's': {
                this.doType(83);
                break;
            }
            case 't': {
                this.doType(84);
                break;
            }
            case 'u': {
                this.doType(85);
                break;
            }
            case 'v': {
                this.doType(86);
                break;
            }
            case 'w': {
                this.doType(87);
                break;
            }
            case 'x': {
                this.doType(88);
                break;
            }
            case 'y': {
                this.doType(89);
                break;
            }
            case 'z': {
                this.doType(90);
                break;
            }
            case 'A': {
                this.doType(16, 65);
                break;
            }
            case 'B': {
                this.doType(16, 66);
                break;
            }
            case 'C': {
                this.doType(16, 67);
                break;
            }
            case 'D': {
                this.doType(16, 68);
                break;
            }
            case 'E': {
                this.doType(16, 69);
                break;
            }
            case 'F': {
                this.doType(16, 70);
                break;
            }
            case 'G': {
                this.doType(16, 71);
                break;
            }
            case 'H': {
                this.doType(16, 72);
                break;
            }
            case 'I': {
                this.doType(16, 73);
                break;
            }
            case 'J': {
                this.doType(16, 74);
                break;
            }
            case 'K': {
                this.doType(16, 75);
                break;
            }
            case 'L': {
                this.doType(16, 76);
                break;
            }
            case 'M': {
                this.doType(16, 77);
                break;
            }
            case 'N': {
                this.doType(16, 78);
                break;
            }
            case 'O': {
                this.doType(16, 79);
                break;
            }
            case 'P': {
                this.doType(16, 80);
                break;
            }
            case 'Q': {
                this.doType(16, 81);
                break;
            }
            case 'R': {
                this.doType(16, 82);
                break;
            }
            case 'S': {
                this.doType(16, 83);
                break;
            }
            case 'T': {
                this.doType(16, 84);
                break;
            }
            case 'U': {
                this.doType(16, 85);
                break;
            }
            case 'V': {
                this.doType(16, 86);
                break;
            }
            case 'W': {
                this.doType(16, 87);
                break;
            }
            case 'X': {
                this.doType(16, 88);
                break;
            }
            case 'Y': {
                this.doType(16, 89);
                break;
            }
            case 'Z': {
                this.doType(16, 90);
                break;
            }
            case '`': {
                this.doType(192);
                break;
            }
            case '0': {
                this.doType(48);
                break;
            }
            case '1': {
                this.doType(49);
                break;
            }
            case '2': {
                this.doType(50);
                break;
            }
            case '3': {
                this.doType(51);
                break;
            }
            case '4': {
                this.doType(52);
                break;
            }
            case '5': {
                this.doType(53);
                break;
            }
            case '6': {
                this.doType(54);
                break;
            }
            case '7': {
                this.doType(55);
                break;
            }
            case '8': {
                this.doType(56);
                break;
            }
            case '9': {
                this.doType(57);
                break;
            }
            case '-': {
                this.doType(45);
                break;
            }
            case '=': {
                this.doType(61);
                break;
            }
            case '~': {
                this.doType(16, 192);
                break;
            }
            case '!': {
                this.doType(517);
                break;
            }
            case '@': {
                this.doType(16, 50);
                break;
            }
            case '#': {
                this.doType(16, 51);
                break;
            }
            case '$': {
                this.doType(16, 52);
                break;
            }
            case '%': {
                this.doType(16, 53);
                break;
            }
            case '^': {
                this.doType(16, 54);
                break;
            }
            case '&': {
                this.doType(16, 55);
                break;
            }
            case '*': {
                this.doType(16, 56);
                break;
            }
            case '(': {
                this.doType(16, 57);
                break;
            }
            case ')': {
                this.doType(16, 48);
                break;
            }
            case '_': {
                this.doType(523);
                break;
            }
            case '+': {
                this.doType(521);
                break;
            }
            case '\t': {
                this.doType(9);
                break;
            }
            case '\n': {
                this.doType(10);
                break;
            }
            case '[': {
                this.doType(91);
                break;
            }
            case ']': {
                this.doType(93);
                break;
            }
            case '\\': {
                this.doType(92);
                break;
            }
            case '{': {
                this.doType(16, 91);
                break;
            }
            case '}': {
                this.doType(16, 93);
                break;
            }
            case '|': {
                this.doType(16, 92);
                break;
            }
            case ';': {
                this.doType(59);
                break;
            }
            case ':': {
                this.doType(16, 59);
                break;
            }
            case '\'': {
                this.doType(222);
                break;
            }
            case '\"': {
                this.doType(152);
                break;
            }
            case ',': {
                this.doType(44);
                break;
            }
            case '<': {
                this.doType(16, 44);
                break;
            }
            case '.': {
                this.doType(46);
                break;
            }
            case '>': {
                this.doType(16, 46);
                break;
            }
            case '/': {
                this.doType(47);
                break;
            }
            case '?': {
                this.doType(16, 47);
                break;
            }
            case ' ': {
                this.doType(32);
                break;
            }
            default: {
                throw new IllegalArgumentException("Cannot type character " + character);
            }
        }
    }

    private void doType(final int... keyCodes) {
        this.doType(keyCodes, 0, keyCodes.length);
    }

    private void doType(final int[] keyCodes, final int offset, final int length) {
        if (length == 0) {
            return;
        }
        this.robot.keyPress(keyCodes[offset]);
        this.doType(keyCodes, offset + 1, length - 1);
        this.robot.keyRelease(keyCodes[offset]);
    }

    @Override
    public void tab() {
        this.robot.keyPress(9);
        this.robot.keyRelease(9);
    }

    @Override
    public void enter() {
        this.robot.keyPress(10);
        this.robot.keyRelease(10);
    }

    @Override
    public void f11() {
        this.robot.keyPress(122);
        this.robot.keyRelease(122);
    }
}
