package com.extacy.ms.common

import java.util.*

enum class VirtualKey(val vk: Int)
{
    A(65),
    // The letter "A" key or button.

    Accept(30),
    // The IME accept button.

    Add(107),
    // The add (+) operation key or button as located on a numeric pad.

    Application(93),
    // The application key or button.

    B(66),
    // The letter "B" key or button.

    Back(8),
    // The back key or button.

    C(67),
    // The letter "C" key or button.

    Cancel(3),
    // The cancel key or button.

    CapitalLock(20),
    // The Caps Lock key or button.

    Clear(12),
    // The Clear key or button.

    Control(17),
    // The Ctrl key or button. This is // The general Ctrl case), applicable to key layouts with only one Ctrl key or that do not need to differentiate between left Ctrl and right Ctrl keystrokes.

    Convert(28),
    // The IME convert button.

    D(68),
    // The letter "D" key or button.

    Decimal(110),
    // The decimal (.) key or button as located on a numeric pad.

    Delete(46),
    // The Delete key or button.

    Divide(111),
    // The divide (/) operation key or button as located on a numeric pad.

    Down(40),
    // The Down Arrow key or button.

    E(69),
    // The letter "E" key or button.

    End(35),
    // The End key or button.

    Enter(13),
    // The Enter key or button.

    Escape(27),
    // The Esc key or button.

    Execute(43),
    // The execute key or button.

    F(70),
    // The letter "F" key or button.

    F1(112),
    // The F1 function key or button.

    F10(121),
    // The F10 function key or button.

    F11(122),
    // The F11 function key or button.

    F12(123),
    // The F12 function key or button.

    F13(124),
    // The F13 function key or button.

    F14(125),
    // The F14 function key or button.

    F15(126),
    // The F15 function key or button.

    F16(127),
    // The F16 function key or button.

    F17(128),
    // The F17 function key or button.

    F18(129),
    // The F18 function key or button.

    F19(130),
    // The F19 function key or button.

    F2(113),
    // The F2 function key or button.

    F20(131),
    // The F20 function key or button.

    F21(132),
    // The F21 function key or button.

    F22(133),
    // The F22 function key or button.

    F23(134),
    // The F23 function key or button.

    F24(135),
    // The F24 function key or button.

    F3(114),
    // The F3 function key or button.

    F4(115),
    // The F4 function key or button.

    F5(116),
    // The F5 function key or button.

    F6(117),
    // The F6 function key or button.

    F7(118),
    // The F7 function key or button.

    F8(119),
    // The F8 function key or button.

    F9(120),
    // The F9 function key or button.

    Favorites(171),
    // The favorites key or button.

    Final(24),
    // The Final symbol key or button.

    G(71),
    // The letter "G" key or button.

    GamepadA(195),
    // The gamepad A button.

    GamepadB(196),
    // The gamepad B button.

    GamepadDPadDown(204),
    // The gamepad d-pad down.

    GamepadDPadLeft(205),
    // The gamepad d-pad left.

    GamepadDPadRight(206),
    // The gamepad d-pad right.

    GamepadDPadUp(203),
    // The gamepad d-pad up.

    GamepadLeftShoulder(200),
    // The gamepad left shoulder.

    GamepadLeftThumbstickButton(209),
    // The gamepad left thumbstick button.

    GamepadLeftThumbstickDown(212),
    // The gamepad left thumbstick down.

    GamepadLeftThumbstickLeft(214),
    // The gamepad left thumbstick left.

    GamepadLeftThumbstickRight(213),
    // The gamepad left thumbstick right.

    GamepadLeftThumbstickUp(211),
    // The gamepad left thumbstick up.

    GamepadLeftTrigger(201),
    // The gamepad left trigger.

    GamepadMenu(207),
    // The gamepad menu button.

    GamepadRightShoulder(199),
    // The gamepad right shoulder.

    GamepadRightThumbstickButton(210),
    // The gamepad right thumbstick button.

    GamepadRightThumbstickDown(216),
    // The gamepad right thumbstick down.

    GamepadRightThumbstickLeft(218),
    // The gamepad right thumbstick left.

    GamepadRightThumbstickRight(217),
    // The gamepad right thumbstick right.

    GamepadRightThumbstickUp(215),
    // The gamepad right thumbstick up.

    GamepadRightTrigger(202),
    // The gamepad right trigger.

    GamepadView(208),
    // The gamepad view button.

    GamepadX(197),
    // The gamepad X button.

    GamepadY(198),
    // The gamepad Y button.

    GoBack(166),
    // The go back key or button.

    GoForward(167),
    // The go forward key or button.

    GoHome(172),
    // The go home key or button.

    H(72),
    // The letter "H" key or button.

    Hangul(21),
    // The Hangul symbol option key or button.

    // Hangul are The syllabaries that form parts of The Korean writing system.

    Hanja(25),
    // The Hanja symbol option key or button.

    // Hanja are The syllabaries that form parts of The Korean writing system.

    Help(47),
    // The Help key or button.

    Home(36),
    // The Home key or button.

    I(73),
    // The letter "I" key or button.

    ImeOff(26),
    // The Input Method Editor Off key or button.

    ImeOn(22),
    // The Input Method Editor On key or button.

    Insert(45),
    // The Insert key or button.

    J(74),
    // The letter "J" key or button.

    Junja(23),
    // The Junja symbol option key or button.

    // Junja are The syllabaries that form parts of The Korean writing system.

    K(75),
    // The letter "K" key or button.

    Kana(21),
    // The Kana symbol option key or button.

    // Kana are The syllabaries that form parts of The Japanese writing system.

    Kanji(25),
    // The Kanji symbol option key or button.

    // Kanji are The syllabaries that form parts of The Japanese writing system.

    L(76),
    // The letter "L" key or button.

    Left(37),
    // The Left Arrow key or button.

    LeftButton(1),
    // The left mouse button.

    LeftControl(162),
    // The left Ctrl key or button.

    LeftMenu(164),
    // The left menu key or button.

    LeftShift(160),
    // The left Shift key or button.

    LeftWindows(91),
    // The left Windows key or button.

    M(77),
    // The letter "M" key or button.

    Menu(18),
    // The menu key or button.

    MiddleButton(4),
    // The middle mouse button.

    ModeChange(31),
    // The IME mode change button.

    Multiply(106),
    // The multiply (*) operation key or button as located on a numeric pad.

    N(78),
    // The letter "N" key or button.

    NavigationAccept(142),
    // The navigation accept key or button.

    NavigationCancel(143),
    // The navigation cancel key or button.

    NavigationDown(139),
    // The navigation down key or button.

    NavigationLeft(140),
    // The navigation left key or button.

    NavigationMenu(137),
    // The navigation menu key or button.

    NavigationRight(141),
    // The navigation right key or button.

    NavigationUp(138),
    // The navigation up key or button.

    NavigationView(136),
    // The navigation up key or button.

    NonConvert(29),
    // The IME nonconvert button.

    None(0),
    // No virtual key value.

    Number0(48),
    // The number "0" key or button.

    Number1(49),
    // The number "1" key or button.

    Number2(50),
    // The number "2" key or button.

    Number3(51),
    // The number "3" key or button.

    Number4(52),
    // The number "4" key or button.

    Number5(53),
    // The number "5" key or button.

    Number6(54),
    // The number "6" key or button.

    Number7(55),
    // The number "7" key or button.

    Number8(56),
    // The number "8" key or button.

    Number9(57),
    // The number "9" key or button.

    NumberKeyLock(144),
    // The Num Lock key or button.

    NumberPad0(96),
    // The number "0" key or button as located on a numeric pad.

    NumberPad1(97),
    // The number "1" key or button as located on a numeric pad.

    NumberPad2(98),
    // The number "2" key or button as located on a numeric pad.

    NumberPad3(99),
    // The number "3" key or button as located on a numeric pad.

    NumberPad4(100),
    // The number "4" key or button as located on a numeric pad.

    NumberPad5(101),
    // The number "5" key or button as located on a numeric pad.

    NumberPad6(102),
    // The number "6" key or button as located on a numeric pad.

    NumberPad7(103),
    // The number "7" key or button as located on a numeric pad.

    NumberPad8(104),
    // The number "8" key or button as located on a numeric pad.

    NumberPad9(105),
    // The number "9" key or button as located on a numeric pad.

    O(79),
    // The letter "O" key or button.

    P(80),
    // The letter "P" key or button.

    PageDown(34),
    // The Page Down key or button.

    PageUp(33),
    // The Page Up key or button.

    Pause(19),
    // The Pause key or button.

    Print(42),
    // The Print key or button.

    Q(81),
    // The letter "Q" key or button.

    R(82),
    // The letter "R" key or button.

    Refresh(168),
    // The refresh key or button.

    Right(39),
    // The Right Arrow key or button.

    RightButton(2),
    // The right mouse button.

    RightControl(163),
    // The right Ctrl key or button.

    RightMenu(165),
    // The right menu key or button.

    RightShift(161),
    // The right Shift key or button.

    RightWindows(92),
    // The right Windows key or button.

    S(83),
    // The letter "S" key or button.

    Scroll(145),
    // The Scroll Lock (ScrLk) key or button.

    Search(170),
    // The search key or button.

    Select(41),
    // The Select key or button.

    Separator(108),
    // The separator key or button as located on a numeric pad.

    Shift(16),
    // The Shift key or button. This is // The general Shift case), applicable to key layouts with only one Shift key or that do not need to differentiate between left Shift and right Shift keystrokes.

    Sleep(95),
    // The sleep key or button.

    Snapshot(44),
    // The snapshot key or button.

    Space(32),
    // The Spacebar key or button.

    Stop(169),
    // The stop key or button.

    Subtract(109),
    // The subtract (-) operation key or button as located on a numeric pad.

    T(84),
    // The letter "T" key or button.

    Tab(9),
    // The Tab key or button.

    U(85),
    // The letter "U" key or button.

    Up(38),
    // The Up Arrow key or button.

    V(86),
    // The letter "V" key or button.

    W(87),
    // The letter "W" key or button.

    X(88),
    // The letter "X" key or button.

    XButton1(5),
    // An additional "extended" device key or button (for example), an additional mouse button).

    XButton2(6),
    // An additional "extended" device key or button (for example), an additional mouse button).

    Y(89),
    // The letter "Y" key or button.

    Z(90);
    // The letter "Z" key or button.
    companion object {
        @JvmStatic
        fun fromInt(vk: Int): VirtualKey =
            values().find { value -> value.vk == vk } ?: A
    }


}

inline fun <reified T : Enum<T>> getNames() = enumValues<T>().map { it.name }