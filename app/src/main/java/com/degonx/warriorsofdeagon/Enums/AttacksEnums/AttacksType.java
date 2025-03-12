package com.degonx.warriorsofdeagon.Enums.AttacksEnums;

public enum AttacksType {
    ShortFront(AttacksRanges.SHORT, AttacksDirection.FRONT, AttacksCount.MULTI, AttacksAction.NONE),

    SingleShortFront(AttacksRanges.SHORT, AttacksDirection.FRONT, AttacksCount.SINGLE, AttacksAction.NONE),
    ShortFrontAndMove(AttacksRanges.SHORT, AttacksDirection.FRONT, AttacksCount.MULTI, AttacksAction.MOVE_THROUGH),
    ShortFrontAndPush(AttacksRanges.SHORT, AttacksDirection.FRONT, AttacksCount.MULTI, AttacksAction.PUSH_BACK),
    SingleShortFrontAndPush(AttacksRanges.SHORT, AttacksDirection.FRONT, AttacksCount.SINGLE, AttacksAction.PUSH_BACK),
    ShortSurround(AttacksRanges.SHORT, AttacksDirection.SURROUND, AttacksCount.MULTI, AttacksAction.NONE),
    MidFront(AttacksRanges.MID, AttacksDirection.FRONT, AttacksCount.MULTI, AttacksAction.NONE),
    SingleMidFront(AttacksRanges.MID, AttacksDirection.FRONT, AttacksCount.SINGLE, AttacksAction.NONE),
    MidFrontAndPush(AttacksRanges.MID, AttacksDirection.FRONT, AttacksCount.MULTI, AttacksAction.PUSH_BACK),
    MidSurround(AttacksRanges.MID, AttacksDirection.SURROUND, AttacksCount.MULTI, AttacksAction.NONE),
    MidSurroundAndPush(AttacksRanges.MID, AttacksDirection.SURROUND, AttacksCount.MULTI, AttacksAction.PUSH_BACK),
    FarFront(AttacksRanges.FAR, AttacksDirection.FRONT, AttacksCount.MULTI, AttacksAction.NONE),
    SingleFarFront(AttacksRanges.FAR, AttacksDirection.FRONT, AttacksCount.SINGLE, AttacksAction.NONE),
    SingleFarFrontAndPull(AttacksRanges.FAR, AttacksDirection.FRONT, AttacksCount.SINGLE, AttacksAction.PULL),
    Homing(AttacksRanges.NONE, AttacksDirection.NONE, AttacksCount.SINGLE, AttacksAction.NONE),
    FullAreaAttack(AttacksRanges.NONE, AttacksDirection.NONE, AttacksCount.MULTI, AttacksAction.NONE);

    private final AttacksRanges attacksRange;
    private final AttacksDirection attackDirection;
    private final AttacksCount attackCount;
    private final AttacksAction attackAction;

    AttacksType(AttacksRanges attacksRange, AttacksDirection attackDirection, AttacksCount attackCount, AttacksAction attackAction) {
        this.attacksRange = attacksRange;
        this.attackDirection = attackDirection;
        this.attackCount = attackCount;
        this.attackAction = attackAction;
    }

    public AttacksRanges getAttacksRange() {
        return attacksRange;
    }

    public AttacksDirection getAttackDirection() {
        return attackDirection;
    }

    public AttacksCount getAttackCount() {
        return attackCount;
    }

    public AttacksAction getAttackAction() {
        return attackAction;
    }
}
