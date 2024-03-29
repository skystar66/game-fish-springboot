// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Mid.proto

package com.xl.game.message;

public final class Mid {
  private Mid() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  /**
   * <pre>
   *1-10000 		内部消息(游戏服务器内部使用)
   *10001-20000           大厅消息（所有非大厅消息，网关进行转发到子游戏服务器处理）
   *20001-30000           捕鱼游戏
   * </pre>
   *
   * Protobuf enum {@code MID}
   */
  public enum MID
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <pre>
     *服务器内部消息
     * </pre>
     *
     * <code>ServerRegisterReq = 1001;</code>
     */
    ServerRegisterReq(1001),
    /**
     * <pre>
     *注册服务器消息
     * </pre>
     *
     * <code>ServerRegisterRes = 1002;</code>
     */
    ServerRegisterRes(1002),
    /**
     * <pre>
     *获取服务器列表
     * </pre>
     *
     * <code>ServerListReq = 1003;</code>
     */
    ServerListReq(1003),
    /**
     * <pre>
     *获取服务器列表
     * </pre>
     *
     * <code>ServerListRes = 1004;</code>
     */
    ServerListRes(1004),
    /**
     * <pre>
     *改变角色服务器
     * </pre>
     *
     * <code>ChangeRoleServerReq = 1005;</code>
     */
    ChangeRoleServerReq(1005),
    /**
     * <pre>
     *改变角色服务器
     * </pre>
     *
     * <code>ChangeRoleServerRes = 1006;</code>
     */
    ChangeRoleServerRes(1006),
    /**
     * <pre>
     *服务器事件
     * </pre>
     *
     * <code>ServerEventReq = 1007;</code>
     */
    ServerEventReq(1007),
    /**
     * <pre>
     *服务器事件
     * </pre>
     *
     * <code>ServerEventRes = 1008;</code>
     */
    ServerEventRes(1008),
    /**
     * <pre>
     *大厅
     * </pre>
     *
     * <code>LoginReq = 10001;</code>
     */
    LoginReq(10001),
    /**
     * <pre>
     *登录
     * </pre>
     *
     * <code>LoginRes = 10002;</code>
     */
    LoginRes(10002),
    /**
     * <pre>
     *登录子游戏
     * </pre>
     *
     * <code>LoginSubGameReq = 10003;</code>
     */
    LoginSubGameReq(10003),
    /**
     * <pre>
     *登录子游戏
     * </pre>
     *
     * <code>LoginSubGameRes = 10004;</code>
     */
    LoginSubGameRes(10004),
    /**
     * <pre>
     *系统错误消息返回
     * </pre>
     *
     * <code>SystemErrorRes = 10006;</code>
     */
    SystemErrorRes(10006),
    /**
     * <pre>
     *退出游戏
     * </pre>
     *
     * <code>QuitReq = 10007;</code>
     */
    QuitReq(10007),
    /**
     * <pre>
     *退出游戏
     * </pre>
     *
     * <code>QuitRes = 10008;</code>
     */
    QuitRes(10008),
    /**
     * <pre>
     *退出子游戏
     * </pre>
     *
     * <code>QuitSubGameReq = 10009;</code>
     */
    QuitSubGameReq(10009),
    /**
     * <pre>
     *退出子游戏
     * </pre>
     *
     * <code>QuitSubGameRes = 10010;</code>
     */
    QuitSubGameRes(10010),
    /**
     * <pre>
     *心跳
     * </pre>
     *
     * <code>HeartReq = 10011;</code>
     */
    HeartReq(10011),
    /**
     * <pre>
     *心跳
     * </pre>
     *
     * <code>HeartRes = 10012;</code>
     */
    HeartRes(10012),
    /**
     * <pre>
     *udp连接请求
     * </pre>
     *
     * <code>UdpConnectReq = 10013;</code>
     */
    UdpConnectReq(10013),
    /**
     * <pre>
     *udp连接请求
     * </pre>
     *
     * <code>UdpConnectRes = 10014;</code>
     */
    UdpConnectRes(10014),
    /**
     * <pre>
     *聊天
     * </pre>
     *
     * <code>ChatReq = 10015;</code>
     */
    ChatReq(10015),
    /**
     * <pre>
     *聊天
     * </pre>
     *
     * <code>ChatRes = 10016;</code>
     */
    ChatRes(10016),
    /**
     * <pre>
     *背包列表
     * </pre>
     *
     * <code>PacketItemsReq = 10017;</code>
     */
    PacketItemsReq(10017),
    /**
     * <pre>
     *背包列表
     * </pre>
     *
     * <code>PacketItemsRes = 10018;</code>
     */
    PacketItemsRes(10018),
    /**
     * <pre>
     *使用道具
     * </pre>
     *
     * <code>UseItemReq = 10019;</code>
     */
    UseItemReq(10019),
    /**
     * <pre>
     *使用道具
     * </pre>
     *
     * <code>UseItemRes = 10020;</code>
     */
    UseItemRes(10020),
    /**
     * <pre>
     *邮件列表
     * </pre>
     *
     * <code>MailListReq = 10021;</code>
     */
    MailListReq(10021),
    /**
     * <pre>
     *邮件列表
     * </pre>
     *
     * <code>MailListRes = 10022;</code>
     */
    MailListRes(10022),
    /**
     * <pre>
     *修改邮件状态
     * </pre>
     *
     * <code>ModifyMailReq = 10023;</code>
     */
    ModifyMailReq(10023),
    /**
     * <pre>
     *修改邮件状态
     * </pre>
     *
     * <code>ModifyMailRes = 10024;</code>
     */
    ModifyMailRes(10024),
    /**
     * <pre>
     *公会详细信息
     * </pre>
     *
     * <code>GuildInfoReq = 10025;</code>
     */
    GuildInfoReq(10025),
    /**
     * <pre>
     *公会详细信息
     * </pre>
     *
     * <code>GuildInfoRes = 10026;</code>
     */
    GuildInfoRes(10026),
    /**
     * <pre>
     *公会列表
     * </pre>
     *
     * <code>GuildListReq = 10027;</code>
     */
    GuildListReq(10027),
    /**
     * <pre>
     *公会列表
     * </pre>
     *
     * <code>GuildListRes = 10028;</code>
     */
    GuildListRes(10028),
    /**
     * <pre>
     *申请加入公会
     * </pre>
     *
     * <code>ApplyGuildReq = 10029;</code>
     */
    ApplyGuildReq(10029),
    /**
     * <pre>
     *申请加入公会
     * </pre>
     *
     * <code>ApplyGuildRes = 10030;</code>
     */
    ApplyGuildRes(10030),
    /**
     * <pre>
     *公会审批
     * </pre>
     *
     * <code>GuildApprovalReq = 10031;</code>
     */
    GuildApprovalReq(10031),
    /**
     * <pre>
     *公会审批
     * </pre>
     *
     * <code>GuildApprovalRes = 10032;</code>
     */
    GuildApprovalRes(10032),
    /**
     * <pre>
     *创建公会
     * </pre>
     *
     * <code>CreateGuildReq = 10033;</code>
     */
    CreateGuildReq(10033),
    /**
     * <pre>
     *创建公会
     * </pre>
     *
     * <code>CreateGuildRes = 10034;</code>
     */
    CreateGuildRes(10034),
    /**
     * <pre>
     *捕鱼达人
     * </pre>
     *
     * <code>EnterRoomReq = 20001;</code>
     */
    EnterRoomReq(20001),
    /**
     * <pre>
     *进入房间
     * </pre>
     *
     * <code>EnterRoomRes = 20002;</code>
     */
    EnterRoomRes(20002),
    /**
     * <pre>
     *报名竞技赛
     * </pre>
     *
     * <code>ApplyAthleticsReq = 20003;</code>
     */
    ApplyAthleticsReq(20003),
    /**
     * <pre>
     *报名竞技赛
     * </pre>
     *
     * <code>ApplyAthleticsRes = 20004;</code>
     */
    ApplyAthleticsRes(20004),
    /**
     * <pre>
     *退出房间
     * </pre>
     *
     * <code>QuitRoomReq = 20005;</code>
     */
    QuitRoomReq(20005),
    /**
     * <pre>
     *退出房间
     * </pre>
     *
     * <code>QuitRoomRes = 20006;</code>
     */
    QuitRoomRes(20006),
    /**
     * <pre>
     *鱼进入房间
     * </pre>
     *
     * <code>FishEnterRoomRes = 20008;</code>
     */
    FishEnterRoomRes(20008),
    /**
     * <pre>
     *房间信息
     * </pre>
     *
     * <code>RoomInfoRes = 20010;</code>
     */
    RoomInfoRes(20010),
    /**
     * <pre>
     *房间改变
     * </pre>
     *
     * <code>RoomChangeRes = 20012;</code>
     */
    RoomChangeRes(20012),
    /**
     * <pre>
     *使用技能
     * </pre>
     *
     * <code>UseSkillReq = 20013;</code>
     */
    UseSkillReq(20013),
    /**
     * <pre>
     *使用技能
     * </pre>
     *
     * <code>UseSkillRes = 20014;</code>
     */
    UseSkillRes(20014),
    /**
     * <pre>
     *开炮
     * </pre>
     *
     * <code>FireReq = 20015;</code>
     */
    FireReq(20015),
    /**
     * <pre>
     *开炮
     * </pre>
     *
     * <code>FireRes = 20016;</code>
     */
    FireRes(20016),
    /**
     * <pre>
     *伤害结果
     * </pre>
     *
     * <code>FireResultReq = 20017;</code>
     */
    FireResultReq(20017),
    /**
     * <pre>
     *伤害结果
     * </pre>
     *
     * <code>FireResultRes = 20018;</code>
     */
    FireResultRes(20018),
    /**
     * <pre>
     *升级炮台
     * </pre>
     *
     * <code>GunLeveUpReq = 20019;</code>
     */
    GunLeveUpReq(20019),
    /**
     * <pre>
     *升级炮台
     * </pre>
     *
     * <code>GunLeveUpRes = 20020;</code>
     */
    GunLeveUpRes(20020),
    ;

    /**
     * <pre>
     *服务器内部消息
     * </pre>
     *
     * <code>ServerRegisterReq = 1001;</code>
     */
    public static final int ServerRegisterReq_VALUE = 1001;
    /**
     * <pre>
     *注册服务器消息
     * </pre>
     *
     * <code>ServerRegisterRes = 1002;</code>
     */
    public static final int ServerRegisterRes_VALUE = 1002;
    /**
     * <pre>
     *获取服务器列表
     * </pre>
     *
     * <code>ServerListReq = 1003;</code>
     */
    public static final int ServerListReq_VALUE = 1003;
    /**
     * <pre>
     *获取服务器列表
     * </pre>
     *
     * <code>ServerListRes = 1004;</code>
     */
    public static final int ServerListRes_VALUE = 1004;
    /**
     * <pre>
     *改变角色服务器
     * </pre>
     *
     * <code>ChangeRoleServerReq = 1005;</code>
     */
    public static final int ChangeRoleServerReq_VALUE = 1005;
    /**
     * <pre>
     *改变角色服务器
     * </pre>
     *
     * <code>ChangeRoleServerRes = 1006;</code>
     */
    public static final int ChangeRoleServerRes_VALUE = 1006;
    /**
     * <pre>
     *服务器事件
     * </pre>
     *
     * <code>ServerEventReq = 1007;</code>
     */
    public static final int ServerEventReq_VALUE = 1007;
    /**
     * <pre>
     *服务器事件
     * </pre>
     *
     * <code>ServerEventRes = 1008;</code>
     */
    public static final int ServerEventRes_VALUE = 1008;
    /**
     * <pre>
     *大厅
     * </pre>
     *
     * <code>LoginReq = 10001;</code>
     */
    public static final int LoginReq_VALUE = 10001;
    /**
     * <pre>
     *登录
     * </pre>
     *
     * <code>LoginRes = 10002;</code>
     */
    public static final int LoginRes_VALUE = 10002;
    /**
     * <pre>
     *登录子游戏
     * </pre>
     *
     * <code>LoginSubGameReq = 10003;</code>
     */
    public static final int LoginSubGameReq_VALUE = 10003;
    /**
     * <pre>
     *登录子游戏
     * </pre>
     *
     * <code>LoginSubGameRes = 10004;</code>
     */
    public static final int LoginSubGameRes_VALUE = 10004;
    /**
     * <pre>
     *系统错误消息返回
     * </pre>
     *
     * <code>SystemErrorRes = 10006;</code>
     */
    public static final int SystemErrorRes_VALUE = 10006;
    /**
     * <pre>
     *退出游戏
     * </pre>
     *
     * <code>QuitReq = 10007;</code>
     */
    public static final int QuitReq_VALUE = 10007;
    /**
     * <pre>
     *退出游戏
     * </pre>
     *
     * <code>QuitRes = 10008;</code>
     */
    public static final int QuitRes_VALUE = 10008;
    /**
     * <pre>
     *退出子游戏
     * </pre>
     *
     * <code>QuitSubGameReq = 10009;</code>
     */
    public static final int QuitSubGameReq_VALUE = 10009;
    /**
     * <pre>
     *退出子游戏
     * </pre>
     *
     * <code>QuitSubGameRes = 10010;</code>
     */
    public static final int QuitSubGameRes_VALUE = 10010;
    /**
     * <pre>
     *心跳
     * </pre>
     *
     * <code>HeartReq = 10011;</code>
     */
    public static final int HeartReq_VALUE = 10011;
    /**
     * <pre>
     *心跳
     * </pre>
     *
     * <code>HeartRes = 10012;</code>
     */
    public static final int HeartRes_VALUE = 10012;
    /**
     * <pre>
     *udp连接请求
     * </pre>
     *
     * <code>UdpConnectReq = 10013;</code>
     */
    public static final int UdpConnectReq_VALUE = 10013;
    /**
     * <pre>
     *udp连接请求
     * </pre>
     *
     * <code>UdpConnectRes = 10014;</code>
     */
    public static final int UdpConnectRes_VALUE = 10014;
    /**
     * <pre>
     *聊天
     * </pre>
     *
     * <code>ChatReq = 10015;</code>
     */
    public static final int ChatReq_VALUE = 10015;
    /**
     * <pre>
     *聊天
     * </pre>
     *
     * <code>ChatRes = 10016;</code>
     */
    public static final int ChatRes_VALUE = 10016;
    /**
     * <pre>
     *背包列表
     * </pre>
     *
     * <code>PacketItemsReq = 10017;</code>
     */
    public static final int PacketItemsReq_VALUE = 10017;
    /**
     * <pre>
     *背包列表
     * </pre>
     *
     * <code>PacketItemsRes = 10018;</code>
     */
    public static final int PacketItemsRes_VALUE = 10018;
    /**
     * <pre>
     *使用道具
     * </pre>
     *
     * <code>UseItemReq = 10019;</code>
     */
    public static final int UseItemReq_VALUE = 10019;
    /**
     * <pre>
     *使用道具
     * </pre>
     *
     * <code>UseItemRes = 10020;</code>
     */
    public static final int UseItemRes_VALUE = 10020;
    /**
     * <pre>
     *邮件列表
     * </pre>
     *
     * <code>MailListReq = 10021;</code>
     */
    public static final int MailListReq_VALUE = 10021;
    /**
     * <pre>
     *邮件列表
     * </pre>
     *
     * <code>MailListRes = 10022;</code>
     */
    public static final int MailListRes_VALUE = 10022;
    /**
     * <pre>
     *修改邮件状态
     * </pre>
     *
     * <code>ModifyMailReq = 10023;</code>
     */
    public static final int ModifyMailReq_VALUE = 10023;
    /**
     * <pre>
     *修改邮件状态
     * </pre>
     *
     * <code>ModifyMailRes = 10024;</code>
     */
    public static final int ModifyMailRes_VALUE = 10024;
    /**
     * <pre>
     *公会详细信息
     * </pre>
     *
     * <code>GuildInfoReq = 10025;</code>
     */
    public static final int GuildInfoReq_VALUE = 10025;
    /**
     * <pre>
     *公会详细信息
     * </pre>
     *
     * <code>GuildInfoRes = 10026;</code>
     */
    public static final int GuildInfoRes_VALUE = 10026;
    /**
     * <pre>
     *公会列表
     * </pre>
     *
     * <code>GuildListReq = 10027;</code>
     */
    public static final int GuildListReq_VALUE = 10027;
    /**
     * <pre>
     *公会列表
     * </pre>
     *
     * <code>GuildListRes = 10028;</code>
     */
    public static final int GuildListRes_VALUE = 10028;
    /**
     * <pre>
     *申请加入公会
     * </pre>
     *
     * <code>ApplyGuildReq = 10029;</code>
     */
    public static final int ApplyGuildReq_VALUE = 10029;
    /**
     * <pre>
     *申请加入公会
     * </pre>
     *
     * <code>ApplyGuildRes = 10030;</code>
     */
    public static final int ApplyGuildRes_VALUE = 10030;
    /**
     * <pre>
     *公会审批
     * </pre>
     *
     * <code>GuildApprovalReq = 10031;</code>
     */
    public static final int GuildApprovalReq_VALUE = 10031;
    /**
     * <pre>
     *公会审批
     * </pre>
     *
     * <code>GuildApprovalRes = 10032;</code>
     */
    public static final int GuildApprovalRes_VALUE = 10032;
    /**
     * <pre>
     *创建公会
     * </pre>
     *
     * <code>CreateGuildReq = 10033;</code>
     */
    public static final int CreateGuildReq_VALUE = 10033;
    /**
     * <pre>
     *创建公会
     * </pre>
     *
     * <code>CreateGuildRes = 10034;</code>
     */
    public static final int CreateGuildRes_VALUE = 10034;
    /**
     * <pre>
     *捕鱼达人
     * </pre>
     *
     * <code>EnterRoomReq = 20001;</code>
     */
    public static final int EnterRoomReq_VALUE = 20001;
    /**
     * <pre>
     *进入房间
     * </pre>
     *
     * <code>EnterRoomRes = 20002;</code>
     */
    public static final int EnterRoomRes_VALUE = 20002;
    /**
     * <pre>
     *报名竞技赛
     * </pre>
     *
     * <code>ApplyAthleticsReq = 20003;</code>
     */
    public static final int ApplyAthleticsReq_VALUE = 20003;
    /**
     * <pre>
     *报名竞技赛
     * </pre>
     *
     * <code>ApplyAthleticsRes = 20004;</code>
     */
    public static final int ApplyAthleticsRes_VALUE = 20004;
    /**
     * <pre>
     *退出房间
     * </pre>
     *
     * <code>QuitRoomReq = 20005;</code>
     */
    public static final int QuitRoomReq_VALUE = 20005;
    /**
     * <pre>
     *退出房间
     * </pre>
     *
     * <code>QuitRoomRes = 20006;</code>
     */
    public static final int QuitRoomRes_VALUE = 20006;
    /**
     * <pre>
     *鱼进入房间
     * </pre>
     *
     * <code>FishEnterRoomRes = 20008;</code>
     */
    public static final int FishEnterRoomRes_VALUE = 20008;
    /**
     * <pre>
     *房间信息
     * </pre>
     *
     * <code>RoomInfoRes = 20010;</code>
     */
    public static final int RoomInfoRes_VALUE = 20010;
    /**
     * <pre>
     *房间改变
     * </pre>
     *
     * <code>RoomChangeRes = 20012;</code>
     */
    public static final int RoomChangeRes_VALUE = 20012;
    /**
     * <pre>
     *使用技能
     * </pre>
     *
     * <code>UseSkillReq = 20013;</code>
     */
    public static final int UseSkillReq_VALUE = 20013;
    /**
     * <pre>
     *使用技能
     * </pre>
     *
     * <code>UseSkillRes = 20014;</code>
     */
    public static final int UseSkillRes_VALUE = 20014;
    /**
     * <pre>
     *开炮
     * </pre>
     *
     * <code>FireReq = 20015;</code>
     */
    public static final int FireReq_VALUE = 20015;
    /**
     * <pre>
     *开炮
     * </pre>
     *
     * <code>FireRes = 20016;</code>
     */
    public static final int FireRes_VALUE = 20016;
    /**
     * <pre>
     *伤害结果
     * </pre>
     *
     * <code>FireResultReq = 20017;</code>
     */
    public static final int FireResultReq_VALUE = 20017;
    /**
     * <pre>
     *伤害结果
     * </pre>
     *
     * <code>FireResultRes = 20018;</code>
     */
    public static final int FireResultRes_VALUE = 20018;
    /**
     * <pre>
     *升级炮台
     * </pre>
     *
     * <code>GunLeveUpReq = 20019;</code>
     */
    public static final int GunLeveUpReq_VALUE = 20019;
    /**
     * <pre>
     *升级炮台
     * </pre>
     *
     * <code>GunLeveUpRes = 20020;</code>
     */
    public static final int GunLeveUpRes_VALUE = 20020;


    public final int getNumber() {
      return value;
    }

    /**
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static MID valueOf(int value) {
      return forNumber(value);
    }

    public static MID forNumber(int value) {
      switch (value) {
        case 1001: return ServerRegisterReq;
        case 1002: return ServerRegisterRes;
        case 1003: return ServerListReq;
        case 1004: return ServerListRes;
        case 1005: return ChangeRoleServerReq;
        case 1006: return ChangeRoleServerRes;
        case 1007: return ServerEventReq;
        case 1008: return ServerEventRes;
        case 10001: return LoginReq;
        case 10002: return LoginRes;
        case 10003: return LoginSubGameReq;
        case 10004: return LoginSubGameRes;
        case 10006: return SystemErrorRes;
        case 10007: return QuitReq;
        case 10008: return QuitRes;
        case 10009: return QuitSubGameReq;
        case 10010: return QuitSubGameRes;
        case 10011: return HeartReq;
        case 10012: return HeartRes;
        case 10013: return UdpConnectReq;
        case 10014: return UdpConnectRes;
        case 10015: return ChatReq;
        case 10016: return ChatRes;
        case 10017: return PacketItemsReq;
        case 10018: return PacketItemsRes;
        case 10019: return UseItemReq;
        case 10020: return UseItemRes;
        case 10021: return MailListReq;
        case 10022: return MailListRes;
        case 10023: return ModifyMailReq;
        case 10024: return ModifyMailRes;
        case 10025: return GuildInfoReq;
        case 10026: return GuildInfoRes;
        case 10027: return GuildListReq;
        case 10028: return GuildListRes;
        case 10029: return ApplyGuildReq;
        case 10030: return ApplyGuildRes;
        case 10031: return GuildApprovalReq;
        case 10032: return GuildApprovalRes;
        case 10033: return CreateGuildReq;
        case 10034: return CreateGuildRes;
        case 20001: return EnterRoomReq;
        case 20002: return EnterRoomRes;
        case 20003: return ApplyAthleticsReq;
        case 20004: return ApplyAthleticsRes;
        case 20005: return QuitRoomReq;
        case 20006: return QuitRoomRes;
        case 20008: return FishEnterRoomRes;
        case 20010: return RoomInfoRes;
        case 20012: return RoomChangeRes;
        case 20013: return UseSkillReq;
        case 20014: return UseSkillRes;
        case 20015: return FireReq;
        case 20016: return FireRes;
        case 20017: return FireResultReq;
        case 20018: return FireResultRes;
        case 20019: return GunLeveUpReq;
        case 20020: return GunLeveUpRes;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<MID>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        MID> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<MID>() {
            public MID findValueByNumber(int number) {
              return MID.forNumber(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(ordinal());
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return com.xl.game.message.Mid.getDescriptor().getEnumTypes().get(0);
    }

    private static final MID[] VALUES = values();

    public static MID valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }

    private final int value;

    private MID(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:MID)
  }


  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\tMid.proto*\374\010\n\003MID\022\026\n\021ServerRegisterReq" +
      "\020\351\007\022\026\n\021ServerRegisterRes\020\352\007\022\022\n\rServerLis" +
      "tReq\020\353\007\022\022\n\rServerListRes\020\354\007\022\030\n\023ChangeRol" +
      "eServerReq\020\355\007\022\030\n\023ChangeRoleServerRes\020\356\007\022" +
      "\023\n\016ServerEventReq\020\357\007\022\023\n\016ServerEventRes\020\360" +
      "\007\022\r\n\010LoginReq\020\221N\022\r\n\010LoginRes\020\222N\022\024\n\017Login" +
      "SubGameReq\020\223N\022\024\n\017LoginSubGameRes\020\224N\022\023\n\016S" +
      "ystemErrorRes\020\226N\022\014\n\007QuitReq\020\227N\022\014\n\007QuitRe" +
      "s\020\230N\022\023\n\016QuitSubGameReq\020\231N\022\023\n\016QuitSubGame" +
      "Res\020\232N\022\r\n\010HeartReq\020\233N\022\r\n\010HeartRes\020\234N\022\022\n\r" +
      "UdpConnectReq\020\235N\022\022\n\rUdpConnectRes\020\236N\022\014\n\007" +
      "ChatReq\020\237N\022\014\n\007ChatRes\020\240N\022\023\n\016PacketItemsR" +
      "eq\020\241N\022\023\n\016PacketItemsRes\020\242N\022\017\n\nUseItemReq" +
      "\020\243N\022\017\n\nUseItemRes\020\244N\022\020\n\013MailListReq\020\245N\022\020" +
      "\n\013MailListRes\020\246N\022\022\n\rModifyMailReq\020\247N\022\022\n\r" +
      "ModifyMailRes\020\250N\022\021\n\014GuildInfoReq\020\251N\022\021\n\014G" +
      "uildInfoRes\020\252N\022\021\n\014GuildListReq\020\253N\022\021\n\014Gui" +
      "ldListRes\020\254N\022\022\n\rApplyGuildReq\020\255N\022\022\n\rAppl" +
      "yGuildRes\020\256N\022\025\n\020GuildApprovalReq\020\257N\022\025\n\020G" +
      "uildApprovalRes\020\260N\022\023\n\016CreateGuildReq\020\261N\022" +
      "\023\n\016CreateGuildRes\020\262N\022\022\n\014EnterRoomReq\020\241\234\001" +
      "\022\022\n\014EnterRoomRes\020\242\234\001\022\027\n\021ApplyAthleticsRe" +
      "q\020\243\234\001\022\027\n\021ApplyAthleticsRes\020\244\234\001\022\021\n\013QuitRo" +
      "omReq\020\245\234\001\022\021\n\013QuitRoomRes\020\246\234\001\022\026\n\020FishEnte" +
      "rRoomRes\020\250\234\001\022\021\n\013RoomInfoRes\020\252\234\001\022\023\n\rRoomC" +
      "hangeRes\020\254\234\001\022\021\n\013UseSkillReq\020\255\234\001\022\021\n\013UseSk" +
      "illRes\020\256\234\001\022\r\n\007FireReq\020\257\234\001\022\r\n\007FireRes\020\260\234\001" +
      "\022\023\n\rFireResultReq\020\261\234\001\022\023\n\rFireResultRes\020\262" +
      "\234\001\022\022\n\014GunLeveUpReq\020\263\234\001\022\022\n\014GunLeveUpRes\020\264" +
      "\234\001B\025\n\023com.xl.game.message"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}
